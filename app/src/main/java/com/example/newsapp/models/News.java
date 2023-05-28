package com.example.newsapp.models;

import androidx.annotation.NonNull;

import com.example.newsapp.utils.Format;

import java.io.Serializable;

public class News implements Serializable {
    private String title;
    private String link;
    private String linkImage;
    private String date;
    private String description;
    private String category;
    private boolean isSelected;

    public News() {
    }

    public News(String title, String link, String linkImage, String date) {
        this.title = title;
        this.link = link;
        this.linkImage = linkImage;
        this.date = date;
    }

    public News(String title, String link, String linkImage, String date, String description) {
        this(title, link, linkImage, date);
        this.description = description;
    }

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
        return Format.format(date);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @NonNull
    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
