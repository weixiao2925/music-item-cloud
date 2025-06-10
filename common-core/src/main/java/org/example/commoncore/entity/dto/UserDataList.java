package org.example.commoncore.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

/**
 * 用于用户管理页的表格数据
 */
@Data
@AllArgsConstructor
public class UserDataList {
    int id;
    String name;
    String sex;
    String email;
    Date birth_date;
    String signature;
    String region;
    String avatarUrl;
    Date registrationTime;
}
