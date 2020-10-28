package com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Activity {

    private String type;
    private long count;
}
