package com.rakuten.ecld.wms.wombatoutbound.architecture.common;

import com.rakuten.ecld.wms.wombatoutbound.architecture.core.Model;

public interface CommandHandler {
    String getCommand();
    String[] getAlias();
    Model getModel();
    void define();
    Object generateState();
}
