package com.linguafy.services;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.linguafy.dto.TranslateRequestDTO;
import com.linguafy.dto.TranslateResponseDTO;
import com.linguafy.entities.Language;
import com.linguafy.repositories.LanguageRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TranslationService {

    private final LanguageRepository languageRepository;

    public TranslationService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public TranslateResponseDTO translate(TranslateRequestDTO request) {
        Language language = languageRepository.findById(request.getLanguageId())
                .orElseThrow(() -> new EntityNotFoundException("Idioma não encontrado"));

        Map<String, String> sampleDictionary = new HashMap<>();
        sampleDictionary.put("hello", "olá");
        sampleDictionary.put("house", "casa");
        sampleDictionary.put("book", "livro");
        sampleDictionary.put("water", "água");

        String original = request.getWord() == null ? "" : request.getWord().trim();
        String normalized = original.toLowerCase(Locale.ROOT);
        String translated = sampleDictionary.getOrDefault(normalized, original + " (" + language.getName() + ")");

        TranslateResponseDTO response = new TranslateResponseDTO();
        response.setOriginal(original);
        response.setTranslated(translated);
        return response;
    }
}