package com.example.probook33.ignaro.classes;

/**
 * Created by probook@33 on 04-04-2017.
 */

public class User {

    String username;
    String email;
    String name;

    public User() {
    }

    public User(String u_id, String email, String name) {
        this.username = u_id;
        this.email = email;
        this.name = name;

    }


    public String getU_id() {
        return username;
    }

    public void setU_id(String u_id) {
        this.username = u_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
