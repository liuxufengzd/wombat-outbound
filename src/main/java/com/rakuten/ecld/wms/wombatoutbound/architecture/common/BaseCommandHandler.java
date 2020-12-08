package com.rakuten.ecld.wms.wombatoutbound.architecture.common;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;

public class BaseCommandHandler<T> {

    protected boolean hasUserEnteredCancel(final CliHandler<T> cliHandler) {
        String input = cliHandler.getInput();
        return "cancel".equals(input) || "c".equals(input);
    }

    protected boolean hasUserEnteredBreak(final CliHandler<T> cliHandler) {
        String input = cliHandler.getInput();
        return "break".equals(input) || "bk".equals(input);
    }

    protected boolean hasUserEnteredShort(final CliHandler<T> cliHandler) {
        String input = cliHandler.getInput();
        return "short".equals(input) || "st".equals(input);
    }

    protected boolean hasUserEnteredDamage(final CliHandler<T> cliHandler) {
        String input = cliHandler.getInput();
        return "damage".equals(input) || "dmg".equals(input);
    }

    protected boolean hasUserEnteredCdl(final CliHandler<T> cliHandler) {
        return "cdl".equals(cliHandler.getInput()) || "chgdeli".equals(cliHandler.getInput());
    }

    protected boolean hasUserEnteredChangeFloor(final CliHandler<T> cliHandler) {
        String input = cliHandler.getInput();
        return "chgfloor".equals(input) || "chgf".equals(input);
    }
    
    protected boolean hasUserEnteredEnd(final CliHandler<T> cliHandler) {
        String input = cliHandler.getInput();
        return "end".equals(input);
    }
    
    protected boolean isAborted(final CliHandler<T> cliHandler) {
        boolean result = cliHandler.isAbort();
        cliHandler.setAbort(false);
        return result;
    }
}
