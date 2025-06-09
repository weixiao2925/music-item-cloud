package org.example.gatewayserver.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtDetail {
    private CustomUserDetails userDetails;
    private int id;
    private String username;
    private boolean rememberMe;
}
