package com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response;

import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.ResponseStyle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ResponseEntry {
    private final String text;
    private ResponseStyle style = ResponseStyle.NORMAL;
}
