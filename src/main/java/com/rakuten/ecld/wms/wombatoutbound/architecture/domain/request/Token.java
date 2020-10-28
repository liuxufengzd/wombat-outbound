package com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class Token {
    private final String accessToken;
    private final Date expiresIn;
}
