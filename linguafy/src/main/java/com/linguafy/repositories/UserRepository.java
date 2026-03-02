package com.linguafy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linguafy.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
