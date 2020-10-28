package com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.RfcBusiness;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class JWTUserDetail {

    private Token token;
    private String userCode;
    private String userName;
    private String roleCode;
    private boolean admin;
    private String business;
    private Set<String> center;

    @JsonIgnore
    public Set<String> getRoles() {

        if (StringUtils.isBlank(roleCode)) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(roleCode.split(",")));
    }

    @JsonIgnore
    public boolean containsRole(String role) {

        return getRoles().contains(role);
    }

    @JsonIgnore
    public RfcBusiness getRfcBusiness() {

        return RfcBusiness.getEnumByName(business);
    }
}
