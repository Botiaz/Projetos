package com.linguafy.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linguafy.dto.WordRequestDTO;
import com.linguafy.dto.WordResponseDTO;
import com.linguafy.services.WordService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/words")
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping
    public List<WordResponseDTO> findAll() {
        return wordService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WordResponseDTO> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(wordService.findById(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<WordResponseDTO> create(@RequestBody WordRequestDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(wordService.create(dto));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<WordResponseDTO> update(@PathVariable Long id, @RequestBody WordRequestDTO dto) {
        try {
            return ResponseEntity.ok(wordService.update(id, dto));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            wordService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
