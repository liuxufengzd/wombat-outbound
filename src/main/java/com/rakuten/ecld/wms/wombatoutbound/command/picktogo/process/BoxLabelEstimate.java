package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.service.command.picktogo.PtgDeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.rakuten.ecld.wms.wombatoutbound.constant.CommandConstant.BOX_LABEL_REGEX;

@Component
@Slf4j
@RequiredArgsConstructor
public class BoxLabelEstimate extends AbstractBaseStepHandler<PtgState> {
    private final PtgDeliveryService ptgDeliveryService;

    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> box label estimate");

        PtgState state = cliHandler.getState();
        String boxLabel = cliHandler.getInput();
        state.getItem().setBoxLabel(boxLabel);
        ptgDeliveryService.registerDelivery(state.getItem().getDeliveryCode(), boxLabel, state.getItem().getBoxArea());
    }

    @Override
    public boolean isInputValid(CliHandler<PtgState> cliHandler) {
        return !isInputEmpty(cliHandler) && matchPattern(cliHandler,BOX_LABEL_REGEX);
    }

    @Override
    public void invalidPostProcess(CliHandler<PtgState> cliHandler) {
        String boxArea = cliHandler.getState().getItem().getBoxArea();
        cliHandler.getState().getItem().setBoxArea(ptgDeliveryService.lastBoxArea(boxArea));
    }
}
