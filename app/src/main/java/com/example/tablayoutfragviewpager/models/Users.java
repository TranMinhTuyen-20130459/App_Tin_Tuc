package com.example.tablayoutfragviewpager.models;

public class Users {
    String role;


    String username;
    String password;
    String fullname;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Users() {

    }
    public Users(String role, String username, String password, String fullname) {
        this.role = role;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
    }

}
