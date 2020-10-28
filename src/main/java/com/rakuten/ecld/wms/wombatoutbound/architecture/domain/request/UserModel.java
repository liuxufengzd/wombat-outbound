package com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Setter
@Getter
@Builder
@ToString
public class UserModel {

    private String userCode;
    private String userName;
    private String roleCode;
    private boolean admin;
    private String business;
    private Set<String> center;
}
