package com.example.news_app_v3.network;

import com.example.news_app_v3.model.NewsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("top-news")
    Call<NewsResponse> fetchNews(
            @Query("source-country") String sourceCountry,
            @Query("language") String language,
            @Query("date") String date,
            @Query("api-key") String apiKey
    );
}