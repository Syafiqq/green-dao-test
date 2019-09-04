package com.github.syafiqq.greendaotest001.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.greenrobot.greendao.DaoException;

@Entity(
        nameInDb = "users_roles",
        generateGettersSetters = false
)
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class UserRole {

    /*
     * For Column Property
     * */

    @Id
    private Long id;

    @Property(nameInDb = "user_id")
    private Long userId;

    @Property(nameInDb = "role_id")
    private Long roleId;

    /*
     * For Relation
     * */

    @EqualsAndHashCode.Exclude
    @ToOne(joinProperty = "userId")
    private User user;

    @EqualsAndHashCode.Exclude
    @ToOne(joinProperty = "roleId")
    private Role role;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1114803083)
    private transient UserRoleDao myDao;

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;

    @Generated(hash = 312471022)
    private transient Long role__resolvedKey;

    @Generated(hash = 46191464)
    public UserRole(Long id, Long userId, Long roleId) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
    }

    @Generated(hash = 552541888)
    public UserRole() {
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 859885876)
    public User getUser() {
        Long __key = this.userId;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1065606912)
    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            userId = user == null ? null : user.getId();
            user__resolvedKey = userId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1948065557)
    public Role getRole() {
        Long __key = this.roleId;
        if (role__resolvedKey == null || !role__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RoleDao targetDao = daoSession.getRoleDao();
            Role roleNew = targetDao.load(__key);
            synchronized (this) {
                role = roleNew;
                role__resolvedKey = __key;
            }
        }
        return role;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 736917490)
    public void setRole(Role role) {
        synchronized (this) {
            this.role = role;
            roleId = role == null ? null : role.getId();
            role__resolvedKey = roleId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1183361425)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserRoleDao() : null;
    }
}
