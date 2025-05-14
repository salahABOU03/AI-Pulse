package com.example.aipulse.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DiagnosisResult {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public float bpm;
    public String riskMessage;
    public long timestamp;
}
