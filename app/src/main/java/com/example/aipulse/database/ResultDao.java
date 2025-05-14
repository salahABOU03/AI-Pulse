package com.example.aipulse.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ResultDao {
    @Insert
    void insert(DiagnosisResult result);

    @Query("SELECT * FROM DiagnosisResult ORDER BY timestamp DESC")
    List<DiagnosisResult> getAll();
}
