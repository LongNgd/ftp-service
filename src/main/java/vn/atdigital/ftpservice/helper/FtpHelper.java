package vn.atdigital.ftpservice.helper;

import lombok.RequiredArgsConstructor;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import vn.atdigital.ftpservice.common.enums.AttachmentOwnerTypeEnum;
import vn.atdigital.ftpservice.domain.dto.FtpConnectionDetail;
import vn.atdigital.ftpservice.feignclient.TesterClient;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FtpHelper {
    private final TesterClient testerClient;

    public DefaultFtpSessionFactory connectFtp(FtpConnectionDetail ftpConnectionDetail) {
        DefaultFtpSessionFactory ftpSessionFactory = new DefaultFtpSessionFactory();
        ftpSessionFactory.setHost(ftpConnectionDetail.getHost());
        ftpSessionFactory.setPort(ftpConnectionDetail.getPort());
        ftpSessionFactory.setUsername(ftpConnectionDetail.getUsername());
        ftpSessionFactory.setPassword(ftpConnectionDetail.getPassword());

        // 2 = Passive mode
        ftpSessionFactory.setClientMode(2);

        // 2 = Binary mode
        ftpSessionFactory.setFileType(2);

        // Buffer để tăng tốc độ truyền
        ftpSessionFactory.setBufferSize(100000);

        // Bắt buộc FTP client sử dụng UTF-8
        ftpSessionFactory.setControlEncoding("UTF-8");

        return ftpSessionFactory;
    }

    public String getPath(Long assetId, String assetType) {
        AttachmentOwnerTypeEnum attachmentOwnerTypeEnum = AttachmentOwnerTypeEnum.fromValue(assetType);
        ResponseEntity<String> assetPathResponse = testerClient.getAssetPath(assetId, attachmentOwnerTypeEnum.toString());
        String assetPath = assetPathResponse.getBody();
        Assert.isTrue(assetPath != null, "Cannot get asset path of asset " + assetId + " type " + assetType);
        assetPath = assetPath.substring(assetPath.lastIndexOf(":\"") + 2, assetPath.length() - 2);

        return assetPath;
    }

    public void createDirectoriesRecursively(Session<FTPFile> session, String dirPath) throws IOException {
        String[] folders = dirPath.split("/");
        String currentPath = "";

        for (String folder : folders) {
            if (folder.isEmpty()) continue;
            currentPath += "/" + folder;
            if (!session.exists(currentPath)) {
                session.mkdir(currentPath);
            }
        }
    }
}
