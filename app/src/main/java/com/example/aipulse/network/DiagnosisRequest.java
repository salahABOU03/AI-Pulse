package com.example.aipulse.network;

public class DiagnosisRequest {
    private int bpm;

    public DiagnosisRequest(int bpm) {
        this.bpm = bpm;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }
}
