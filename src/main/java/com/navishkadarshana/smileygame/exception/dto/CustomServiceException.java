package com.navishkadarshana.smileygame.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Navishka Darshana - navishkada@gmail.com
 * @project  simple-game-backend
 * @CreatedBy IntelliJ IDEA
 * @created 26/02/2024 - 23.16
 */


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomServiceException extends RuntimeException {

    private String message;
    private HttpStatus httpStatus;
    private int code;

    public CustomServiceException(String message) {
        super(message);
        this.message = message;
    }

    public CustomServiceException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CustomServiceException(int code, String message, Throwable cause) {
        super(cause);
        this.message = message;
        this.code = code;
    }

    public CustomServiceException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public CustomServiceException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
