package com.linguafy.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.linguafy.dto.CategoryRequestDTO;
import com.linguafy.dto.CategoryResponseDTO;
import com.linguafy.entities.Category;
import com.linguafy.entities.Language;
import com.linguafy.repositories.CategoryRepository;
import com.linguafy.repositories.LanguageRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;

    public CategoryService(CategoryRepository categoryRepository, LanguageRepository languageRepository) {
        this.categoryRepository = categoryRepository;
        this.languageRepository = languageRepository;
    }

    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));
        return toResponse(category);
    }

    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        Language language = languageRepository.findById(dto.getLanguageId())
                .orElseThrow(() -> new EntityNotFoundException("Idioma não encontrado: " + dto.getLanguageId()));

        Category category = new Category();
        category.setName(dto.getName());
        category.setLanguage(language);

        Category saved = categoryRepository.save(category);
        return toResponse(saved);
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));

        Language language = languageRepository.findById(dto.getLanguageId())
                .orElseThrow(() -> new EntityNotFoundException("Idioma não encontrado: " + dto.getLanguageId()));

        category.setName(dto.getName());
        category.setLanguage(language);

        Category updated = categoryRepository.save(category);
        return toResponse(updated);
    }

    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));
        categoryRepository.delete(category);
    }

    private CategoryResponseDTO toResponse(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setLanguageId(category.getLanguage().getId());
        return dto;
    }
}
