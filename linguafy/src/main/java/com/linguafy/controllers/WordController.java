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
import com.linguafy.dto.WordTranslateRequestDTO;
import com.linguafy.dto.WordTranslateResponseDTO;
import com.linguafy.services.DeepLTranslationService;
import com.linguafy.services.WordService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/words")
public class WordController {

    private final WordService wordService;
    private final DeepLTranslationService deepLTranslationService;

    public WordController(WordService wordService, DeepLTranslationService deepLTranslationService) {
        this.wordService = wordService;
        this.deepLTranslationService = deepLTranslationService;
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
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/translate")
    public ResponseEntity<WordTranslateResponseDTO> translate(@RequestBody WordTranslateRequestDTO dto) {
        try {
            String translatedText = deepLTranslationService.translate(dto.getWord(), dto.getSourceLanguageCode());

            WordTranslateResponseDTO response = new WordTranslateResponseDTO();
            response.setWord(dto.getWord());
            response.setSourceLanguageCode(dto.getSourceLanguageCode());
            response.setTargetLanguageCode(deepLTranslationService.getDefaultTargetLang());
            response.setTranslation(translatedText);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<WordResponseDTO> update(@PathVariable Long id, @RequestBody WordRequestDTO dto) {
        try {
            return ResponseEntity.ok(wordService.update(id, dto));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
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
