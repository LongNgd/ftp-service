package vn.atdigital.ftpservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.atdigital.ftpservice.service.FtpService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static vn.atdigital.ftpservice.common.Constants.API_RESPONSE.*;

@RestController
@RequestMapping("/ftp")
@RequiredArgsConstructor
public class FtpController extends CommonController {

    private final FtpService ftpService;

    @GetMapping("/list-by-path")
    public ResponseEntity<?> listFiles(
            @RequestParam(required = false) String filePath,
            @RequestParam(required = false) List<String> extensions) {
        try {
            List<String> files = ftpService.listFiles(filePath, extensions);
            return toSuccessResult(files, "Get list files successfully");
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR_NOTFOUND);
        }
    }

    @GetMapping("/list-by-asset")
    public ResponseEntity<?> listFiles(
            @RequestParam Long assetId,
            @RequestParam String assetType,
            @RequestParam(required = false) List<String> extensions) {
        try {
            List<String> files = ftpService.listFiles(assetId, assetType, extensions);
            return toSuccessResult(files, "Get list files successfully");
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR_NOTFOUND);
        }
    }

    @GetMapping("/get-by-path")
    public ResponseEntity<?> getFile(
            @RequestParam String filePath) {
        try {
            byte[] file = ftpService.getFile(filePath);

            String downloadFileName = filePath.substring(filePath.lastIndexOf("/") + 1);

            Path path = Paths.get(downloadFileName);
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadFileName + "\"")
                    .body(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-by-asset")
    public ResponseEntity<?> getFile(
            @RequestParam Long assetId,
            @RequestParam String assetType,
            @RequestParam String fileName
            ) {
        try {
            byte[] file = ftpService.getFile(assetId, assetType, fileName);

            Path path = Paths.get(fileName);
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam Long assetId,
            @RequestParam String assetType,
            @RequestParam MultipartFile file) {
        try {
            String filePath = ftpService.uploadFile(assetId, assetType, file);
            return toSuccessResult(filePath, "Upload file successfully");
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
        }
    }

    @DeleteMapping("/delete-by-path")
    public ResponseEntity<?> deleteFile(
            @RequestParam String filePath
    ) {
        try {
            ftpService.deleteFile(filePath);
            return toSuccessResult(filePath, "Delete file successfully");
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
        }
    }

    @DeleteMapping("/delete-by-asset ")
    public ResponseEntity<?> deleteFile(
            @RequestParam Long assetId,
            @RequestParam String assetType,
            @RequestParam String fileName
    ) {
        try {
            ftpService.deleteFile(assetId, assetType, fileName);
            return toSuccessResult(fileName, "Delete file successfully");
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
        }
    }
}
