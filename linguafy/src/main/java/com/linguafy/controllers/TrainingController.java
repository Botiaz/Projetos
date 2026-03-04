package com.linguafy.controllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linguafy.dto.TrainingProgressDTO;
import com.linguafy.dto.TrainingReviewRequestDTO;
import com.linguafy.services.WordService;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

    private final WordService wordService;

    public TrainingController(WordService wordService) {
        this.wordService = wordService;
    }

    @PostMapping("/review")
    public ResponseEntity<Void> review(@RequestBody TrainingReviewRequestDTO dto, Principal principal) {
        wordService.reviewWord(principal.getName(), dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/progress")
    public TrainingProgressDTO progress(Principal principal) {
        return wordService.getProgress(principal.getName());
    }
}