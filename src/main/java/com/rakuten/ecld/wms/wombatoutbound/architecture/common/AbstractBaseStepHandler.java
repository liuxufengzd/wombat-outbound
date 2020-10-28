package com.rakuten.ecld.wms.wombatoutbound.architecture.common;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.util.MessageSourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Slf4j
@Component
public abstract class AbstractBaseStepHandler<T> implements StepHandler<T> {
    protected MessageSourceUtil messageSourceUtil;

    @Autowired
    public void setMessageSourceUtil(MessageSourceUtil messageSourceUtil) {
        this.messageSourceUtil = messageSourceUtil;
    }

    @Override
    public void execute(CliHandler<T> cliHandler) {
        long startTime = System.currentTimeMillis();
        if (isInputValid(cliHandler))   {
            log.info("The step {} started at {}", this.getClass().getSimpleName(), startTime);
            process(cliHandler);
            log.info("Total time taken by step {} is {}ms", this.getClass().getSimpleName(), System.currentTimeMillis() - startTime);
        }else invalidPostProcess(cliHandler);
    }

    protected boolean isInputEmpty(CliHandler<T> cliHandler) {
        if (cliHandler.getInput().isBlank()) {
            cliHandler.fail(messageSourceUtil.getMessage("outbound.common.input.empty"));
            return true;
        }
        return false;
    }

    protected boolean matchPattern(CliHandler<T> cliHandler, String pattern) {
        if (!Pattern.matches(pattern, cliHandler.getInput())) {
            cliHandler.fail(messageSourceUtil.getMessage("outbound.common.input.invalid"));
            return false;
        }
        return true;
    }

    public abstract void process(CliHandler<T> cliHandler);

    public boolean isInputValid(CliHandler<T> cliHandler){
        return true;
    }

    public void invalidPostProcess(CliHandler<T> cliHandler){}
}
