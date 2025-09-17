package com.example.demo.service;

import com.example.demo.dto.DiaryEntryRequest;
import com.example.demo.model.DiaryEntry;
import com.example.demo.model.User;
import com.example.demo.repository.DiaryEntryRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {

    @Mock
    private DiaryEntryRepository diaryEntryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DiaryService diaryService;

    @Test
    void shouldCreateEntry() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");

        DiaryEntryRequest req = new DiaryEntryRequest();
        req.setContent("Olá mundo");
        req.setMood(5);

        DiaryEntry saved = new DiaryEntry();
        saved.setId(10L);
        saved.setContent(req.getContent());

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(diaryEntryRepository.save(any(DiaryEntry.class))).thenReturn(saved);

        DiaryEntry result = diaryService.createEntry("user@test.com", req);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(diaryEntryRepository, times(1)).save(any(DiaryEntry.class));
    }

    @Test
    void shouldGetEntriesForUser() {
        User user = new User();
        user.setId(2L);
        user.setEmail("u2@test.com");

        DiaryEntry e = new DiaryEntry();
        e.setId(100L);

        when(userRepository.findByEmail("u2@test.com")).thenReturn(Optional.of(user));
        when(diaryEntryRepository.findByUserIdOrderByCreatedAtDesc(2L)).thenReturn(List.of(e));

        List<DiaryEntry> list = diaryService.getEntriesForUser("u2@test.com");
        assertEquals(1, list.size());
        assertEquals(100L, list.get(0).getId());
    }

    @Test
    void shouldSearchByKeywordOrDate() {
        User user = new User();
        user.setId(3L);
        user.setEmail("u3@test.com");

        DiaryEntry e1 = new DiaryEntry();
        e1.setId(201L);

        when(userRepository.findByEmail("u3@test.com")).thenReturn(Optional.of(user));
        when(diaryEntryRepository.findByUserIdAndContentContainingIgnoreCaseOrderByCreatedAtDesc(3L, "hello"))
                .thenReturn(List.of(e1));

        List<DiaryEntry> byKeyword = diaryService.searchEntries("u3@test.com", "hello", null);
        assertEquals(1, byKeyword.size());

        
        LocalDate date = LocalDate.now();
        when(diaryEntryRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(anyLong(), any(), any()))
                .thenReturn(List.of(e1));

        List<DiaryEntry> byDate = diaryService.searchEntries("u3@test.com", null, date);
        assertEquals(1, byDate.size());
    }
}
