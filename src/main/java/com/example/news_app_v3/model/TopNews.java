package com.example.news_app_v3.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TopNews {
    @SerializedName("news")
    private List<News> news;

    public List<News> getNews() {
        return news;
    }
}