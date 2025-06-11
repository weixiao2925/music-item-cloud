package org.example.authserver.service;

import org.example.authserver.entity.dto.Account;
import org.example.commoncore.entity.vo.request.ChangePasswordVO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {
    Account findAccountByUsernameToEmail(String username);
    //重置密码
    String changePassword(int id, ChangePasswordVO vo);
}
