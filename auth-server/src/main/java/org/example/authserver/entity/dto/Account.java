package org.example.authserver.entity.dto;


import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "users")
public interface Account {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    @Key
    String username();

    String password();

    @Nullable
    String name();

    @Nullable
    String sex();

    @Nullable
    Date birthDate();

    @Nullable
    String signature();

    @Nullable
    String avatarPath();

    @Nullable
    String role();

    @Nullable
    LocalDateTime registerTime();
}

