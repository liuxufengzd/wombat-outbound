package com.rakuten.ecld.wms.wombatoutbound.architecture.common;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;

@FunctionalInterface
public interface StepHandler<T> {
    void execute(CliHandler<T> cliHandler);
}
