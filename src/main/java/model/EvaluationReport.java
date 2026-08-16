package model;

public final class EvaluationReport {

    private final String recipeId;
    private final int evaluationCount;
    private final long totalRating;
    private final double averageRating;
    private final int minimumRating;
    private final int maximumRating;
    private final long firstEvaluationTimestamp;
    private final long lastEvaluationTimestamp;

    public static final EvaluationReport EMPTY = new EvaluationReport(null, 0, 0, 0.0, 0, 0, -1, -1);

    public EvaluationReport(String recipeId, int evaluationCount, long totalRating, double averageRating, int minimumRating, int maximumRating, long firstEvaluationTimestamp, long lastEvaluationTimestamp) {
        this.recipeId = recipeId;
        this.evaluationCount = evaluationCount;
        this.totalRating = totalRating;
        this.averageRating = averageRating;
        this.minimumRating = minimumRating;
        this.maximumRating = maximumRating;
        this.firstEvaluationTimestamp = firstEvaluationTimestamp;
        this.lastEvaluationTimestamp = lastEvaluationTimestamp;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public int getEvaluationCount() {
        return evaluationCount;
    }

    public long getTotalRating() {
        return totalRating;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getMinimumRating() {
        return minimumRating;
    }

    public int getMaximumRating() {
        return maximumRating;
    }

    public long getFirstEvaluationTimestamp() {
        return firstEvaluationTimestamp;
    }

    public long getLastEvaluationTimestamp() {
        return lastEvaluationTimestamp;
    }
}
