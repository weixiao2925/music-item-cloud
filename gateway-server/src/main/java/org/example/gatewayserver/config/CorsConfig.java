package org.example.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 允许携带 Cookie
        // 指定允许跨域的前端地址（开发时可用 *，生产建议写死域名）
        config.addAllowedOrigin("https://localhost:5174");
        // config.addAllowedOriginPattern("*"); // Spring Cloud Gateway 4.x 推荐用这个支持通配
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");     // 允许所有方法 GET POST PUT DELETE 等

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 全局所有路由生效

        return new CorsWebFilter(source);
    }
}
