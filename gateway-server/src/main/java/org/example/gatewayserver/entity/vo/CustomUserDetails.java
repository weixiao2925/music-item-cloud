package org.example.gatewayserver.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails {
    private String username;
    private String password;
    private Collection<String> authorities;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    // 便捷构造器
    public CustomUserDetails(String username, String password, String[] authorities) {
        this.username = username;
        this.password = password;
        this.authorities = Arrays.asList(authorities);
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    // 静态工厂方法
    public static CustomUserDetails create(String username, String password, String[] authorities) {
        return new CustomUserDetails(username, password, authorities);
    }
}
