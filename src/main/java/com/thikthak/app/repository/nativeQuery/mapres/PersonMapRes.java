package com.thikthak.app.repository.nativeQuery.mapres;


public class PersonMapRes {

    public Long id;
    public String displayName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public PersonMapRes() {
    }

    public PersonMapRes(Long id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }


}
