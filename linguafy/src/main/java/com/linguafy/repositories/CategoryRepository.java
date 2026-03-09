package com.linguafy.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linguafy.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByLanguageIdAndName(Long languageId, String name);
}
