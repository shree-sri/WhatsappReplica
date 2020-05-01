package com.shanmathi.myapplication.Models;

public class Users {
    private String id;
    private String username;
    private String imgURL;
    private String status;
    private String search;

    public Users() {
    }

    public Users(String id, String username, String imgURL, String status, String search) {
        this.id = id;
        this.username = username;
        this.imgURL = imgURL;
        this.status = status;
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
