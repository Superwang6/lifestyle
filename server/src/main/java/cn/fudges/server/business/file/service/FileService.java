package cn.fudges.server.business.file.service;

import cn.fudges.server.business.file.entity.FileUpload;
import cn.fudges.server.business.file.request.FileUploadRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 王平远
 * @since 2025/7/24
 */
public interface FileService {
    List<FileUpload> uploadFile(MultipartFile[] files, String businessName, Long userId);

    List<FileUpload> uploadBase64(FileUploadRequest request);

    Boolean deleteFileAsync(String id, Long userId);

    ResponseEntity<Resource> viewFile(String id, Long userId);

    void downloadFile(HttpServletResponse response, String id, Long userId );

    String queryFileUrl(String id, Long userId);
}
