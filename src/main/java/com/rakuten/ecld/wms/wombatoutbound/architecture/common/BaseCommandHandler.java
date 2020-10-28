package com.rakuten.ecld.wms.wombatoutbound.architecture.common;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;

import static com.rakuten.ecld.wms.wombatoutbound.constant.CommandConstant.ROLE_VETERAN;

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

    protected boolean isVeteran(final CliHandler<T> cliHandler) {
        return cliHandler.getUser().getRoles().contains(ROLE_VETERAN);
    }
}
