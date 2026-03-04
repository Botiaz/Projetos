package com.linguafy.services;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.linguafy.dto.WordRequestDTO;
import com.linguafy.dto.WordResponseDTO;
import com.linguafy.entities.Category;
import com.linguafy.entities.Word;
import com.linguafy.repositories.CategoryRepository;
import com.linguafy.repositories.WordRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class WordService {

    private final WordRepository wordRepository;
    private final CategoryRepository categoryRepository;
    private final DeepLTranslationService deepLTranslationService;

    public WordService(WordRepository wordRepository,
            CategoryRepository categoryRepository,
            DeepLTranslationService deepLTranslationService) {
        this.wordRepository = wordRepository;
        this.categoryRepository = categoryRepository;
        this.deepLTranslationService = deepLTranslationService;
    }

    public List<WordResponseDTO> findAll() {
        return wordRepository.findAll().stream().map(this::toResponse).toList();
    }

    public WordResponseDTO findById(Long id) {
        Word word = wordRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new EntityNotFoundException("Palavra não encontrada: " + id));
        return toResponse(word);
    }

    public WordResponseDTO create(WordRequestDTO dto) {
        Category category = categoryRepository.findById(Objects.requireNonNull(dto.getCategoryId()))
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + dto.getCategoryId()));

        Word word = new Word();
        word.setWord(dto.getWord());
        word.setTranslation(resolveTranslation(dto, category));
        word.setPronunciation(dto.getPronunciation());
        word.setAudioUrl(dto.getAudioUrl());
        word.setCategory(category);

        Word saved = wordRepository.save(word);
        return toResponse(saved);
    }

    public WordResponseDTO update(Long id, WordRequestDTO dto) {
        Word word = wordRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new EntityNotFoundException("Palavra não encontrada: " + id));

        Category category = categoryRepository.findById(Objects.requireNonNull(dto.getCategoryId()))
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + dto.getCategoryId()));

        word.setWord(dto.getWord());
        word.setTranslation(resolveTranslation(dto, category));
        word.setPronunciation(dto.getPronunciation());
        word.setAudioUrl(dto.getAudioUrl());
        word.setCategory(category);

        Word updated = wordRepository.save(word);
        return toResponse(updated);
    }

    public void delete(Long id) {
        Word word = wordRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new EntityNotFoundException("Palavra não encontrada: " + id));
        wordRepository.delete(Objects.requireNonNull(word));
    }

    private WordResponseDTO toResponse(Word word) {
        WordResponseDTO dto = new WordResponseDTO();
        dto.setId(word.getId());
        dto.setWord(word.getWord());
        dto.setTranslation(word.getTranslation());
        dto.setPronunciation(word.getPronunciation());
        dto.setAudioUrl(word.getAudioUrl());
        dto.setCategoryId(word.getCategory().getId());
        return dto;
    }

    private String resolveTranslation(WordRequestDTO dto, Category category) {
        if (dto.getTranslation() != null && !dto.getTranslation().isBlank()) {
            return dto.getTranslation();
        }

        String sourceLangCode = null;
        if (category.getLanguage() != null) {
            sourceLangCode = category.getLanguage().getCode();
        }

        return deepLTranslationService.translate(dto.getWord(), sourceLangCode);
    }
}
