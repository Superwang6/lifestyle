package cn.fudges.server.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.request.FileUploadRequest;
import cn.fudges.server.response.FileUploadResponse;
import cn.fudges.server.service.FileService;
import cn.hutool.core.bean.BeanUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 王平远
 * @since 2025/7/24
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResultResponse<List<FileUploadResponse>> uploadFile(@RequestParam("files") MultipartFile[] files,@RequestParam("businessName") String businessName) {
        return ResultResponse.success(BeanUtil.copyToList(fileService.uploadFile(files, businessName, StpUtil.getLoginIdAsLong()), FileUploadResponse.class));
    }

    @PostMapping("/upload/base64")
    public ResultResponse<List<FileUploadResponse>> uploadBase64(@RequestBody FileUploadRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(BeanUtil.copyToList(fileService.uploadBase64(request), FileUploadResponse.class));
    }

    @PostMapping("/delete/{id}")
    public ResultResponse<Boolean> deleteFile(@PathVariable("id") String id) {
        return ResultResponse.success(fileService.deleteFileAsync(id, StpUtil.getLoginIdAsLong()));
    }

    @RequestMapping("/view/{id}")
    public ResponseEntity<Resource> viewFile(@PathVariable("id") String id) {
        return fileService.viewFile(id, StpUtil.getLoginIdAsLong());
    }

    @RequestMapping("/url/{id}")
    public ResultResponse<String> queryFileUrl(@PathVariable("id") String id) {
        return ResultResponse.success(fileService.queryFileUrl(id, StpUtil.getLoginIdAsLong()));
    }

    @GetMapping("/download/{id}")
    public void downloadFile(HttpServletResponse response, @PathVariable("id") String id) {
        fileService.downloadFile(response, id, StpUtil.getLoginIdAsLong());
    }

}
