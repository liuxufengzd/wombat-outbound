package com.rakuten.ecld.wms.wombatoutbound.architecture.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * If we run a model with any step that contains continueAtRoot or runAndContinueAtRoot or callerIs,
 * the state class has to extends this baseState class
 */

@Getter
@Setter
public class BaseState {
    private final Map<String,String> flowToRootStep = new HashMap<>();
    private final Map<String,String> flowToCallerStep = new HashMap<>();
}
