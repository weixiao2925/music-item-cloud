package org.example.gatewayserver.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.commoncore.constants.Const;
import org.example.commoncore.entity.vo.TokenCheckResult;
import org.example.commoncore.exception.TokenInvalidException;
import org.example.gatewayserver.entity.vo.JwtDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.example.gatewayserver.entity.vo.CustomUserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${spring.security.jwt.key}")
    private String key;

    private final StringRedisTemplate stringRedisTemplate;

    // 验证token的有效性，并返回用户信息
    public TokenCheckResult checkToken(String token) {
        TokenCheckResult result = new TokenCheckResult();
        try {
            DecodedJWT jwt = this.parseToken(token);
            // 成功解析，可继续业务逻辑
            result.setCode(200);
            result.setUserId(this.toId(jwt));
            result.setUsername(this.toUsername(jwt));
            return result;
        } catch (org.example.commoncore.exception.TokenExpiredException e) {
            result.setCode(460);
            return result;
        } catch (TokenInvalidException e) {
            result.setCode(401);
            return result;
        }
    }

    // 解析jwt获取用户ID
    public Integer toId(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }
    // 解析jwt获取用户名
    public String toUsername(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("username").asString();
    }
    // 解析jwt用户信息
    public CustomUserDetails toUser(DecodedJWT jwt){
        Map<String, Claim> claims = jwt.getClaims();
        // 使用静态工厂方法
        return CustomUserDetails.create(
                claims.get("username").asString(),
                "******",
                claims.get("authorities").asArray(String.class)
        );
    }
    // 确认有效后的解析方法
    public JwtDetail toJwtDetail(String token, String type) {
        try {
            DecodedJWT jwt = parseToken(token);
            Integer id = toId(jwt);
            String username = toUsername(jwt);
            CustomUserDetails userDetails = toUser(jwt);
            if (type.equals(Const.SESSION_REFRESH)) {
                boolean remember = jwt.getClaims().get("remember").asBoolean();
                return new JwtDetail(userDetails, id, username, remember);
            }else {
                return new JwtDetail(userDetails, id, username, false);
            }
        } catch (Exception e) {
            return null;
        }
    }

    //查看请求头是否携带token,并返回token
    public String convertToken(String headerToken){
        if (headerToken == null || !headerToken.startsWith("Bearer ")){
            return null;
        }
        return headerToken.substring(7);
    }

    // 验证合法性，并将其加入黑名单
    public boolean invalidateJwt(String headerToken) {
        String token = convertToken(headerToken);
        if (token == null) return false;

        Algorithm algorithm=Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier=JWT.require(algorithm).build();

        try{
            DecodedJWT jwt=jwtVerifier.verify(token);
            String id=jwt.getId();
            return deleteToken(id,jwt.getExpiresAt());
        }catch (JWTVerificationException e){
            return false;
        }
    }

    // 看 token 是否有效
    private DecodedJWT parseToken(String token) {
        if (token == null || token.isEmpty()) {
            log.warn("尝试解析空 token");
            throw new TokenInvalidException("Token 为空");
        }

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(key)).build();
            DecodedJWT jwt = verifier.verify(token);

            // 判断是否在黑名单
            if (this.isInvalidToken(jwt.getId())) {
                log.warn("Token在黑名单中: {}", jwt.getId());
                throw new TokenInvalidException("Token 非法");
            }

            return jwt;

        } catch (TokenExpiredException e) {
            log.warn("Token 已过期: {}", e.getMessage());
            throw new TokenExpiredException("Token 过期", e.getExpiredOn());
        } catch (JWTVerificationException e) {
            log.warn("Token 非法: {}", e.getMessage());
            throw new TokenInvalidException("Token 非法");
        }
    }

    // 判断令牌是否在黑名单
    private boolean isInvalidToken (String tokenId) {
        boolean invalid = stringRedisTemplate.hasKey(Const.JWT_BLACK_LIST + tokenId);
        if (invalid) log.info("Token {} 在黑名单中", tokenId);
        return  invalid;
    }

    // 将令牌加入redis黑名单
    private boolean deleteToken(String tokenId, Date expireAt) {
        if (tokenId == null || tokenId.isEmpty()) return false;
        if (this.isInvalidToken(tokenId)) return false;

        Date now=new Date();
        long expire=Math.max(expireAt.getTime()-now.getTime(),0);
        stringRedisTemplate.opsForValue().set(Const.JWT_BLACK_LIST+tokenId, "", expire, TimeUnit.MICROSECONDS);
        return true;
    }

}
