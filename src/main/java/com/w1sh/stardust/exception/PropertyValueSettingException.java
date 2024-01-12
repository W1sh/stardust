package com.w1sh.stardust.exception;

public class PropertyValueSettingException extends RuntimeException {

    public PropertyValueSettingException(String message) {
        super(message);
    }

    public PropertyValueSettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
