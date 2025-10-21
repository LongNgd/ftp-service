package vn.atdigital.ftpservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class FtpServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtpServiceApplication.class, args);
    }

}
