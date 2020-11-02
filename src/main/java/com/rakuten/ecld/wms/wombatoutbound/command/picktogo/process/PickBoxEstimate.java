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
public class PickBoxEstimate extends AbstractBaseStepHandler<PtgState> {
    private final PtgDeliveryService ptgDeliveryService;

    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> pick box estimate");

        PtgState state = cliHandler.getState();
        String boxLabel = cliHandler.getInput();
        if (!boxLabel.equals(state.getItem().getBoxLabel())) {
            cliHandler.fail(messageSourceUtil.getMessage("outbound.common.box_label_problem"));
            return;
        }
        int pickNumber = state.getPickNumber();
        ptgDeliveryService.pickItem(state.getItem().getDeliveryCode(), state.getItem().getItemCode(), pickNumber);
        state.setPickedNumber(state.getPickedNumber()+pickNumber);

    }

    @Override
    public boolean isInputValid(CliHandler<PtgState> cliHandler) {
        return !isInputEmpty(cliHandler) && matchPattern(cliHandler, BOX_LABEL_REGEX);
    }
}
