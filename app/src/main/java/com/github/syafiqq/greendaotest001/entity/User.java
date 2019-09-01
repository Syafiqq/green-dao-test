package com.github.syafiqq.greendaotest001.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class User {
    @Id
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String status;

    @Generated(hash = 1882300885)
    public User(Long id, @NotNull String name, @NotNull String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
