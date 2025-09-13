package br.com.diario.repository;

import br.com.diario.model.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {

    List<DiaryEntry> findByUsuarioIdOrderByDataHoraDesc(Long userId);
}