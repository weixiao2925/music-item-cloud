package org.example.commoncore.entity.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ChangePasswordVO {
    @Length(min = 6,max = 20)
    private String password;
    @Length(min = 6,max = 20)
    private String newPassword;
}
