package com.rakuten.ecld.wms.wombatoutbound.architecture.common;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;

@FunctionalInterface
public interface Condition<T> {
    boolean evaluate(CliHandler<T> cliHandler);
}
