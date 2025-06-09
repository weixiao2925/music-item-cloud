package org.example.authserver.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.authserver.entity.dto.Account;
import org.example.authserver.entity.vo.response.AuthorizeVO;
import org.example.authserver.service.AccountService;
import org.example.authserver.service.JwtTokenService;
import org.example.authserver.utils.CookieUtil;
import org.example.authserver.utils.JwtUtil;
import org.example.commoncore.constants.Const;
import org.example.commoncore.entity.RestBean;
import org.example.commoncore.utils.DigestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AccountService accountService;
    private final JwtTokenService jwtTokenService;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers(
                                "/api/auth/login"
                        ).permitAll()   // 不拦截这些
                        .anyRequest().authenticated()  // 其它都需要认证
                )
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onAuthenticationFailure)
                )
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                //没有登录或不够权限
                .exceptionHandling(conf->conf
                        .authenticationEntryPoint(this::onUnauthorized)
                        .accessDeniedHandler(this::onAccessDeny)
                )
                //关闭csrf
                .csrf(AbstractHttpConfigurer::disable)
                //会话管理
                .sessionManagement(conf->conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//关闭通过session来验证（无状态验证）
                )
                .build();
    }

    //登录验证成功了，但是没有权限处理
    private void onAccessDeny(HttpServletRequest request,
                              HttpServletResponse response,
                              AccessDeniedException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");//返回值的字符编码
        response.getWriter().write(RestBean.forbidden(exception.getMessage()).asJsonString());
    }


    //没有登陆时的处理
    public void onUnauthorized(HttpServletRequest request,
                               HttpServletResponse response,
                               AuthenticationException exception)throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");//返回值的字符编码
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());

    }

    //验证成功时的处理
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");//返回值的字符编码
        PrintWriter writer = response.getWriter();
        User user=(User) authentication.getPrincipal(); //获取登录的用户信息
        System.out.println(user.getUsername());
        Account account = accountService.findAccountByUsernameToEmail(user.getUsername());

        Boolean rememberMe = Boolean.parseBoolean(request.getParameter("remember"));
        request.setAttribute(Const.ATTR_USER_RENEGER, rememberMe);

        String sessionId = DigestUtils.generateSessionId((int) account.id());
        String accessToken = jwtTokenService.createAccessToken(user,(int) account.id(), account.username(), sessionId);
        jwtTokenService.createRefreshToken(user, (int) account.id(), account.username(), rememberMe, sessionId);

        // 设置新 Cookie
        cookieUtil.setRefreshTokenCookie(response, sessionId, rememberMe);
        AuthorizeVO vo = new AuthorizeVO(
                account.username(),
                account.role(),
                accessToken,
                rememberMe,
                jwtUtil.expireAccessTime()
        );
        writer.write(RestBean.success(vo).asJsonString());
    }

    //验证失败后的处理
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");//返回值的字符编码
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());
    }
    //登出时成功的处理
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");//返回值的字符编码
        PrintWriter writer=response.getWriter();
        String authorization=request.getHeader("Authorization");//获取Authorization请求头中的jwt信息
        // 1. accessToken 加入黑名单
        boolean invalidated = jwtUtil.invalidateJwt(authorization);

        // 2. 清除 Redis 中的 refreshToken
        String sessionId = cookieUtil.getRefreshTokenCookie(request);
        if (sessionId != null && !sessionId.isEmpty()) {
            String redisKey = Const.SESSION_REFRESH + sessionId;
            stringRedisTemplate.delete(redisKey);
        }

        // 3. 清除 Cookie 中的 refreshToken
        cookieUtil.clearRefreshTokenCookie(response);
        if (invalidated) {
            writer.write(RestBean.success("退出成功").asJsonString());
        } else {
            writer.write(RestBean.failure(400, "退出登录失败").asJsonString());
        }
    }

}
