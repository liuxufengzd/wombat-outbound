package com.rakuten.ecld.wms.wombatoutbound.architecture.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommandExecutionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CommandExecutionException(String message) {
        super(message);
    }

    public CommandExecutionException(String message, Exception cause) {
        super(message, cause);
    }
}
