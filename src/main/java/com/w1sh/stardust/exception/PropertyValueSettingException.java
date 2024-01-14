package com.w1sh.stardust.exception;

public class PropertyValueSettingException extends RuntimeException {

    public PropertyValueSettingException(String message) {
        super(message);
    }

    public PropertyValueSettingException(String message, Throwable cause) {
        super(message, cause);
    }

    public static PropertyValueSettingException alreadySet(String keyName) {
        return new PropertyValueSettingException(String.format("Value for key %s is already present and overriding is not allowed.", keyName));
    }
}
