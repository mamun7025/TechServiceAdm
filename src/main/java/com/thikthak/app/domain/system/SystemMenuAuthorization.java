package com.thikthak.app.domain.system;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="SYSTEM_MENU_AUTHORIZATION")
public class SystemMenuAuthorization {

    @Id
    @GeneratedValue(generator="SysPkIdSeq")
    @SequenceGenerator(name="SysPkIdSeq",sequenceName="SYS_PKID_SEQ", allocationSize=5)
    Long id;
    String menuCode;
    String parentMenuCode;
    String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_menu_id", nullable = false)
    SystemMenu systemMenu;

    Boolean canSee;
    Integer sequence;

    // System log fields
    Date creationDateTime;
    String creationUser;
    Date lastUpdateDateTime;
    String lastUpdateUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getParentMenuCode() {
        return parentMenuCode;
    }

    public void setParentMenuCode(String parentMenuCode) {
        this.parentMenuCode = parentMenuCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SystemMenu getSystemMenu() {
        return systemMenu;
    }

    public void setSystemMenu(SystemMenu systemMenu) {
        this.systemMenu = systemMenu;
    }

    public Boolean getCanSee() {
        return canSee;
    }

    public void setCanSee(Boolean canSee) {
        this.canSee = canSee;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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
