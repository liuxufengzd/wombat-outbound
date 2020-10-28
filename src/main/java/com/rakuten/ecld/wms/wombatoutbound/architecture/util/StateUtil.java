package com.rakuten.ecld.wms.wombatoutbound.architecture.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakuten.ecld.wms.wombatoutbound.architecture.exception.CommandExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StateUtil {
    private final ObjectMapper jackson;

    public Object deserializeState(JsonNode stateData, Object state) {
        Object result = null;
        if (stateData != null) {
            try {
                result = jackson.treeToValue(stateData, state.getClass());
            } catch (JsonProcessingException e) {
                throw new CommandExecutionException("Failed to deserialize state", e);
            }
        }
        return result != null ? result : state;
    }

    public JsonNode serialize(Object state) {
        return jackson.valueToTree(state);
    }
}
