package com.example.newsapp.models;

import com.example.newsapp.utils.Format;

import java.io.Serializable;

public class News implements Serializable {
    private String title;
    private String link;
    private String linkImage;
    private String date;
    String des;
    private String category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getDate() {
        return Format.formartDate(date);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public News(String title, String link, String linkImage, String date) {
        this.title = title;
        this.link = link;
        this.linkImage = linkImage;
        this.date = date;
    }
}
