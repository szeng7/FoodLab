package com.example.lalacindyy.foodlab;

import java.util.Map;

/**
 * Created by lalacindyy on 4/24/18.
 */

public class user {
    public String username;
    public String password;
    public String email;
    //public Map<String, String> myFav;

    public user() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public user(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void setname(String name) {
        this.username = name;
    }

    public String getname() {
        return username;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getemail() {
        return email;
    }

    public void setpassword(String password) {
        this.password = password;
    }

    public String getpassword() {
        return password;
    }

}
