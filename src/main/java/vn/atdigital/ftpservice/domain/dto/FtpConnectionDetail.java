package vn.atdigital.ftpservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class FtpConnectionDetail {
    @Value("${ftp.connection.host}")
    private String host;

    @Value("${ftp.connection.port}")
    private int port;

    @Value("${ftp.connection.username}")
    private String username;

    @Value("${ftp.connection.password}")
    private String password;
}
