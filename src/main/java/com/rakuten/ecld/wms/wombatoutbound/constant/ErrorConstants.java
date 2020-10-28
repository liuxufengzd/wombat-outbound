package com.rakuten.ecld.wms.wombatoutbound.constant;

/**
 * Error constants
 */
public final class ErrorConstants {

    /**
     * The Constant SERVER_ERR_END.
     */
    public static final int SERVER_ERR_END = 599;

    /**
     * The Constant SERVER_ERR_START.
     */
    public static final int SERVER_ERR_START = 500;

    /**
     * The Constant CLIENT_ERR_END.
     */
    public static final int CLIENT_ERR_END = 499;

    /**
     * The Constant CLIENT_ERR_START.
     */
    public static final int CLIENT_ERR_START = 400;

    /**
     * The Constant NOT_FOUND.
     */
    public static final int NOT_FOUND = 404;

    /**
     * The Constant BAD_REQUEST.
     */
    public static final int BAD_REQUEST = 400;

    /**
     * The Constant FORBIDDEN.
     */
    public static final int FORBIDDEN = 403;

    /**
     * The Constant SUCCESS.
     */
    public static final int SUCCESS = 200;

    public enum ResponseCodes {

        // DB error if you want to throw error but don't want to render in UI
        // INVALID_REQUEST if you want to throw error and want to render in UI
        LOGIN_ERROR(1000), ACCESS_TOKEN_EXPIRED_ERROR(1001), INVALID_REQUEST(1002),
        VALIDATION_ERROR(1003), SERVER_ERROR(1004), DB_ERROR(1005), DATA_NOT_FOUND(1006),
        FORBIDDEN(403);

        private final int code;

        ResponseCodes(final int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    private ErrorConstants() {

    }
}
