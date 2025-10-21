package vn.atdigital.ftpservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vn.atdigital.ftpservice.domain.message.ResponseMessage;

import static vn.atdigital.ftpservice.common.Constants.API_RESPONSE.*;
import static vn.atdigital.ftpservice.common.Constants.STATUS_COMMON.*;

/**
 * CommonController provides utility methods to standardize API responses
 * across all controllers in the system.
 *
 * <p>Each method returns a {@link ResponseEntity} wrapping a {@link ResponseMessage},
 * ensuring a consistent API response format (status, success flag, message, data).</p>
 */
public abstract class CommonController {

    /**
     * Returns a success response with data and a custom success message.
     *
     * @param <T>            the type of the response data
     * @param data           the payload to be returned to the client
     * @param successMessage a descriptive message for the success case
     * @return {@link ResponseEntity} containing {@link ResponseMessage} with HTTP 200 (OK)
     */
    protected <T> ResponseEntity<?> toSuccessResult(T data, String successMessage) {
        ResponseMessage<T> message = new ResponseMessage<>();

        message.setStatus(RETURN_CODE_SUCCESS);
        message.setSuccess(RESPONSE_STATUS_TRUE);
        message.setMessage(successMessage);
        message.setData(data);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Returns an error response when an exception or failure occurs.
     *
     * @param <T>         the type of the response data (usually null in case of error)
     * @param errorMessage a descriptive error message
     * @param code         error code (defined in Constants)
     * @return {@link ResponseEntity} containing {@link ResponseMessage} with HTTP 400 (Bad Request)
     */
    protected <T> ResponseEntity<?> toExceptionResult(String errorMessage, String code) {
        ResponseMessage<T> message = new ResponseMessage<>();

        message.setSuccess(RESPONSE_STATUS_FALSE);
        message.setStatus(code);
        message.setMessage(errorMessage);

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Returns a success response without any data (only status and success flag).
     *
     * <p>Typically used for operations such as upload, delete, or update
     * where no response body is required.</p>
     *
     * @param <T> the type of the response data (commonly null)
     * @return {@link ResponseEntity} containing {@link ResponseMessage} with HTTP 200 (OK)
     */
    protected <T> ResponseEntity<?> toSuccessResultNull() {
        ResponseMessage<T> message = new ResponseMessage<>();

        message.setStatus(RETURN_CODE_SUCCESS);
        message.setSuccess(RESPONSE_STATUS_TRUE);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
