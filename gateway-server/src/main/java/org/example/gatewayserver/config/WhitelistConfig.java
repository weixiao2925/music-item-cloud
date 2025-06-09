package org.example.gatewayserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "my.gateway")
public class WhitelistConfig {
    private List<String> whitelist;
}
