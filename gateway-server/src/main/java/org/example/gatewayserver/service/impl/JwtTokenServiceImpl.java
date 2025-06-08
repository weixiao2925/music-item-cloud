package org.example.gatewayserver.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.commoncore.constants.Const;
import org.example.commoncore.entity.vo.TokenCheckResult;
import org.example.commoncore.exception.TokenExpiredException;
import org.example.commoncore.exception.TokenInvalidException;
import org.example.commoncore.utils.DigestUtils;
import org.example.gatewayserver.entity.dto.JwtDetail;
import org.example.gatewayserver.entity.dto.RefreshResult;
import org.example.gatewayserver.service.JwtTokenService;
import org.example.gatewayserver.utils.JwtUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public String createAccessToken(UserDetails details, int id, String username, String sessionId) {
        return jwtUtil.createJwt(
                details, id, username, jwtUtil.expireAccessTime());
    }

    @Override
    public String createRefreshToken(UserDetails details, int id, String username, Boolean rememberMe, String sessionId) {
        String refreshToken = jwtUtil.createJwt(details, id, username, jwtUtil.expireRefreshTime(rememberMe), rememberMe);
        stringRedisTemplate.opsForValue().set(
                Const.SESSION_REFRESH + sessionId,
                refreshToken,
                jwtUtil.expireRefreshSeconds(rememberMe)
        );
        return refreshToken;
    }

    @Override
    public TokenCheckResult checkToken(String token) {
        TokenCheckResult result = new TokenCheckResult();
        try {
            DecodedJWT jwt = jwtUtil.parseToken(token);
            // 成功解析，可继续业务逻辑
            result.setCode(200);
            result.setUserId(jwtUtil.toId(jwt));
            result.setUsername(jwtUtil.toUsername(jwt));
            return result;
        } catch (TokenExpiredException e) {
            result.setCode(460);
            return result;
        } catch (TokenInvalidException e) {
            result.setCode(401);
            return result;
        }
    }

    public RefreshResult refreshToken(String sessionId) {
        String key = Const.SESSION_REFRESH + sessionId;
        String storedRefreshToken = stringRedisTemplate.opsForValue().get(key);

        if (storedRefreshToken == null || storedRefreshToken.isEmpty())
            return new RefreshResult(null, false, false, "已失效，请重新登录");

        TokenCheckResult tokenCheckResult = checkToken(storedRefreshToken);
        if (tokenCheckResult.getCode() != 200)
            return new RefreshResult(null, false, false, "已失效，请重新登录");

        JwtDetail jwtDetail = jwtUtil.toJwtDetail(storedRefreshToken, Const.SESSION_REFRESH);
        String newSessionId = DigestUtils.generateSessionId(tokenCheckResult.getUserId());

        createAccessToken(
                jwtDetail.getUserDetails(),
                tokenCheckResult.getUserId(),
                tokenCheckResult.getUsername(),
                newSessionId
        );
        createRefreshToken(
                jwtDetail.getUserDetails(),
                tokenCheckResult.getUserId(),
                tokenCheckResult.getUsername(),
                jwtDetail.isRememberMe(),
                newSessionId
        );

        stringRedisTemplate.delete(Const.SESSION_REFRESH + sessionId);

        return new RefreshResult(newSessionId, jwtDetail.isRememberMe(), true, null);
    }
}
