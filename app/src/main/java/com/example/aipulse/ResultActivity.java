package com.example.aipulse;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aipulse.database.AppDatabase;
import com.example.aipulse.database.DiagnosisResult;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

    public class ResultActivity extends AppCompatActivity {

    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textViewResult = findViewById(R.id.textViewResult);
        float bpm = getIntent().getFloatExtra("bpm", 70);

        try {
            Interpreter tflite = new Interpreter(loadModelFile());
            float[][] input = new float[1][1];
            float[][] output = new float[1][1];

            input[0][0] = bpm;
            tflite.run(input, output);

            float result = output[0][0];
            String message;
            if (result > 0.5) {
                message = "Risque ÉLEVÉ de maladie cardiaque";
            } else {
                message = "Risque FAIBLE de maladie cardiaque";
            }

            textViewResult.setText("Résultat : " + message);

            // Sauvegarde en base de données
            DiagnosisResult diag = new DiagnosisResult();
            diag.bpm = bpm;
            diag.riskMessage = message;
            diag.timestamp = System.currentTimeMillis();

            new Thread(() -> AppDatabase.getInstance(this).resultDao().insert(diag)).start();

        } catch (Exception e) {
            textViewResult.setText("Erreur dans le modèle");
            e.printStackTrace();
        }
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd("heart_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.getStartOffset(), fileDescriptor.getDeclaredLength());
    }
}
