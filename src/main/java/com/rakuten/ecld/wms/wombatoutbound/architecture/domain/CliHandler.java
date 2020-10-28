package com.rakuten.ecld.wms.wombatoutbound.architecture.domain;

import com.rakuten.ecld.wms.wombatoutbound.architecture.core.ModelRunner;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response.Activity;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response.CliParams;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response.ResponseEntry;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.ResponseStyle;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.Sound;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
public class CliHandler<T> extends CliData<T> {
    private Activity logActivity;
    private Sound soundId;
    private final List<ResponseEntry> response = new ArrayList<>();
    private boolean failed;
    private CliParams cliParams;
    private ModelRunner modelRunner;

    public void response(ResponseEntry responseEntry) {
        this.response.add(responseEntry);
    }

    public void response(String response) {
        this.response.add(new ResponseEntry(response));
    }

    public void response(String response, ResponseStyle style) {
        this.response.add(new ResponseEntry(response, style));
    }

    public void setModelRunner(ModelRunner modelRunner) {
        this.modelRunner = modelRunner;
    }

    public void setCliParams(CliParams cliParams) {
        this.cliParams = cliParams;
    }

    public void fail() {
        this.failed = true;
        this.soundId = Sound.SOUND_ERROR;
    }

    public void fail(ResponseEntry responseEntry) {
        this.fail();
        response(responseEntry);
    }

    public void fail(String response) {
        this.fail();
        response(response, ResponseStyle.ERROR);
    }

    public void fail(String response, ResponseStyle style) {
        this.fail();
        response(response, style);
    }

    public void sound(Sound soundType) {
        this.soundId = soundType;
    }

    public void logActivity(String type, long count) {
        logActivity = Activity.builder().type(type).count(count).build();
    }
}
