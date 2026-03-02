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

import com.linguafy.dto.LanguageRequestDTO;
import com.linguafy.dto.LanguageResponseDTO;
import com.linguafy.services.LanguageService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public List<LanguageResponseDTO> findAll() {
        return languageService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageResponseDTO> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(languageService.findById(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<LanguageResponseDTO> create(@RequestBody LanguageRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(languageService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LanguageResponseDTO> update(@PathVariable Long id, @RequestBody LanguageRequestDTO dto) {
        try {
            return ResponseEntity.ok(languageService.update(id, dto));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            languageService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
