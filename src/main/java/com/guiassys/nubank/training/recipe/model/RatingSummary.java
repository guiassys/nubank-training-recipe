package com.guiassys.nubank.training.recipe.model;

public final class RatingSummary {

    private final int totalEvaluations;
    private final double averageRating;

    public RatingSummary(int totalEvaluations, double averageRating) {
        this.totalEvaluations = totalEvaluations;
        this.averageRating = averageRating;
    }

    public int getTotalEvaluations() {
        return totalEvaluations;
    }

    public double getAverageRating() {
        return averageRating;
    }

    @Override
    public String toString() {
        return "RatingSummary{" +
                "totalEvaluations=" + totalEvaluations +
                ", averageRating=" + averageRating +
                '}';
    }
}
