package com.github.syafiqq.greendaotest001.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(
        nameInDb = "roles",
        generateGettersSetters = false
)
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Role {
    @Id
    private Long id;
    @NotNull
    private String name;

    @Generated(hash = 1450421865)
    public Role(Long id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 844947497)
    public Role() {
    }
}
