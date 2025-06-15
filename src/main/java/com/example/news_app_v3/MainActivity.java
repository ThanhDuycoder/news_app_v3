package com.example.news_app_v3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.news_app_v3.adapter.NewsAdapter;
import com.example.news_app_v3.databinding.ActivityMainBinding;
import com.example.news_app_v3.viewmodel.NewsViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NewsViewModel viewModel;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getSupportActionBar() == null) {
            setSupportActionBar(binding.toolbar);
        }
        setTitle(R.string.vietnam_news);

        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        adapter = new NewsAdapter(this, new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        viewModel.getNewsList().observe(this, news -> {
            if (news != null) {
                adapter = new NewsAdapter(this, news);
                binding.recyclerView.setAdapter(adapter);
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading && adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        });

        viewModel.getError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                binding.errorText.setText(error);
                binding.errorText.setVisibility(View.VISIBLE);
            } else {
                binding.errorText.setVisibility(View.GONE);
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.fetchNews();
            binding.swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_manage_news) {
            Intent intent = new Intent(this, ManageNewsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}