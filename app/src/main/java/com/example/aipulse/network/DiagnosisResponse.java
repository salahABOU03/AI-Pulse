package com.example.aipulse.network;

public class DiagnosisResponse {
    private String message;
    private boolean risk;

    public String getMessage() {
        return message;
    }

    public boolean isRisk() {
        return risk;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRisk(boolean risk) {
        this.risk = risk;
    }
}
