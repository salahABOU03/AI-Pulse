package com.example.aipulse;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aipulse.database.AppDatabase;
import com.example.aipulse.database.DiagnosisResult;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            List<DiagnosisResult> results = AppDatabase.getInstance(this).resultDao().getAll();
            runOnUiThread(() -> {
                adapter = new HistoryAdapter(results);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }
}
