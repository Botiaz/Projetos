package com.linguafy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linguafy.entities.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
