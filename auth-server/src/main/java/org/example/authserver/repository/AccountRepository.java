package org.example.authserver.repository;


import lombok.RequiredArgsConstructor;
import org.babyfish.jimmer.sql.JSqlClient;

import org.example.authserver.entity.dto.Account;
import org.example.authserver.entity.dto.AccountTable;
import org.example.commoncore.entity.vo.request.ChangePasswordVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountRepository {

    private final JSqlClient sqlClient;
    private static final AccountTable tabel = AccountTable.$;
    private final PasswordEncoder passwordEncoder;

    public Optional<Account> findByUsernameOption(String username) {
        return  sqlClient.createQuery(tabel)
                .where(tabel.username().eq(username))
                .select(tabel)
                .fetchOptional();
    }

    public Account findByUsernameEntity(String username) {
        return  sqlClient.createQuery(tabel)
                .where(tabel.username().eq(username))
                .select(tabel)
                .fetchOne();
    }

    public String getPasswordById(Integer id) {
        return sqlClient
                .createQuery(tabel)
                .where(tabel.id().eq(Long.valueOf(id)))
                .select(tabel.password())
                .fetchOneOrNull();
    }

    public boolean changePassword(Integer id, ChangePasswordVO vo) {
        return sqlClient
                .createUpdate(tabel)
                .set(tabel.password(), passwordEncoder.encode(vo.getNewPassword()))
                .where(tabel.id().eq(Long.valueOf(id)))
                .execute() > 0;
    }
}
