package org.example.authserver.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.commoncore.constants.Const;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieUtil {

    @Value("${cookie.secure}")
    private boolean secure;

    private final JwtUtil jwtUtil;

    /**
     * 从请求中获取刷新令牌Cookie
     */
    public String getRefreshTokenCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Const.SESSION_ID.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 设置刷新令牌Cookie
     */
    public void setRefreshTokenCookie(HttpServletResponse response, String newSessionId, boolean rememberMe) {
        Cookie cookie = new Cookie(Const.SESSION_ID, newSessionId);
        cookie.setHttpOnly(true);
        cookie.setSecure(secure); // 仅在HTTPS下传输
        cookie.setPath("/");
        cookie.setMaxAge(rememberMe ? (int) jwtUtil.expireRefreshSeconds(true) : -1);
        response.addCookie(cookie);
        log.info("设置刷新令牌Cookie, rememberMe: {}, maxAge: {}", rememberMe,
                 rememberMe ? (int) jwtUtil.expireRefreshSeconds(true) : -1);
    }

    /**
     * 清除刷新令牌Cookie
     */
    public void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(Const.SESSION_ID, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 立即过期
        response.addCookie(cookie);
    }
}
