package com.example.probook33.ignaro.classes;

import java.util.List;

/**
 * Created by probook@33 on 02-04-2017.
 */

public class group
{
    String admin;
    String groupname;
    String key;
    String g_id;

    public group() {
    }

    public group(String admin, String groupname, String g_id, String key) {
        this.admin = admin;
        this.groupname = groupname;
        this.g_id=g_id;
        this.key=key;
    }

    public String getAdmin() { return admin; }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getGroupname() {
        return groupname;
    }

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
