package com.linguafy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linguafy.entities.Word;

public interface WordRepository extends JpaRepository<Word, Long> {
}
