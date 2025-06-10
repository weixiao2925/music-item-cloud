package org.example.commoncore.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

//用来注册账号
@Data
public class EmailRegisterVO {
    @Email
    String email;
    @Length(max = 6,min = 6)
    String code;
    @Length(min = 6)
    String password;
    String name="admin";
}
