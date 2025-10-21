package vn.atdigital.ftpservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FtpService {
    List<String> listFiles(String filePath, List<String> extensions) throws IOException;

    List<String> listFiles(Long assetId, String assetType, List<String> extensions) throws IOException;

    String uploadFile(Long assetId, String assetType, MultipartFile file) throws IOException;

    byte[] getFile(String filePath) throws IOException;

    byte[] getFile(Long assetId, String assetType, String fileName) throws IOException;

    void deleteFile(String filePath) throws IOException;

    void deleteFile(Long assetId, String assetType, String fileName) throws IOException;
}
