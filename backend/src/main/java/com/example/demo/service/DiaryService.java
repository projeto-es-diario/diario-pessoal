package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DiaryEntryRequest;
import com.example.demo.model.DiaryEntry;
import com.example.demo.model.User;
import com.example.demo.repository.DiaryEntryRepository;
import com.example.demo.repository.UserRepository;

@Service
public class DiaryService {

    private final DiaryEntryRepository diaryEntryRepository;
    private final UserRepository userRepository;

    public DiaryService(DiaryEntryRepository diaryEntryRepository, UserRepository userRepository) {
        this.diaryEntryRepository = diaryEntryRepository;
        this.userRepository = userRepository;
    }

    public DiaryEntry createEntry(String userEmail, DiaryEntryRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        DiaryEntry newEntry = new DiaryEntry();
        newEntry.setContent(request.getContent());
        newEntry.setMood(request.getMood());
        newEntry.setUser(user);
        newEntry.setStatus(request.getStatus());

        return diaryEntryRepository.save(newEntry);
    }

    public List<DiaryEntry> getEntriesForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return diaryEntryRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public List<DiaryEntry> searchEntries(String userEmail, String keyword, LocalDate date) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (keyword != null && !keyword.isEmpty()) {
            return diaryEntryRepository.findByUserIdAndContentContainingIgnoreCaseOrderByCreatedAtDesc(user.getId(), keyword);
        }

        if (date != null) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
            return diaryEntryRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(user.getId(), startOfDay, endOfDay);
        }

        return getEntriesForUser(userEmail); // Default to all entries if no criteria
    }
}