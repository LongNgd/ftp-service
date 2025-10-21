package vn.atdigital.ftpservice.domain.message;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseMessage implements Serializable {
    private String status;
    private boolean success;
    private String message;
}
