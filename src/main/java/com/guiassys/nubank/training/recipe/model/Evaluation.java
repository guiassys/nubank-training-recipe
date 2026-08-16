package com.guiassys.nubank.training.recipe.model;

import java.util.Objects;

public class Evaluation {

    private final String evaluationId;
    private final String recipeId;
    private final int rating;
    private final long timestamp;

    public Evaluation(String evaluationId, String recipeId, int rating, long timestamp) {
        if (evaluationId == null || evaluationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Evaluation ID cannot be null or blank.");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (timestamp < 0) {
            throw new IllegalArgumentException("Timestamp cannot be negative.");
        }
        this.evaluationId = evaluationId;
        this.recipeId = recipeId;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public String getEvaluationId() {
        return evaluationId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public int getRating() {
        return rating;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evaluation that = (Evaluation) o;
        return Objects.equals(evaluationId, that.evaluationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evaluationId);
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "evaluationId='" + evaluationId + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", rating=" + rating +
                ", timestamp=" + timestamp +
                '}';
    }
}
