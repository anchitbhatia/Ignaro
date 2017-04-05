package com.example.probook33.ignaro.classes;

/**
 * Created by probook@33 on 04-04-2017.
 */

public class Membership {
    String g_id;
    String username;

    public Membership() {
    }

    public Membership(String g_id, String username) {
        this.g_id = g_id;
        this.username = username;
    }

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
