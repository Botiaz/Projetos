package com.linguafy.services;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.linguafy.dto.SaveWordRequestDTO;
import com.linguafy.dto.TrainingProgressDTO;
import com.linguafy.dto.TrainingReviewRequestDTO;
import com.linguafy.dto.WordRequestDTO;
import com.linguafy.dto.WordResponseDTO;
import com.linguafy.entities.Category;
import com.linguafy.entities.Language;
import com.linguafy.entities.User;
import com.linguafy.entities.Word;
import com.linguafy.repositories.CategoryRepository;
import com.linguafy.repositories.LanguageRepository;
import com.linguafy.repositories.UserRepository;
import com.linguafy.repositories.WordRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class WordService {

    private final WordRepository wordRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;
    private final DeepLTranslationService deepLTranslationService;

    public WordService(
            WordRepository wordRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            LanguageRepository languageRepository,
            DeepLTranslationService deepLTranslationService) {

        this.wordRepository = wordRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.languageRepository = languageRepository;
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

    public List<WordResponseDTO> findMyWords(String email) {
        return wordRepository.findByUserEmail(email).stream().map(this::toResponse).toList();
    }

    public WordResponseDTO createForUser(String email, SaveWordRequestDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Language language = languageRepository.findById(dto.getLanguageId())
                .orElseThrow(() -> new EntityNotFoundException("Idioma não encontrado"));

        Category category = categoryRepository.findByLanguageIdAndName(language.getId(), "Geral")
                .orElseGet(() -> {
                    Category created = new Category();
                    created.setName("Geral");
                    created.setLanguage(language);
                    return categoryRepository.save(created);
                });

        Word word = new Word();
        word.setWord(dto.getWord());
        word.setTranslation(dto.getTranslation());
        word.setPronunciation(dto.getPronunciation());
        word.setAudioUrl(dto.getAudioUrl());
        word.setCategory(category);
        word.setUser(user);
        word.setReviewCount(0);
        word.setLearnedCount(0);

        Word saved = wordRepository.save(word);
        return toResponse(saved);
    }

    public void reviewWord(String email, TrainingReviewRequestDTO dto) {
        Word word = wordRepository.findById(dto.getWordId())
                .orElseThrow(() -> new EntityNotFoundException("Palavra não encontrada"));

        if (word.getUser() == null || !Objects.equals(word.getUser().getEmail(), email)) {
            throw new EntityNotFoundException("Palavra não encontrada para este usuário");
        }

        int reviews = word.getReviewCount() == null ? 0 : word.getReviewCount();
        int learned = word.getLearnedCount() == null ? 0 : word.getLearnedCount();

        word.setReviewCount(reviews + 1);
        word.setLastDifficulty(dto.getDifficulty());

        if (dto.getDifficulty() != null &&
                dto.getDifficulty().toLowerCase(Locale.ROOT).contains("fácil")) {
            word.setLearnedCount(learned + 1);
        }

        wordRepository.save(word);
    }

    public TrainingProgressDTO getProgress(String email) {
        List<Word> words = wordRepository.findByUserEmail(email);

        int totalLearned = words.stream()
                .mapToInt(word -> word.getLearnedCount() == null ? 0 : word.getLearnedCount())
                .sum();

        int totalReviewed = words.stream()
                .mapToInt(word -> word.getReviewCount() == null ? 0 : word.getReviewCount())
                .sum();

        int totalWords = words.size();

        int learnedWords = (int) words.stream()
                .filter(word -> (word.getLearnedCount() == null ? 0 : word.getLearnedCount()) > 0)
                .count();

        int progressPercent = totalWords == 0 ? 0 : (learnedWords * 100) / totalWords;

        TrainingProgressDTO dto = new TrainingProgressDTO();
        dto.setTotalLearned(totalLearned);
        dto.setTotalReviewed(totalReviewed);
        dto.setProgressPercent(progressPercent);
        dto.setEvolution(List.of(
                Math.max(0, progressPercent - 20),
                Math.max(0, progressPercent - 10),
                progressPercent
        ));

        return dto;
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

        wordRepository.delete(word);
    }

    private WordResponseDTO toResponse(Word word) {
        WordResponseDTO dto = new WordResponseDTO();
        dto.setId(word.getId());
        dto.setWord(word.getWord());
        dto.setTranslation(word.getTranslation());
        dto.setPronunciation(word.getPronunciation());
        dto.setAudioUrl(word.getAudioUrl());
        dto.setCategoryId(word.getCategory().getId());
        dto.setUserId(word.getUser() == null ? null : word.getUser().getId());
        dto.setReviewCount(word.getReviewCount() == null ? 0 : word.getReviewCount());
        dto.setLearnedCount(word.getLearnedCount() == null ? 0 : word.getLearnedCount());
        dto.setLastDifficulty(word.getLastDifficulty());
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