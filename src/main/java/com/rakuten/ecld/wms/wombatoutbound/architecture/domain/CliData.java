package com.rakuten.ecld.wms.wombatoutbound.architecture.domain;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request.JWTUserDetail;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class CliData<T> {
    private final Object state;
    private final String input;
    private final String centerCode;
    private final String business;
    private final JWTUserDetail user;

    @SuppressWarnings("unchecked")
    public T getState() {
        return (T) state;
    }
}
