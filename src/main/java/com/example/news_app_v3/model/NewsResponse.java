package com.example.news_app_v3.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NewsResponse {
    @SerializedName("top_news")
    private List<TopNews> topNews;
    private String language;
    private String country;

    public NewsResponse(String language, String country) {
        this.language = language;
        this.country = country;
    }

    public List<TopNews> getTopNews() {
        return topNews;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }
}