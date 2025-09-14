package com.diario.diario.controller;

import com.diario.diario.dto.DiaryEntryRequest;
import com.diario.diario.dto.DiaryEntryResponse;
import com.diario.diario.model.DiaryEntry;
import com.diario.diario.service.DiaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/entries")
    public ResponseEntity<DiaryEntryResponse> createEntry(@RequestBody DiaryEntryRequest request, Authentication authentication) {
        String userEmail = authentication.getName();
        DiaryEntry newEntry = diaryService.createEntry(userEmail, request);
        DiaryEntryResponse response = new DiaryEntryResponse(newEntry.getId(), newEntry.getContent(), newEntry.getMood(), newEntry.getCreatedAt());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/entries")
    public ResponseEntity<List<DiaryEntryResponse>> getUserEntries(Authentication authentication) {
        String userEmail = authentication.getName();
        List<DiaryEntry> entries = diaryService.getEntriesForUser(userEmail);
        List<DiaryEntryResponse> response = entries.stream()
                .map(e -> new DiaryEntryResponse(e.getId(), e.getContent(), e.getMood(), e.getCreatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/entries/search")
    public ResponseEntity<List<DiaryEntryResponse>> searchEntries(
            Authentication authentication,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        String userEmail = authentication.getName();
        List<DiaryEntry> entries = diaryService.searchEntries(userEmail, keyword, date);
        List<DiaryEntryResponse> response = entries.stream()
                .map(e -> new DiaryEntryResponse(e.getId(), e.getContent(), e.getMood(), e.getCreatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
