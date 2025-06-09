package org.example.authserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authserver.entity.dto.Account;
import org.example.authserver.repository.AccountRepository;
import org.example.authserver.service.AccountService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account findAccountByUsernameToEmail(String username) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account =accountRepository.findByUsernameOption(username).
                orElseThrow(()->new UsernameNotFoundException("账号或密码错误"));
//        System.out.println("账号信息：" + account);
        return User.withUsername(account.username())
                .password(account.password())
                .roles(account.role())
                .build();
    }
}
