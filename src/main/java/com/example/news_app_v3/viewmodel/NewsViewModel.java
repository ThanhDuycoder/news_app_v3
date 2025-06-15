package com.example.news_app_v3.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.news_app_v3.model.News;
import com.example.news_app_v3.model.NewsResponse;
import com.example.news_app_v3.model.TopNews;
import com.example.news_app_v3.network.NewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsViewModel extends AndroidViewModel {
    private final MutableLiveData<List<News>> newsList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final NewsService newsService;

    public NewsViewModel(Application application) {
        super(application);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.worldnewsapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newsService = retrofit.create(NewsService.class);
        fetchNews();
    }

    public LiveData<List<News>> getNewsList() {
        return newsList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void fetchNews() {
        isLoading.setValue(true);
        error.setValue("");
        String yesterday = getYesterdayDate();
        Call<NewsResponse> call = newsService.fetchNews("vn", "vi", yesterday, "0fe3578a3be242f6a6fba2fd47344f78");
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<News> news = new ArrayList<>();
                    for (TopNews topNews : response.body().getTopNews()) {
                        news.addAll(topNews.getNews());
                    }
                    newsList.setValue(news);
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        error.setValue("Failed to load news: " + errorBody);
                    } catch (Exception e) {
                        error.setValue("Failed to load news: HTTP " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                isLoading.setValue(false);
                error.setValue("Error: " + t.getMessage());
            }
        });
    }

    private String getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(calendar.getTime());
    }
}