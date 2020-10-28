package com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.Sound;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class ResponseObject {
    private String process;
    private String step;
    private boolean nextStep;
    private CliParams cli;
    @Singular("responseEntry")
    private List<ResponseEntry> response;
    private JsonNode stateData;
    private Activity logActivity;
    private Sound soundId;
}
