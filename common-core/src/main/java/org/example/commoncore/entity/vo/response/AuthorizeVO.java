package org.example.commoncore.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AuthorizeVO {
    int id;
    String username;
    String name;
    String role;
    String token;
    Date expire;
}
