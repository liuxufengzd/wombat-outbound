package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.service.command.picktogo.PtgDeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.rakuten.ecld.wms.wombatoutbound.constant.CommandConstant.DELIVERY_CODE_REGEX;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegisterDeliveryEstimate extends AbstractBaseStepHandler<PtgState> {
    private final PtgDeliveryService ptgDeliveryService;

    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> register delivery estimate");

        List<String> deliveries = ptgDeliveryService.getUnregisteredDeliveries(cliHandler.getState().getBatch());
        if (!deliveries.contains(cliHandler.getInput())){
            cliHandler.fail(messageSourceUtil.getMessage("outbound.common.delivery_problem"));
            return;
        }
        cliHandler.getState().getItem().setDeliveryCode(cliHandler.getInput());
    }

    @Override
    public boolean isInputValid(CliHandler<PtgState> cliHandler) {
        return !isInputEmpty(cliHandler) && matchPattern(cliHandler,DELIVERY_CODE_REGEX);
    }
}
