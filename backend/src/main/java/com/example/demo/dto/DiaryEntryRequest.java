package com.example.demo.dto;

import com.example.demo.model.DiaryEntry;

public class DiaryEntryRequest {
    private String content;
    private Integer mood;
    private DiaryEntry.EntryStatus status = DiaryEntry.EntryStatus.PUBLISHED; // Default to PUBLISHED

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMood() {
        return mood;
    }

    public void setMood(Integer mood) {
        this.mood = mood;
    }

    public DiaryEntry.EntryStatus getStatus() {
        return status;
    }

    public void setStatus(DiaryEntry.EntryStatus status) {
        this.status = status;
    }
}