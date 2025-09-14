package com.diario.diario.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diario.diario.model.DiaryEntry;

@Repository
public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {

    List<DiaryEntry> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<DiaryEntry> findByUserIdAndContentContainingIgnoreCaseOrderByCreatedAtDesc(Long userId, String keyword);

    List<DiaryEntry> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long userId, LocalDateTime start, LocalDateTime end);
}
