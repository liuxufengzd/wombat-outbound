package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.exception.BusinessException;
import com.rakuten.ecld.wms.wombatoutbound.service.command.picktogo.PtgDeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.rakuten.ecld.wms.wombatoutbound.constant.CommandConstant.DELIVERY_CODE_REGEX;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryEstimate extends AbstractBaseStepHandler<PtgState> {
    private final PtgDeliveryService ptgDeliveryService;

    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> delivery estimate");

        try {
            ptgDeliveryService.batchEstimate(cliHandler);
        }catch (BusinessException e){
            failHandler(cliHandler,e);
        }
    }

    @Override
    public boolean isInputValid(CliHandler<PtgState> cliHandler) {
        return !isInputEmpty(cliHandler) && matchPattern(cliHandler,DELIVERY_CODE_REGEX);
    }
}
