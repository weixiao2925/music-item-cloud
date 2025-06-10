package org.example.authserver.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authserver.entity.dto.Account;
import org.example.authserver.entity.vo.response.AuthorizeVO;
import org.example.authserver.service.AccountService;
import org.example.authserver.utils.CookieUtil;
import org.example.commoncore.constants.Const;
import org.example.commoncore.entity.vo.TokenCheckResult;
import org.example.commoncore.exception.TokenExpiredException;
import org.example.commoncore.exception.TokenInvalidException;
import org.example.commoncore.utils.DigestUtils;
import org.example.authserver.entity.dto.JwtDetail;
import org.example.authserver.service.JwtTokenService;
import org.example.authserver.utils.JwtUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final AccountService accountService;
    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public String createAccessToken(UserDetails details, int id, String username, String sessionId) {
        return jwtUtil.createJwt(
                details, id, username, jwtUtil.expireAccessTime());
    }

    @Override
    public String createRefreshToken(UserDetails details, int id, String username, Boolean rememberMe, String sessionId) {
        String refreshToken = jwtUtil.createJwt(details, id, username, jwtUtil.expireRefreshTime(rememberMe), rememberMe).trim();
//        System.out.println("生成的refreshToken: " + refreshToken);
//        System.out.println("refreshToken 类型: " + refreshToken.getClass().getName());
//        System.out.println("refreshToken 字节: " + Arrays.toString(refreshToken.getBytes(StandardCharsets.UTF_8)));

        stringRedisTemplate.opsForValue().set(
                Const.SESSION_REFRESH + sessionId,
                refreshToken,
                jwtUtil.expireRefreshSeconds(rememberMe),
                TimeUnit.SECONDS  // ✅ 推荐明确使用秒
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

    @Override
    public AuthorizeVO refreshToken(String sessionId, HttpServletResponse response) {
        String key = Const.SESSION_REFRESH + sessionId;
        String storedRefreshToken = stringRedisTemplate.opsForValue().get(key);

        if (storedRefreshToken == null || storedRefreshToken.isEmpty()) return null;
        TokenCheckResult tokenCheckResult = checkToken(storedRefreshToken);
        if (tokenCheckResult.getCode() != 200) return null;

        JwtDetail jwtDetail = jwtUtil.toJwtDetail(storedRefreshToken, Const.SESSION_REFRESH);
        String newSessionId = DigestUtils.generateSessionId(tokenCheckResult.getUserId());

        String username = jwtDetail.getUsername();
        Account account = accountService.findAccountByUsernameToEmail(username);

        if (account == null) return null;

        String accessToken = createAccessToken(
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

        // 清除旧的 Cookie
        cookieUtil.clearRefreshTokenCookie(response);

        // 设置新 Cookie
        cookieUtil.setRefreshTokenCookie(response, newSessionId, jwtDetail.isRememberMe());

        return new AuthorizeVO(
                account.username(),
                account.role(),
                accessToken,
                jwtDetail.isRememberMe(),
                jwtUtil.expireAccessTime()
        );
    }
}
