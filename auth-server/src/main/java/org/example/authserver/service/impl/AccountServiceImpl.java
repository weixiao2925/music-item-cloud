package org.example.authserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authserver.entity.dto.Account;
import org.example.authserver.repository.AccountRepository;
import org.example.authserver.service.AccountService;
import org.example.commoncore.entity.vo.request.ChangePasswordVO;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Account findAccountByUsernameToEmail(String username) {
        return accountRepository.findByUsernameEntity(username);
    }

    @Override
    public String changePassword(int id, ChangePasswordVO vo) {
        String password = accountRepository.getPasswordById(id);

        if (!passwordEncoder.matches(vo.getPassword(),  password))
            return "原密码错误，请重新输入";

        return accountRepository.changePassword(id, vo)
                ? null
                : "未知错误， 请联系管理员";

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
