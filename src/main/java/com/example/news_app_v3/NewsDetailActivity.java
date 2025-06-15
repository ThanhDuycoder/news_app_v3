package com.example.news_app_v3;


import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.news_app_v3.databinding.ActivityNewsDetailBinding;
import com.example.news_app_v3.model.News;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsDetailActivity extends AppCompatActivity {
    private ActivityNewsDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.news_detail);
        News news = (News) getIntent().getSerializableExtra("news");
        if (news != null) {
            binding.newsTitle.setText(news.getTitle());
            binding.newsText.setText(news.getText());

            if (news.getImage() != null && !news.getImage().isEmpty()) {
                binding.newsImage.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(news.getImage())
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_error)
                        .into(binding.newsImage);
            } else {
                binding.newsImage.setVisibility(View.GONE);
            }

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
                Date date = inputFormat.parse(news.getPublishDate());
                binding.newsDate.setText(String.format(getString(R.string.published), outputFormat.format(date)));
            } catch (ParseException e) {
                binding.newsDate.setText(String.format(getString(R.string.published), news.getPublishDate()));
            }

            if (news.getAuthor() != null && !news.getAuthor().isEmpty()) {
                binding.newsAuthor.setText(String.format(getString(R.string.author), news.getAuthor()));
            } else {
                binding.newsAuthor.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}