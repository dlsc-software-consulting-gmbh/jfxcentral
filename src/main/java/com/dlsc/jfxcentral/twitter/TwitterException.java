package com.dlsc.jfxcentral.twitter;

/**
 * This class represents exception that can be thrown by the {@link TwitterService}
 *
 * @author Thierry Wasylczenko
 * @version 1.1-SNAPSHOT
 * @since SlideshowFX 2.0
 */
public class TwitterException extends RuntimeException {
    public enum ErrorCodes {
        UNAUTHORIZED_APPLICATION,
        NOT_AUTHENTICATED,
        UNKNOWN_ERROR
    }

    protected final ErrorCodes code;

    public TwitterException(ErrorCodes code) {
        this(code, null);
    }

    public TwitterException(ErrorCodes code, String message) {
        this(code, message, null);
    }

    public TwitterException(ErrorCodes code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}