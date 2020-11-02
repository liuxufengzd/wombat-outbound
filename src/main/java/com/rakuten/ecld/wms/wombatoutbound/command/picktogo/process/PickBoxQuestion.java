package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.ResponseStyle;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PickBoxQuestion extends AbstractBaseStepHandler<PtgState> {

    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> pick box question");

        String boxArea = cliHandler.getState().getItem().getBoxArea();
        String boxLabel = cliHandler.getState().getItem().getBoxLabel();
        boxLabel = boxLabel.substring(boxLabel.length()-3);
        cliHandler.response(messageSourceUtil.getMessage("outbound.command.ptg.box_area",new String[]{boxArea}), ResponseStyle.HIGHLIGHT);
        cliHandler.response(messageSourceUtil.getMessage("outbound.command.ptg.box_label_last_three",new String[]{boxLabel}),ResponseStyle.HIGHLIGHT);
        cliHandler.response(messageSourceUtil.getMessage("outbound.common.box_label_question"));
    }
}
