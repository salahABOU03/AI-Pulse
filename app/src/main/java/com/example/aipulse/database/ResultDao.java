package com.example.aipulse.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ResultDao {
    @Insert
    void insert(DiagnosisResult result);

    @Query("SELECT * FROM diagnosis_results ORDER BY id DESC")
    List<DiagnosisResult> getAllResults();
}
