package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class QAEstimate extends AbstractBaseStepHandler<PtgState> {

    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> QA estimate");

        try {
            /**
             * call service layer (step split db)
             */
        }catch (BusinessException e){
            cliHandler.fail(messageSourceUtil.getMessage(e.getMessage()));
        }
    }

    @Override
    public boolean isInputValid(CliHandler<PtgState> cliHandler) {
        return true;
    }

    @Override
    public void invalidPostProcess(CliHandler<PtgState> cliHandler) {

    }
}
