package com.rakuten.ecld.wms.wombatoutbound.architecture.common;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.util.MessageSourceUtil;
import com.rakuten.ecld.wms.wombatoutbound.exception.BusinessException;
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
            cliHandler.fail("入力してください");
            return true;
        }
        return false;
    }

    protected void failHandler(CliHandler<T> cliHandler, BusinessException e){
        String message = messageSourceUtil.getMessage(e.getMessage());
        if ("".equals(message))
            cliHandler.fail(e.getMessage());
        else cliHandler.fail(message);
    }

    public abstract void process(CliHandler<T> cliHandler);

    // Optional to override if the input data has to be verified
    public boolean isInputValid(CliHandler<T> cliHandler){
        return true;
    }

    // Optional to override if some post process has to be run when the input data is invalid
    public void invalidPostProcess(CliHandler<T> cliHandler){}

    protected boolean matchPattern(CliHandler<T> cliHandler, String pattern) {
        if (!Pattern.matches(pattern, cliHandler.getInput())) {
            cliHandler.fail(messageSourceUtil.getMessage("outbound.common.input.invalid"));
            return false;
        }
        return true;
    }
}
