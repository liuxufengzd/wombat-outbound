package com.rakuten.ecld.wms.wombatoutbound.architecture.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request.JWTUserDetail;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request.Token;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request.UserModel;
import com.rakuten.ecld.wms.wombatoutbound.architecture.exception.CommandExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public final class JWTTokenUtil {

    private static final String CLAIM_USER_CODE = "userCode";
    private static final String CLAIM_USER_NAME = "userName";
    private static final String CLAIM_ROLE_CODE = "roleCode";
    private static final String CLAIM_IS_ADMIN = "isAdmin";
    private static final String CLAIM_BUSINESS = "business";
    private static final String CLAIM_CENTER = "cen";
    @Value("${jwt.expiration}")
    private Integer expiration;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.header}")
    private String header;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    public Token generateToken(final UserModel userModel) {
        return Token.builder()
            .accessToken(generateAccessToken(userModel))
            .expiresIn(getExpirationDate(expiration))
            .build();
    }

    public String getUserNameFromToken(final String token) {

        return JWT.decode(token).getClaim(CLAIM_USER_NAME).asString();
    }

    public String getUserCodeFromToken(final String token) {

        return JWT.decode(token).getClaim(CLAIM_USER_CODE).asString();
    }

    private String getRoleCodeFromToken(final String token) {

        return JWT.decode(token).getClaim(CLAIM_ROLE_CODE).asString();
    }

    private String generateAccessToken(final UserModel user) {

        log.info("Generate Token Parameters : userCode = {}", user.getUserCode());
        log.info("Generate Token Parameters : userName = {}", user.getUserName());
        log.info("Generate Token Parameters : roleCode = {}", user.getRoleCode());
        log.info("Generate Token Parameters : admin = {}", user.isAdmin());
        log.info("Generate Token Parameters : business = {}", user.getBusiness());
        String centerParams = null;
        if (user.getCenter() != null) {
            centerParams = String.join(",", user.getCenter());
        }
        log.info("Token Generate Parameters : center = {}", centerParams);
        String accessToken = JWT.create()
            .withClaim(CLAIM_USER_CODE, user.getUserCode())
            .withClaim(CLAIM_USER_NAME, user.getUserName())
            .withClaim(CLAIM_ROLE_CODE, user.getRoleCode())
            .withClaim(CLAIM_IS_ADMIN, user.isAdmin())
            .withClaim(CLAIM_CENTER,
                CommonUtil.returnEmptyStreamIfNullOrEmpty(user.getCenter()).collect(Collectors.joining(",")))
            .withClaim(CLAIM_BUSINESS, user.getBusiness())
            .withExpiresAt(getExpirationDate(expiration))
            .sign(getAlgorithm(secret));
        log.info("Generated Token = {}", accessToken);
        return accessToken;
    }

    private Date getExpirationDate(final Integer expiration) {

        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(expiration);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    private Algorithm getAlgorithm(final String secret) {

        return Algorithm.HMAC256(secret.getBytes());
    }

    public JWTUserDetail getUserFromToken(final String token) {

        log.info("auth token to get user:{}", token);
        final Map<String, Claim> claims = JWT.decode(token).getClaims();
        final Claim userCode = claims.get(CLAIM_USER_CODE);
        final Claim userName = claims.get(CLAIM_USER_NAME);
        final Claim roleCode = claims.get(CLAIM_ROLE_CODE);
        final Claim isAdmin = claims.get(CLAIM_IS_ADMIN);
        final Claim center = claims.get(CLAIM_CENTER);
        final Claim business = claims.get(CLAIM_BUSINESS);
        return JWTUserDetail.builder()
            .userCode(null == userCode ? null : userCode.asString())
            .userName(null == userName ? null : userName.asString())
            .roleCode(null == roleCode ? null : roleCode.asString())
            .admin(null == isAdmin ? false : isAdmin.asBoolean())
            .center(getCenters(center))
            .business(null == business ? null : business.asString())
            .build();
    }

    private Set<String> getCenters(final Claim center) {

        if (null == center) {
            return Collections.emptySet();
        }
        String centerStr = center.asString();
        if (StringUtils.isBlank(centerStr)) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(centerStr.split(",")));
    }

    public boolean validateAccessToken(final String token, final JWTUserDetail userDetails) {

        final JWTVerifier verifier = JWT.require(getAlgorithm(secret)).build();
        verifier.verify(token);
        final String userCode = getUserCodeFromToken(token);
        final String username = getUserNameFromToken(token);
        final String roleCode = getRoleCodeFromToken(token);
        return userDetails.getUserCode().equals(userCode)
            && userDetails.getUserName().equals(username)
            && userDetails.getRoleCode().equals(roleCode);
    }

    public String getAuthTokenFromRequest(final HttpServletRequest request) {

        return getAuthKey(request.getHeader(header));
    }

    public String getAuthKey(final String authHeader) {

        log.debug("authHeader in get auth key: {}, {}", authHeader, tokenHeader);
        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith(tokenHeader)) {
            return authHeader.substring(tokenHeader.length());
        }
        return null;
    }

    public String getToken(Map<String, String> headers) {
        Map<String, String> newHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        newHeaders.putAll(headers);
        String tokenHeader = newHeaders.get(header);
        if (tokenHeader == null)
            throw new CommandExecutionException("Can't get token header from " + header);
        return this.getAuthKey(tokenHeader);
    }
}
