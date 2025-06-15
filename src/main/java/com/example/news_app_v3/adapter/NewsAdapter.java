package com.example.news_app_v3.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.news_app_v3.R;
import com.example.news_app_v3.model.News;
import com.example.news_app_v3.NewsDetailActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private final List<News> newsList;
    private final Context context;

    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.summary.setText(news.getSummary());

        if (news.getImage() != null && !news.getImage().isEmpty()) {
            Glide.with(context)
                    .load(news.getImage())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.ic_placeholder);
        }

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
            Date date = inputFormat.parse(news.getPublishDate());
            holder.date.setText(String.format(context.getString(R.string.published), outputFormat.format(date)));
        } catch (ParseException e) {
            holder.date.setText(String.format(context.getString(R.string.published), news.getPublishDate()));
        }

        if (news.getAuthor() != null && !news.getAuthor().isEmpty()) {
            holder.author.setText(String.format(context.getString(R.string.author), news.getAuthor()));
        } else {
            holder.author.setVisibility(View.GONE);
        }

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("news", news);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, summary, date, author;
        CardView cardView;

        NewsViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            image = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
            summary = itemView.findViewById(R.id.news_summary);
            date = itemView.findViewById(R.id.news_date);
            author = itemView.findViewById(R.id.news_author);
        }
    }
}