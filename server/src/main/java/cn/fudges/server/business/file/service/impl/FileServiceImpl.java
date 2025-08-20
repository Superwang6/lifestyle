package cn.fudges.server.business.file.service.impl;

import cn.fudges.server.common.RedisKeys;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.config.properties.ProjectConfigProperties;
import cn.fudges.server.business.file.entity.FileUpload;
import cn.fudges.server.business.file.enums.FileBusinessType;
import cn.fudges.server.business.file.request.FileUploadRequest;
import cn.fudges.server.business.file.service.FileService;
import cn.fudges.server.business.file.service.FileUploadService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author 王平远
 * @since 2025/7/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ProjectConfigProperties projectConfigProperties;

    private final FileUploadService fileUploadService;

    private final RedissonClient redissonClient;

    private static final List<String> supportedSuffix = List.of("jpg", "png", "jpeg", "gif", "pdf", "doc", "docx", "xls", "xlsx");

    @Override
    public List<FileUpload> uploadFile(MultipartFile[] files, String businessName, Long userId) {
        AssertUtils.isTrue(files != null && files.length > 0, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotBlank(businessName, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isTrue(FileBusinessType.check(businessName), ResultCodeEnum.PARAM_ERROR, "业务类型不支持" );
        for (MultipartFile file : files) {
            AssertUtils.isTrue(!file.isEmpty(), ResultCodeEnum.PARAM_ERROR, "文件为空");
            AssertUtils.isTrue(file.getSize() / 1024 < 1024 * 1024 * 20, ResultCodeEnum.PARAM_ERROR, "文件过大");
            String fileName = file.getOriginalFilename();
            AssertUtils.isNotBlank(fileName, ResultCodeEnum.PARAM_ERROR, "文件异常，请重新提交");
            String[] fileInfo = fileName.split("\\.");
            AssertUtils.isTrue(fileInfo.length > 1, ResultCodeEnum.PARAM_ERROR, "文件异常，请重新提交");
            AssertUtils.isTrue(supportedSuffix.contains(fileInfo[1].toLowerCase()), ResultCodeEnum.PARAM_ERROR, "文件格式不支持");
            try {
                AssertUtils.isTrue(supportedSuffix.contains(FileTypeUtil.getType(file.getInputStream())), ResultCodeEnum.PARAM_ERROR, "文件格式不支持");
            } catch (IOException e) {
                log.error("check file suffix error", e);
            }
        }

        List<FileUpload> fileUploads = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if(StrUtil.isNotBlank(fileName)) {
                String uuid = IdUtil.simpleUUID();
                String[] fileInfo = fileName.split("\\.");
                String path = "/" + businessName + "/" + DateUtil.year(new Date()) + "/" + (DateUtil.month(new Date()) + 1) + "/" + uuid + "." + fileInfo[1];
                String fullPath = projectConfigProperties.getFile().getUploadDir() + path;
                File dest = new File(fullPath);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                try {
                    file.transferTo(dest);
                } catch (IOException e) {
                    log.error("file upload error", e);
                    continue;
                }
                FileUpload fileUpload = new FileUpload();
                fileUpload.setId(uuid);
                fileUpload.setFilePath(path);
                fileUpload.setName(fileInfo[0]);
                fileUpload.setSuffix(fileInfo[1]);
                fileUpload.setSize((int) (file.getSize() / 1024));
                fileUpload.setCreateTime(LocalDateTime.now());
                fileUpload.setBusinessName(businessName);
                fileUpload.setUserId(userId);
                fileUploads.add(fileUpload);
            }
        }
        if(CollUtil.isNotEmpty(fileUploads)) {
            fileUploadService.saveBatch(fileUploads);
        }

        return fileUploads;
    }

    @Override
    public List<FileUpload> uploadBase64(FileUploadRequest request) {
        String[] base64s = ArrayUtil.addAll(request.getBase64s(), new String[]{request.getBase64()});
        return uploadFileBase64(base64s, request.getBusinessName(), request.getUserId());
    }

    private List<FileUpload> uploadFileBase64(String[] base64s, String businessName, Long userId) {
        AssertUtils.isTrue(base64s != null && base64s.length > 0, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotBlank(businessName, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isTrue(FileBusinessType.check(businessName), ResultCodeEnum.PARAM_ERROR, "业务类型不支持" );
        List<FileUpload> fileUploadList = new ArrayList<>();
        for (String base64 : base64s) {
            AssertUtils.isNotBlank(base64, ResultCodeEnum.PARAM_ERROR);
            AssertUtils.isTrue(base64.contains(","), ResultCodeEnum.PARAM_ERROR, "base64格式错误");

            String[] data = base64.split(",");
            String base64Data = data[1];
            byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
            AssertUtils.isTrue(decodedBytes.length < 1024 * 1024 * 20, ResultCodeEnum.PARAM_ERROR, "文件过大");
            String prefix = data[0];
            if(prefix.contains(";base64")) {
                prefix = prefix.substring(0, prefix.indexOf(";base64"));
            }
            String suffix = prefix.substring(prefix.lastIndexOf("/") + 1);
            AssertUtils.isTrue(supportedSuffix.contains(suffix.toLowerCase()), ResultCodeEnum.PARAM_ERROR, "文件格式不支持");
            AssertUtils.isTrue(supportedSuffix.contains(FileTypeUtil.getType(new ByteArrayInputStream(decodedBytes))), ResultCodeEnum.PARAM_ERROR, "文件格式不支持");

            FileUpload fileUpload = new FileUpload();
            fileUpload.setId(IdUtil.simpleUUID());
            fileUpload.setName(fileUpload.getId());
            fileUpload.setSuffix(suffix);
            fileUpload.setSize(decodedBytes.length / 1024);
            fileUpload.setBase64(decodedBytes);
            fileUpload.setCreateTime(LocalDateTime.now());
            fileUpload.setBusinessName(businessName);
            fileUpload.setUserId(userId);
            fileUploadList.add(fileUpload);
        }

        List<FileUpload> saveFileList = new ArrayList<>();
        for (FileUpload fileUpload : fileUploadList) {
            String path = "/" + businessName + "/" + DateUtil.year(new Date()) + "/" + (DateUtil.month(new Date()) + 1) + "/" + fileUpload.getId() + "." + fileUpload.getSuffix();
            String fullPath = projectConfigProperties.getFile().getUploadDir() + path;
            File dest = new File(fullPath);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try (FileOutputStream out = new FileOutputStream(dest)) {
                out.write(fileUpload.getBase64());
            } catch (IOException e) {
                log.error("file upload error", e);
                continue;
            }
            fileUpload.setFilePath(path);
            saveFileList.add(fileUpload);
        }
        if(CollUtil.isNotEmpty(saveFileList)) {
            fileUploadService.saveBatch(saveFileList);
        }

        return saveFileList;
    }

    @Override
    public Boolean deleteFileAsync(String id, Long userId) {
        AssertUtils.isNotBlank(id, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(userId, ResultCodeEnum.PARAM_ERROR);

        FileUpload fileUpload = fileUploadService.getById(id);
        AssertUtils.isNotNull(fileUpload, ResultCodeEnum.PARAM_ERROR);

        fileUploadService.removeById(id);

        File file = new File(projectConfigProperties.getFile().getUploadDir() + fileUpload.getFilePath());
        if(file.exists()) {
            return file.delete();
        }
        return false;
    }

    @Override
    public ResponseEntity<Resource> viewFile(String id, Long userId) {
        AssertUtils.isNotBlank(id, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(userId, ResultCodeEnum.PARAM_ERROR);

        FileUpload fileUpload = fileUploadService.getById(id);
        AssertUtils.isNotNull(fileUpload, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isTrue(userId.equals(fileUpload.getUserId()), ResultCodeEnum.PERMISSION_DENIED);

        File file = new File(projectConfigProperties.getFile().getUploadDir(), fileUpload.getFilePath());

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok().contentType(MediaType.IMAGE_PNG);
        if(file.exists()) {
            try {
                 return builder.body(new UrlResource(file.toURI()));
            } catch (MalformedURLException e) {
                log.error("file to resource error", e);
                AssertUtils.isTrue(false, ResultCodeEnum.BUSINESS_EXCEPTION, "读取文件失败");
            }
        }
        return null;
    }

    @Override
    public void downloadFile(HttpServletResponse response, String id, Long userId) {
        AssertUtils.isNotBlank(id, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(userId, ResultCodeEnum.PARAM_ERROR);

        FileUpload fileUpload = fileUploadService.getById(id);
        AssertUtils.isNotNull(fileUpload, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isTrue(userId.equals(fileUpload.getUserId()), ResultCodeEnum.PERMISSION_DENIED);

        File file = new File(projectConfigProperties.getFile().getUploadDir(), fileUpload.getFilePath());
        if(file.exists()) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileUpload.getName() + "." + fileUpload.getSuffix() + "\"");

            try (InputStream in = new FileInputStream(file); OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } catch (IOException e) {
                log.error("file download error", e);
            }
        }
    }

    @Override
    public String queryFileUrl(String id, Long userId) {
        AssertUtils.isNotBlank(id, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(userId, ResultCodeEnum.PARAM_ERROR);

        String path;
        RBucket<String> idBucket = redissonClient.getBucket(RedisKeys.FILE_REFRESH_ID_KEY + id + "_" + userId);
        if(idBucket.isExists()) {
            path = idBucket.get();
        } else {
            FileUpload fileUpload = fileUploadService.getById(id);
            AssertUtils.isNotNull(fileUpload, ResultCodeEnum.PARAM_ERROR);
            AssertUtils.isTrue(userId.equals(fileUpload.getUserId()), ResultCodeEnum.PERMISSION_DENIED);
            path = projectConfigProperties.getDomain() + fileUpload.getFilePath();
            String uuid = IdUtil.fastSimpleUUID();
            path += "?sign=" + uuid;
            RBucket<Object> signBucket = redissonClient.getBucket(RedisKeys.FILE_REFRESH_SIGN_KEY + uuid);
            signBucket.set(1, Duration.ofMinutes(10));
            idBucket.set(path, Duration.ofMinutes(10));
        }

        return path;
    }
}
