package com.example.aipulse.model;

public class Diagnosis {
    private Long id;
    private int bpm;
    private boolean risk;
    private String result;
    private String timestamp;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getBpm() { return bpm; }
    public void setBpm(int bpm) { this.bpm = bpm; }

    public boolean isRisk() { return risk; }
    public void setRisk(boolean risk) { this.risk = risk; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
