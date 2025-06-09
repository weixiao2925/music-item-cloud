package org.example.authserver.service;

import org.example.authserver.entity.dto.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {
    Account findAccountByUsernameToEmail(String username);
}
