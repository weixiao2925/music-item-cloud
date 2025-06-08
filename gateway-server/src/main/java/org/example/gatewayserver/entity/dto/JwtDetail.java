package org.example.gatewayserver.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtDetail {
    private UserDetails userDetails;
    private int id;
    private String username;
    private boolean rememberMe;
}
