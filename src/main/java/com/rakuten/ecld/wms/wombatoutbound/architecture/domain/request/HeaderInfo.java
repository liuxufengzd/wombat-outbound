package com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeaderInfo {
    private String token;
    private String terminalId;
    private String sellerType;
}
