package com.rakuten.ecld.wms.wombatoutbound.exception;

import com.rakuten.ecld.wms.wombatoutbound.constant.ErrorConstants;
import lombok.Getter;

import java.util.List;

@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -3387514353124229948L;

    private final ErrorConstants.ResponseCodes code;
    private final String message;
    private final List<BaseError> errors;

    public BusinessException(final String message) {
        this(message, ErrorConstants.ResponseCodes.SERVER_ERROR, null, null);
    }

    public BusinessException(final String message, final ErrorConstants.ResponseCodes code) {
        this(message, code, null, null);
    }

    public BusinessException(final String message, final Throwable err) {
        this(message, ErrorConstants.ResponseCodes.SERVER_ERROR, null, err);
    }

    public BusinessException(final String message, final ErrorConstants.ResponseCodes code,
                             final Throwable err) {
        this(message, code, null, err);
    }

    public BusinessException(final String message, final ErrorConstants.ResponseCodes code,
                             final List<BaseError> errors) {
        this(message, code, errors, null);
    }

    private BusinessException(final String message, final ErrorConstants.ResponseCodes code,
                              final List<BaseError> errors, final Throwable err) {
        super(message, err);
        this.message = message;
        this.code = code;
        this.errors = errors;
    }
}
