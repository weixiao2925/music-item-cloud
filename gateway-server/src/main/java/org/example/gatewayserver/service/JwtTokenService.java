package org.example.gatewayserver.service;

import org.example.commoncore.entity.vo.TokenCheckResult;
import org.example.gatewayserver.entity.dto.RefreshResult;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenService {
    String createAccessToken(UserDetails details, int id, String username, String sessionId);
    String createRefreshToken(UserDetails details, int id, String username, Boolean rememberMe, String sessionId);
    TokenCheckResult checkToken(String token);
    RefreshResult refreshToken(String sessionId);
}
