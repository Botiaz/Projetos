package com.linguafy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linguafy.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
