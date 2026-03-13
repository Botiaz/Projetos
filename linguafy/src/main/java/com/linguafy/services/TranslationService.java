package com.linguafy.services;

import org.springframework.stereotype.Service;

import com.linguafy.dto.TranslateRequestDTO;
import com.linguafy.dto.TranslateResponseDTO;
import com.linguafy.entities.Language;
import com.linguafy.repositories.LanguageRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TranslationService {

    private final LanguageRepository languageRepository;
    private final DeepLTranslationService deepLTranslationService;

    public TranslationService(LanguageRepository languageRepository, DeepLTranslationService deepLTranslationService) {
        this.languageRepository = languageRepository;
        this.deepLTranslationService = deepLTranslationService;
    }

    public TranslateResponseDTO translate(TranslateRequestDTO request) {
        Language language = languageRepository.findById(request.getLanguageId())
                .orElseThrow(() -> new EntityNotFoundException("Idioma não encontrado"));

        String original = request.getWord() == null ? "" : request.getWord().trim();
        if (original.isBlank()) {
            throw new IllegalArgumentException("Informe uma palavra para traduzir");
        }

        String translated = deepLTranslationService.translate(original, language.getCode());

        TranslateResponseDTO response = new TranslateResponseDTO();
        response.setOriginal(original);
        response.setTranslated(translated);
        return response;
    }
}
