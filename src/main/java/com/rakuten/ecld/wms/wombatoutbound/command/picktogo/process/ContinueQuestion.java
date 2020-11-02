package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContinueQuestion extends AbstractBaseStepHandler<PtgState> {

    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        cliHandler.response(messageSourceUtil.getMessage("outbound.command.ptg.continue_ptg"));
    }
}