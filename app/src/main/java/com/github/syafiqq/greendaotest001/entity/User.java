package com.github.syafiqq.greendaotest001.entity;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(
        nameInDb = "users",
        generateGettersSetters = false
)
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class User {

    /*
     * For Column Property
     * */

    @Id
    private Long id;

    @Property(nameInDb = "name")
    @NotNull
    private String name;

    @Property(nameInDb = "status")
    @NotNull
    private String status;

    @Property(nameInDb = "parent_id")
    private Long parentId;

    /*
     * For Relation
     * */

    @ToMany(referencedJoinProperty = "userId")
    private List<Note> orders;

    @ToMany
    @JoinEntity(
            entity = UserRole.class,
            sourceProperty = "userId",
            targetProperty = "roleId"
    )
    private List<Role> roles;

    @ToOne(joinProperty = "parentId")
    private User parent;

    @ToMany(referencedJoinProperty = "parentId")
    private List<User> children;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;

    @Generated(hash = 1293412156)
    private transient Long parent__resolvedKey;

    @Generated(hash = 36145661)
    public User(Long id, @NotNull String name, @NotNull String status, Long parentId) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.parentId = parentId;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1151083587)
    public List<Note> getOrders() {
        if (orders == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NoteDao targetDao = daoSession.getNoteDao();
            List<Note> ordersNew = targetDao._queryUser_Orders(id);
            synchronized (this) {
                if (orders == null) {
                    orders = ordersNew;
                }
            }
        }
        return orders;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1446109810)
    public synchronized void resetOrders() {
        orders = null;
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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 353725095)
    public List<Role> getRoles() {
        if (roles == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RoleDao targetDao = daoSession.getRoleDao();
            List<Role> rolesNew = targetDao._queryUser_Roles(id);
            synchronized (this) {
                if (roles == null) {
                    roles = rolesNew;
                }
            }
        }
        return roles;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 254386649)
    public synchronized void resetRoles() {
        roles = null;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 273916361)
    public User getParent() {
        Long __key = this.parentId;
        if (parent__resolvedKey == null || !parent__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User parentNew = targetDao.load(__key);
            synchronized (this) {
                parent = parentNew;
                parent__resolvedKey = __key;
            }
        }
        return parent;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 644276634)
    public void setParent(User parent) {
        synchronized (this) {
            this.parent = parent;
            parentId = parent == null ? null : parent.getId();
            parent__resolvedKey = parentId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1414846852)
    public List<User> getChildren() {
        if (children == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            List<User> childrenNew = targetDao._queryUser_Children(id);
            synchronized (this) {
                if (children == null) {
                    children = childrenNew;
                }
            }
        }
        return children;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1590975152)
    public synchronized void resetChildren() {
        children = null;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }
}
