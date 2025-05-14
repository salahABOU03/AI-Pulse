package com.example.aipulse;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager;
    Sensor heartRateSensor;
    TextView textViewHeartRate;
    Button buttonAnalyze, historyButton;
    float currentBpm = 75f;

    Interpreter tflite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewHeartRate = findViewById(R.id.textViewHeartRate);
        buttonAnalyze = findViewById(R.id.buttonAnalyze);
        historyButton = findViewById(R.id.historyButton); // Nouveau bouton

        // Initialiser capteur
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        if (heartRateSensor != null) {
            sensorManager.registerListener(sensorListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            currentBpm = new Random().nextInt(40) + 60;
            textViewHeartRate.setText("BPM simulé: " + currentBpm);
        }

        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonAnalyze.setOnClickListener(v -> {
            float prediction = runModel(currentBpm);
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("bpm", currentBpm);
            intent.putExtra("result", prediction);
            startActivity(intent);
        });

        // Nouveau : bouton historique
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            currentBpm = event.values[0];
            textViewHeartRate.setText("BPM: " + currentBpm);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("heart_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private float runModel(float inputVal) {
        float[][] input = {{inputVal}};
        float[][] output = new float[1][1];
        tflite.run(input, output);
        return output[0][0];
    }
}
