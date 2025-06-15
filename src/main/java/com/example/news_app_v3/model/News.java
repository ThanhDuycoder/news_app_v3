package com.example.news_app_v3.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class News implements Serializable {
    private int id;
    private String title;
    private String text;
    private String summary;
    private String url;
    private String image;
    private String video;
    @SerializedName("publish_date")
    private String publishDate;
    private String author;
    private List<String> authors;
    private String language;
    @SerializedName("source_country")
    private String sourceCountry;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getSummary() {
        return summary;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getLanguage() {
        return language;
    }

    public String getSourceCountry() {
        return sourceCountry;
    }
}