package com.linguafy.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.linguafy.dto.LanguageRequestDTO;
import com.linguafy.dto.LanguageResponseDTO;
import com.linguafy.entities.Language;
import com.linguafy.repositories.LanguageRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public List<LanguageResponseDTO> findAll() {
        return languageRepository.findAll().stream().map(this::toResponse).toList();
    }

    public LanguageResponseDTO findById(Long id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Idioma não encontrado: " + id));
        return toResponse(language);
    }

    public LanguageResponseDTO create(LanguageRequestDTO dto) {
        Language language = new Language();
        language.setName(dto.getName());
        language.setCode(dto.getCode());

        Language saved = languageRepository.save(language);
        return toResponse(saved);
    }

    public LanguageResponseDTO update(Long id, LanguageRequestDTO dto) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Idioma não encontrado: " + id));

        language.setName(dto.getName());
        language.setCode(dto.getCode());

        Language updated = languageRepository.save(language);
        return toResponse(updated);
    }

    public void delete(Long id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Idioma não encontrado: " + id));
        languageRepository.delete(language);
    }

    private LanguageResponseDTO toResponse(Language language) {
        LanguageResponseDTO dto = new LanguageResponseDTO();
        dto.setId(language.getId());
        dto.setName(language.getName());
        dto.setCode(language.getCode());
        return dto;
    }
}
