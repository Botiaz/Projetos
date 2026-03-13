package com.linguafy.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linguafy.dto.SaveWordRequestDTO;
import com.linguafy.dto.TranslateRequestDTO;
import com.linguafy.dto.TranslateResponseDTO;
import com.linguafy.dto.WordResponseDTO;
import com.linguafy.services.TranslationService;
import com.linguafy.services.WordService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final TranslationService translationService;
    private final WordService wordService;

    public DashboardController(TranslationService translationService, WordService wordService) {
        this.translationService = translationService;
        this.wordService = wordService;
    }

    @PostMapping("/translate")
    public ResponseEntity<?> translate(@RequestBody TranslateRequestDTO dto) {
        try {
            return ResponseEntity.ok(translationService.translate(dto));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/words")
    public ResponseEntity<WordResponseDTO> saveWord(@RequestBody SaveWordRequestDTO dto, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wordService.createForUser(principal.getName(), dto));
    }

    @GetMapping("/words")
    public List<WordResponseDTO> getMyWords(Principal principal) {
        return wordService.findMyWords(principal.getName());
    }
}