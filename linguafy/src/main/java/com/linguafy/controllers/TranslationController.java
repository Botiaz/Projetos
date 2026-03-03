package com.linguafy.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linguafy.services.DeepLService;

@RestController
@RequestMapping("/api/translate")
public class TranslationController {

    private final DeepLService deepLService;

    public TranslationController(DeepLService deepLService) {
        this.deepLService = deepLService;
    }

    @GetMapping
    public ResponseEntity<String> translate(
            @RequestParam String word,
            @RequestParam String sourceLang,
            @RequestParam(defaultValue = "PT-BR") String targetLang) {
        if (word == null || word.isBlank()) {
            return ResponseEntity.badRequest().body("O parâmetro 'word' não pode ser vazio");
        }
        if (sourceLang == null || sourceLang.isBlank()) {
            return ResponseEntity.badRequest().body("O parâmetro 'sourceLang' não pode ser vazio");
        }
        String translation = deepLService.translate(word, sourceLang, targetLang);
        return ResponseEntity.ok(translation);
    }
}
