package com.github.syafiqq.greendaotest001.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(
        nameInDb = "users_roles",
        generateGettersSetters = false
)
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class UserRole {
    @Id
    private Long id;
    @Property(nameInDb = "user_id")
    private Long userId;
    @Property(nameInDb = "role_id")
    private Long roleId;

    @Generated(hash = 46191464)
    public UserRole(Long id, Long userId, Long roleId) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
    }

    @Generated(hash = 552541888)
    public UserRole() {
    }
}
