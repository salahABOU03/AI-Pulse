package com.example.aipulse.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "diagnosis_results")
public class DiagnosisResult {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int bpm;
    public boolean risk;
    public String result;
    public String timestamp;
}
