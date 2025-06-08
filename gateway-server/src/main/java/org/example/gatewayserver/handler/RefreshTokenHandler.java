package org.example.gatewayserver.handler;


import lombok.RequiredArgsConstructor;
import org.example.commoncore.constants.Const;
import org.example.commoncore.entity.RestBean;
import org.example.gatewayserver.entity.dto.RefreshResult;
import org.example.gatewayserver.service.JwtTokenService;
import org.example.gatewayserver.utils.JwtUtil;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RefreshTokenHandler {

    private final JwtTokenService jwtTokenService;
    private final JwtUtil jwtUtil;

    public Mono<ServerResponse> refreshToken(ServerRequest request) {
        // 1. 获取 sessionId (比如从 Cookie)
        String sessionId = String.valueOf(request.cookies().getFirst(Const.SESSION_ID));

        RefreshResult result = jwtTokenService.refreshToken(sessionId);

        if (!result.success()) {
            return ServerResponse.status(401)
                    .bodyValue(RestBean.failure(401, result.errorMsg()));
        }

        // 2. 删除旧Cookie（设置 MaxAge=0），并设置新 Cookie
        ResponseCookie deleteOld = ResponseCookie.from(Const.SESSION_ID, "")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie newCookie = ResponseCookie.from(Const.SESSION_ID, result.newSessionId())
                .httpOnly(true)
                .path("/")
                .maxAge(result.rememberMe() ? jwtUtil.expireRefreshSeconds(true) : -1)
                .build();

        return ServerResponse.ok()
                .cookie(deleteOld)
                .cookie(newCookie)
                .bodyValue(RestBean.success());
    }
}
