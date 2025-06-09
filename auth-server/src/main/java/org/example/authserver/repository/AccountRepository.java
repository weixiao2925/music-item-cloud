package org.example.authserver.repository;


import lombok.RequiredArgsConstructor;
import org.babyfish.jimmer.sql.JSqlClient;

import org.example.authserver.entity.dto.Account;
import org.example.authserver.entity.dto.AccountTable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountRepository {

    private final JSqlClient sqlClient;
    private static final AccountTable tabel = AccountTable.$;

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
}
