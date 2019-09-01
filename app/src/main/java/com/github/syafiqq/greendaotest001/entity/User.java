package com.github.syafiqq.greendaotest001.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import org.greenrobot.greendao.annotation.Generated;

@Entity(
        generateConstructors = false,
        generateGettersSetters = false
)
public class User {
    @Id
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String status;

    public User() {
    }
}
