package com.master.plannerway;

import java.util.Date;

public class UserInfo {
    public String uid;
    public String username;
    public String name;
    public String email;
    public Date dob;
    public UserInfo(){}


    public String getEmail() {
        return email;
    }

    public Date getDob() {return dob; }

    public UserInfo(String uid,
                    String username,
                    String name,
                    String email,
                    Date dob){
        this.uid = uid;
        this.username = username;

        this.dob = dob;
        this.email = email;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }
}
