package com.guiassys.nubank.training.recipe.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class EvaluationReport {

    private final String recipeId;
    private final int evaluationCount;
    private final long totalRating;
    private final double averageRating;
    private final int minimumRating;
    private final int maximumRating;
    private final long firstEvaluationTimestamp;
    private final long lastEvaluationTimestamp;

    public static final EvaluationReport EMPTY = EvaluationReport.builder().build();

}
