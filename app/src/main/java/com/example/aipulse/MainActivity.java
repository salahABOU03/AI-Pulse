package com.example.aipulse;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aipulse.database.AppDatabase;
import com.example.aipulse.database.DiagnosisResult;
import com.example.aipulse.model.Diagnosis;
import com.example.aipulse.network.DiagnosisApiService;
import com.example.aipulse.network.DiagnosisRequest;
import com.example.aipulse.network.RetrofitClient;

import java.util.Random;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private TextView bpmText;
    private Button predictButton, historyButton;
    private float lastBpm = 0f;
    private boolean sensorAvailable = false;
    private EditText bpmInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bpmText = findViewById(R.id.bpmText);
        bpmInput = findViewById(R.id.bpmInput);
        predictButton = findViewById(R.id.predictButton);
        historyButton = findViewById(R.id.historyButton);

        predictButton.setEnabled(false); // Attente de lecture

        // Initialisation du capteur
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        if (heartRateSensor != null) {
            sensorAvailable = true;
            sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
            bpmText.setText("En attente du capteur...");
        } else {
            // Pas de capteur : simuler avec une valeur aléatoire
            bpmText.setText("Capteur non disponible. Simulation...");
            simulateFakeBpm();
        }

        predictButton.setOnClickListener(v -> {
            int bpm = Integer.parseInt(bpmInput.getText().toString());
            DiagnosisRequest request = new DiagnosisRequest(bpm);

            DiagnosisApiService apiService = RetrofitClient.getInstance().create(DiagnosisApiService.class);
            apiService.sendDiagnosis(request).enqueue(new Callback<Diagnosis>() {
                @Override
                public void onResponse(Call<Diagnosis> call, Response<Diagnosis> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Diagnosis diag = response.body();

                        // Save to Room (thread séparé)
                        DiagnosisResult localResult = new DiagnosisResult();
                        localResult.bpm = diag.getBpm();
                        localResult.risk = diag.isRisk();
                        localResult.result = diag.getResult();
                        localResult.timestamp = diag.getTimestamp();

                        Executors.newSingleThreadExecutor().execute(() -> {
                            AppDatabase.getInstance(MainActivity.this).resultDao().insert(localResult);
                        });

                        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                        intent.putExtra("result", diag.getResult());
                        intent.putExtra("risk", diag.isRisk());
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Erreur lors de la prédiction", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Diagnosis> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Échec de la requête : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        historyButton.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
    }

    private void simulateFakeBpm() {
        new Handler().postDelayed(() -> {
            Random rand = new Random();
            lastBpm = 60 + rand.nextInt(40); // Valeur entre 60 et 100
            bpmText.setText("BPM simulé : " + (int) lastBpm);
            bpmInput.setText(String.valueOf((int) lastBpm));
            predictButton.setEnabled(true);
        }, 3000); // 3 secondes de "délai"
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            lastBpm = event.values[0];
            bpmText.setText("BPM détecté : " + (int) lastBpm);
            bpmInput.setText(String.valueOf((int) lastBpm));
            predictButton.setEnabled(true);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorAvailable) {
            sensorManager.unregisterListener(this);
        }
    }
}
