package com.example.newsapp.models;

public class Categories {
    String position;
    String url;
    String title;
    String active;

    public Categories(String position, String url, String title, String active) {
        this.position = position;
        this.url = url;
        this.title = title;
        this.active = active;
    }


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Categories() {
    }


}
