package com.thikthak.app.acl.auth.domain;

import com.thikthak.app.acl.auth.domain.Role;
import com.thikthak.app.acl.auth.domain.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AUTH_USER_ROLE")
public class UserRole {
    private static final long serialVersionUID = 1;

    //    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @GeneratedValue(generator="SysPkIdSeq")
    @SequenceGenerator(name="SysPkIdSeq",sequenceName="SYS_PKID_SEQ", allocationSize=5)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    User user;
    @ManyToOne
    Role role;

    // System log fields
    Date creationDateTime;
    String creationUser;
    Date lastUpdateDateTime;
    String lastUpdateUser;

    public UserRole(){
    }
    public UserRole(Long id, User user, Role role, Date creationDateTime, String creationUser, Date lastUpdateDateTime, String lastUpdateUser) {
        this.id = id;
        this.user = user;
        this.role = role;
        this.creationDateTime = creationDateTime;
        this.creationUser = creationUser;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.lastUpdateUser = lastUpdateUser;
    }

    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }



    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }


}
