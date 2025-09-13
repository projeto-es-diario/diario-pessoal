package br.com.diario.controller;

import br.com.diario.model.DiaryEntry;
import br.com.diario.repository.DiaryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diario")
public class DiaryController {

    @Autowired
    private DiaryEntryRepository diaryRepository;

    @GetMapping
    public ResponseEntity<List<DiaryEntry>> getEntries() {        
        return ResponseEntity.ok(List.of());
    }

    @PostMapping
    public ResponseEntity createEntry(@RequestBody DiaryEntry entryData) {        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}