package com.example.news_app_v3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageNewsActivity extends AppCompatActivity {
    private EditText editTitle, editContent, editDate;
    private Button btnAdd, btnUpdate, btnDelete;
    private ListView newsList;
    private ArrayList<String> newsListItems;
    private ArrayAdapter<String> adapter;
    private NewsDatabaseHelper dbHelper;
    private int selectedPosition = -1;
    private HashMap<Integer, Integer> positionToIdMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_news);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTitle = findViewById(R.id.edit_title);
        editContent = findViewById(R.id.edit_content);
        editDate = findViewById(R.id.edit_date);
        btnAdd = findViewById(R.id.btn_add);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        newsList = findViewById(R.id.news_list);

        newsListItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, newsListItems);
        newsList.setAdapter(adapter);

        dbHelper = new NewsDatabaseHelper(this);
        positionToIdMap = new HashMap<>();
        loadNews();

        btnAdd.setOnClickListener(v -> addNews());
        btnUpdate.setOnClickListener(v -> updateNews());
        btnDelete.setOnClickListener(v -> deleteNews());

        newsList.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            String selectedItem = newsListItems.get(position);
            String[] parts = selectedItem.split("\n");
            if (parts.length >= 3) {
                editTitle.setText(parts[0].replace("Title: ", ""));
                editContent.setText(parts[1].replace("Content: ", ""));
                editDate.setText(parts[2].replace("Date: ", ""));
                btnUpdate.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
            }
        });
    }

    private void loadNews() {
        newsListItems.clear();
        positionToIdMap.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                NewsDatabaseHelper.TABLE_NAME,
                null, null, null, null, null, null
        );
        int idIndex = cursor.getColumnIndexOrThrow(NewsDatabaseHelper.COLUMN_ID);
        int titleIndex = cursor.getColumnIndexOrThrow(NewsDatabaseHelper.COLUMN_TITLE);
        int contentIndex = cursor.getColumnIndexOrThrow(NewsDatabaseHelper.COLUMN_CONTENT);
        int dateIndex = cursor.getColumnIndexOrThrow(NewsDatabaseHelper.COLUMN_DATE);

        if (cursor.moveToFirst()) {
            int position = 0;
            do {
                int id = cursor.getInt(idIndex);
                String title = cursor.getString(titleIndex);
                String content = cursor.getString(contentIndex);
                String date = cursor.getString(dateIndex);
                String item = "Title: " + title + "\nContent: " + content + "\nDate: " + date;
                newsListItems.add(item);
                positionToIdMap.put(position, id);
                position++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void addNews() {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();
        String date = editDate.getText().toString().trim();

        if (!title.isEmpty() && !content.isEmpty() && !date.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NewsDatabaseHelper.COLUMN_TITLE, title);
            values.put(NewsDatabaseHelper.COLUMN_CONTENT, content);
            values.put(NewsDatabaseHelper.COLUMN_DATE, date);
            db.insert(NewsDatabaseHelper.TABLE_NAME, null, values);
            db.close();
            clearFields();
            loadNews();
        }
    }

    private void updateNews() {
        if (selectedPosition != -1) {
            String title = editTitle.getText().toString().trim();
            String content = editContent.getText().toString().trim();
            String date = editDate.getText().toString().trim();

            if (!title.isEmpty() && !content.isEmpty() && !date.isEmpty()) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(NewsDatabaseHelper.COLUMN_TITLE, title);
                values.put(NewsDatabaseHelper.COLUMN_CONTENT, content);
                values.put(NewsDatabaseHelper.COLUMN_DATE, date);
                Integer id = positionToIdMap.get(selectedPosition);
                if (id != null) {
                    db.update(
                            NewsDatabaseHelper.TABLE_NAME,
                            values,
                            NewsDatabaseHelper.COLUMN_ID + " = ?",
                            new String[]{String.valueOf(id)}
                    );
                }
                db.close();
                clearFields();
                loadNews();
                selectedPosition = -1;
                toggleButtons();
            }
        }
    }

    private void deleteNews() {
        if (selectedPosition != -1) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Integer id = positionToIdMap.get(selectedPosition);
            if (id != null) {
                db.delete(
                        NewsDatabaseHelper.TABLE_NAME,
                        NewsDatabaseHelper.COLUMN_ID + " = ?",
                        new String[]{String.valueOf(id)}
                );
                db.close();
                clearFields();
                loadNews();
                selectedPosition = -1;
                toggleButtons();
            }
        }
    }

    private void clearFields() {
        editTitle.setText("");
        editContent.setText("");
        editDate.setText("");
        toggleButtons();
    }

    private void toggleButtons() {
        btnAdd.setVisibility(selectedPosition == -1 ? View.VISIBLE : View.GONE);
        btnUpdate.setVisibility(selectedPosition != -1 ? View.VISIBLE : View.GONE);
        btnDelete.setVisibility(selectedPosition != -1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}