package org.example.commoncore.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenCheckResult {
    private int code;
    private int userId;
    private String username;
}
