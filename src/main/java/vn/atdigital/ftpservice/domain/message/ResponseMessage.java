package vn.atdigital.ftpservice.domain.message;

import lombok.*;

/**
 * CommonController
 *
 * @author hoangtd5
 * @version 1.0
 * @since 5/9/2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode(callSuper = true)
public class ResponseMessage<T> extends BaseMessage {
    private T data;
}
