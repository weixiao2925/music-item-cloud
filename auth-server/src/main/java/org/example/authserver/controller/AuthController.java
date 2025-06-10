package org.example.authserver.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authserver.entity.vo.response.AuthorizeVO;
import org.example.authserver.service.JwtTokenService;
import org.example.authserver.utils.CookieUtil;
import org.example.authserver.utils.JwtUtil;
import org.example.commoncore.constants.Const;
import org.example.commoncore.entity.RestBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final JwtTokenService jwtTokenService;
    private final StringRedisTemplate stringRedisTemplate;

    @PostMapping("/refreshVerify")
    public RestBean<AuthorizeVO> refresh(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = cookieUtil.getRefreshTokenCookie(request);
        if (sessionId == null || sessionId.isEmpty()) return RestBean.unauthorized("身份已过期，请重新登录");

        System.out.println("获取到的sessionId: " + sessionId.substring(0, 10) + "...");

        String refreshToken = stringRedisTemplate.opsForValue().get(Const.SESSION_REFRESH + sessionId);
        // TODO 这里redis读取有问题
        System.out.println("redis: "+refreshToken);
        DecodedJWT jwt = jwtUtil.parseToken(refreshToken);
        boolean remember = jwt.getClaim("remember").asBoolean();

        AuthorizeVO vo = jwtTokenService.refreshToken(sessionId, response);
        log.warn("refresh vo: {}", vo);
        if (vo == null) return RestBean.unauthorized("身份已过期，请重新登录");
        vo.setRememberMe(remember);
        return RestBean.success(vo);
    }

    @GetMapping("/test")
    public RestBean<String> test(HttpServletRequest request) {
        return RestBean.success("测试成功，当前用户："+request.getHeader(Const.ATTR_USERNAME));
    }

}
