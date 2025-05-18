package com.example.aipulse.network;

import com.example.aipulse.model.Diagnosis;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DiagnosisApiService {
    @POST("/api/diagnosis")
    Call<Diagnosis> sendDiagnosis(@Body DiagnosisRequest request);

    @GET("/api/diagnosis/{id}")
    Call<Diagnosis> getDiagnosisById(@Path("id") Long id);

    @PUT("/api/diagnosis/{id}")
    Call<Diagnosis> updateDiagnosis(@Path("id") Long id, @Body DiagnosisRequest request);

    @DELETE("/api/diagnosis/{id}")
    Call<Void> deleteDiagnosis(@Path("id") Long id);
}
