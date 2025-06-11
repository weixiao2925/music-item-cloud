package org.example.commoncore.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoVO {
    private int userId;
    private String username;
    private String name;
    private String sex;
    private String birthDate;
    private String region;
    private String signature;
    private String avatarPath;
    private String role;
    private Date registrationTime;
}
