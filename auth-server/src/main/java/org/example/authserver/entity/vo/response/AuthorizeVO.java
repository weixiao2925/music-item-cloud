package org.example.authserver.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizeVO {
    private String username;
    private String role;
    private String token;
    private Boolean rememberMe;
    private Date expire;
}
