package com.example.demo.dto;

import com.example.demo.model.DiaryEntry;
import java.time.LocalDateTime;

public class DiaryEntryResponse {
    private Long id;
    private String content;
    private Integer mood;
    private LocalDateTime createdAt;
    private DiaryEntry.EntryStatus status;

    public DiaryEntryResponse(Long id, String content, Integer mood, LocalDateTime createdAt, DiaryEntry.EntryStatus status) {
        this.id = id;
        this.content = content;
        this.mood = mood;
        this.createdAt = createdAt;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DiaryEntry.EntryStatus getStatus() {
        return status;
    }

    public void setStatus(DiaryEntry.EntryStatus status) {
        this.status = status;
    }
}