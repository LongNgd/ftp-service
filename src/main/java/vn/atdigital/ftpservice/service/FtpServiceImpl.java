package vn.atdigital.ftpservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.atdigital.ftpservice.common.enums.ActivityEnum;
import vn.atdigital.ftpservice.common.enums.AttachmentOwnerTypeEnum;
import vn.atdigital.ftpservice.common.enums.ExtensionEnum;
import vn.atdigital.ftpservice.domain.dto.FtpConnectionDetail;
import vn.atdigital.ftpservice.domain.model.FileRecord;
import vn.atdigital.ftpservice.helper.ActivityRecordHelper;
import vn.atdigital.ftpservice.helper.FtpHelper;
import vn.atdigital.ftpservice.repository.FileRecordRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FtpServiceImpl implements FtpService {
    private final FtpConnectionDetail ftpConnectionDetail;
    private final FtpHelper ftpHelper;
    private final FileRecordRepository fileRecordRepository;
    private final ActivityRecordHelper activityRecordHelper;

    public List<String> listFiles(String filePath, List<String> extensions) throws IOException {
        DefaultFtpSessionFactory ftpSessionFactory = ftpHelper.connectFtp(ftpConnectionDetail);

        List<String> result = new ArrayList<>();

        try (Session<FTPFile> session = ftpSessionFactory.getSession()) {
            if (filePath != null && !filePath.isEmpty()) {
                Assert.isTrue(session.exists(filePath), "Incorrect path: " + filePath);
            }

            FTPFile[] files = session.list(filePath);

            if (files != null) {
                for (FTPFile file : files) {
                    if (file.isDirectory()) continue;
                    String filename = file.getName();

                    if (extensions == null || extensions.isEmpty()) {
                        result.add(filename);
                    } else {
                        for (String ext : extensions) {
                            Assert.isTrue(ExtensionEnum.checkExtension(ext), "Incorrect extension: " + ext);
                            if (filename.toLowerCase().endsWith(ext.toLowerCase())) {
                                result.add(filename);
                                break;
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    public List<String> listFiles(Long assetId, String assetType, List<String> extensions) throws IOException {
        String path = ftpHelper.getPath(assetId, assetType);
        return listFiles(path, extensions);
    }

    @Transactional
    public String uploadFile(Long assetId, String assetType, MultipartFile file) throws IOException {
        DefaultFtpSessionFactory ftpSessionFactory = ftpHelper.connectFtp(ftpConnectionDetail);

        String assetPath = ftpHelper.getPath(assetId, assetType);

        try (Session<FTPFile> session = ftpSessionFactory.getSession()) {
            ftpHelper.createDirectoriesRecursively(session, assetPath);

            String remoteFilePath = assetPath + "/" + file.getOriginalFilename();

            session.write(file.getInputStream(), remoteFilePath);

            FileRecord fileRecord = FileRecord.builder()
                    .name(file.getOriginalFilename())
                    .path(remoteFilePath)
                    .ownerId(assetId)
                    .ownerType(AttachmentOwnerTypeEnum.fromValue(assetType))
                    .build();

            fileRecord = fileRecordRepository.save(fileRecord);

            activityRecordHelper.createActivityRecord(fileRecord.getPath(), ActivityEnum.CREATE);

            return remoteFilePath;
        }
    }

    public byte[] getFile(String filePath) throws IOException {
        DefaultFtpSessionFactory ftpSessionFactory = ftpHelper.connectFtp(ftpConnectionDetail);

        Optional<FileRecord> fileRecord = fileRecordRepository.findByPath(filePath);
        Assert.isTrue(fileRecord.isPresent(), "File not found: " + filePath);

        try (Session<FTPFile> session = ftpSessionFactory.getSession()) {
            try (InputStream inputStream = session.readRaw(filePath)) {
                activityRecordHelper.createActivityRecord(fileRecord.get().getPath(), ActivityEnum.DOWNLOAD);
                return StreamUtils.copyToByteArray(inputStream);
            }
        }
    }

    public byte[] getFile(Long assetId, String assetType, String fileName) throws IOException {
        String filePath = ftpHelper.getPath(assetId, assetType) + "/" + fileName;
        return getFile(filePath);
    }

    @Transactional
    public void deleteFile(String filePath) throws IOException {
        DefaultFtpSessionFactory ftpSessionFactory = ftpHelper.connectFtp(ftpConnectionDetail);

        Optional<FileRecord> fileRecord = fileRecordRepository.findByPath(filePath);
        Assert.isTrue(fileRecord.isPresent(), "File not found: " + filePath);

        try (Session<FTPFile> session = ftpSessionFactory.getSession()) {
            session.remove(fileRecord.get().getPath());
            fileRecordRepository.delete(fileRecord.get());
            activityRecordHelper.createActivityRecord(fileRecord.get().getPath(), ActivityEnum.DELETE);
        }
    }

    @Transactional
    public void deleteFile(Long assetId, String assetType, String fileName) throws IOException {
        String filePath = ftpHelper.getPath(assetId, assetType) + "/" + fileName;
        deleteFile(filePath);
    }
}