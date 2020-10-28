package com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response;

import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.Action;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.InputFormat;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.InputType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CliParams {
    @Builder.Default
    private InputType inputType = InputType.NO_LIMIT;
    @Builder.Default
    private int min = 1;
    @Builder.Default
    private int max = 255;
    private Action[] actionKey;
    @Builder.Default
    private InputFormat format = InputFormat.NORMAL;
    @Builder.Default
    private boolean clearScreen = false;
    @Builder.Default
    private boolean changeCommand = false;
}
