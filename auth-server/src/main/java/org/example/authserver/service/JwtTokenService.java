package org.example.authserver.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.authserver.entity.vo.response.AuthorizeVO;
import org.example.commoncore.entity.vo.TokenCheckResult;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenService {
    String createAccessToken(UserDetails details, int id, String username, String sessionId);
    String createRefreshToken(UserDetails details, int id, String username, Boolean rememberMe, String sessionId);
    TokenCheckResult checkToken(String token);
    AuthorizeVO refreshToken(String sessionId, HttpServletResponse response);
}
