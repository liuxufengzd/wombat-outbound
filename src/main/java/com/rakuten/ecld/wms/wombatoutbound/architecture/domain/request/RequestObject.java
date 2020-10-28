package com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class RequestObject {

    private String process;
    private String step;
    private String input;
    private JsonNode stateData;

    public void log() {
        log.info("process = {}", getProcess());
        log.info("step = {}", getStep());
        log.info("input = {}", getInput());
        if (getStateData() != null) {
            log.info("state = {}", stateData.toString());
        }
    }
}
