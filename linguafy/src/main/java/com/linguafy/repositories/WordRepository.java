package com.linguafy.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linguafy.entities.Word;

public interface WordRepository extends JpaRepository<Word, Long> {
	List<Word> findByUserEmail(String email);
}
