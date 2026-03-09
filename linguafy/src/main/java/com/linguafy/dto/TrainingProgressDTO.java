package com.linguafy.dto;

import java.util.List;

public class TrainingProgressDTO {

    private int totalLearned;
    private int totalReviewed;
    private int progressPercent;
    private List<Integer> evolution;

    public int getTotalLearned() {
        return totalLearned;
    }

    public void setTotalLearned(int totalLearned) {
        this.totalLearned = totalLearned;
    }

    public int getTotalReviewed() {
        return totalReviewed;
    }

    public void setTotalReviewed(int totalReviewed) {
        this.totalReviewed = totalReviewed;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    public List<Integer> getEvolution() {
        return evolution;
    }

    public void setEvolution(List<Integer> evolution) {
        this.evolution = evolution;
    }
}
