package com.tordoya.subo.otp.exception;

public class OtpRoutingException extends RuntimeException {
    public OtpRoutingException(String message) {
        super(message);
    }

    public OtpRoutingException(String message, Throwable cause) {
        super(message, cause);
    }
}