package com.rakuten.ecld.wms.wombatoutbound.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseError implements Serializable {

    private static final long serialVersionUID = -3387516993124453948L;

    public BaseError(final String field, final String message) {
        this.field = field;
        this.message = message;
        this.messages = null;
    }

    private final String field;
    private final String message;
    private final List<String> messages;

}
