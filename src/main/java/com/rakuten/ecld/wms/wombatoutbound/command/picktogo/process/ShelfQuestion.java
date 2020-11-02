package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.ResponseStyle;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShelfQuestion extends AbstractBaseStepHandler<PtgState> {
    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> shelf question");

        Item item = cliHandler.getState().getItem();
        cliHandler.response(item.getShelfCode(), ResponseStyle.HIGHLIGHT);
        cliHandler.response(item.getItemCode()+"(商品コード)");
        cliHandler.response(item.getName()+"(商品名)");
        cliHandler.response(item.getSource()+"(著者)");
        cliHandler.response("QTY "+item.getNumber());
        cliHandler.response(messageSourceUtil.getMessage("outbound.common.shelf.question"));
    }
}
