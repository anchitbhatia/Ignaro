package com.example.probook33.ignaro.classes;

/**
 * Created by probook@33 on 05-04-2017.
 */

public class GroupLocation {
    String g_id,g_name,name,lat,lon;

    public GroupLocation() {
    }

    public GroupLocation(String g_id, String g_name, String name, String lat, String lon) {
        this.g_id = g_id;
        this.g_name = g_name;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getG_id() {

        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public String getG_name() {
        return g_name;
    }

    public void setG_name(String g_name) {
        this.g_name = g_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
