package com.example.aipulse;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView resultText, riskText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultText = findViewById(R.id.resultText);
        riskText = findViewById(R.id.riskText);

        String result = getIntent().getStringExtra("result");
        boolean risk = getIntent().getBooleanExtra("risk", false);

        resultText.setText(result);
        riskText.setText(risk ? "Risque détecté" : "Pas de risque");
    }
}
