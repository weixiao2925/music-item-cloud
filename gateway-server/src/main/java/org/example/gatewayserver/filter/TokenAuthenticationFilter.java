package org.example.gatewayserver.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.commoncore.constants.Const;
import org.example.commoncore.entity.RestBean;
import org.example.commoncore.entity.vo.TokenCheckResult;
import org.example.gatewayserver.config.WhitelistConfig;
import org.example.gatewayserver.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final WhitelistConfig whitelistConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        System.out.println("拦截请求: " + exchange.getRequest().getURI());
        String path = exchange.getRequest().getURI().getPath();
        // 白名单接口直接放行
        if (whitelistConfig.getWhitelist().stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }
//        System.out.println("啊~被拦截了: " + path);


        String sessionId = extractSessionId(exchange);
        if (sessionId == null) { // 没有这个证明没有登录
            log.warn("未获取到 sessionId, 已拦截");
            return writeJsonResponse(exchange,
                    RestBean.unauthorized("未登录，请跳转授权登录"));
        }

        // 提取token
        String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");
        String accessToken = jwtUtil.convertToken(authorization);

        // 验证合法性
        TokenCheckResult tokenCheckResult = jwtUtil.checkToken(accessToken);
        int code = tokenCheckResult.getCode();

//        System.out.println("code : " + code);

        if (code == 460) {
            log.warn("Token 已过期，返回460，让前端尝试续签");
            return writeJsonResponse(exchange, RestBean.tokenExpired(""));
        }else if (code == 401) {
            log.warn("Token 不合法，已拦截");
            return writeJsonResponse(exchange, RestBean.unauthorized("Token 不合法"));
        }



        // 通过 Token 校验，向下传递用户信息
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(builder -> builder
                        .header(Const.ATTR_USER_ID, String.valueOf(tokenCheckResult.getUserId()))
                        .header(Const.ATTR_USERNAME, tokenCheckResult.getUsername())
                ).build();

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }

    private String extractSessionId(ServerWebExchange exchange) {
        return exchange.getRequest()
                .getCookies()
                .getFirst(Const.SESSION_ID) != null
                ? exchange.getRequest().getCookies().getFirst(Const.SESSION_ID).getValue()
                : null;
    }

    private Mono<Void> writeJsonResponse(ServerWebExchange exchange, Object body) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            String json = new ObjectMapper().writeValueAsString(body);
            DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("JSON 序列化失败", e);
            return response.setComplete(); // 兜底返回
        }
    }

}
