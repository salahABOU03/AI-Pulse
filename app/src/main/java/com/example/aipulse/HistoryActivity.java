package com.example.aipulse;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aipulse.database.AppDatabase;
import com.example.aipulse.database.DiagnosisResult;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class HistoryActivity extends AppCompatActivity {

    private ListView historyList;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyList = findViewById(R.id.historyList);

        executor.execute(() -> {
            List<DiagnosisResult> results = AppDatabase.getInstance(this)
                    .resultDao()
                    .getAllResults();

            List<String> display = results.stream()
                    .map(r -> r.timestamp + " - BPM: " + r.bpm + " - " + r.result)
                    .collect(Collectors.toList());

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        HistoryActivity.this,
                        android.R.layout.simple_list_item_1,
                        display
                );
                historyList.setAdapter(adapter);
            });
        });
    }
}
