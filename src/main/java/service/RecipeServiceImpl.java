package service;

import model.Evaluation;
import model.EvaluationReport;
import model.Ingredient;
import model.RatingSummary;
import model.Recipe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RecipeServiceImpl implements RecipeService {

    private final Map<String, Recipe> recipes = new ConcurrentHashMap<>();
    private final Map<String, Evaluation> evaluations = new ConcurrentHashMap<>();

    @Override
    public boolean createRecipe(String recipeId, String name) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipe ID cannot be null or blank.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipe name cannot be null or blank.");
        }
        return recipes.putIfAbsent(recipeId, new Recipe(recipeId, name)) == null;
    }

    @Override
    public Recipe getRecipe(String recipeId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipe ID cannot be null or blank.");
        }
        return recipes.get(recipeId);
    }

    @Override
    public boolean addIngredient(String recipeId, String ingredientId, int quantity) {
        Recipe recipe = getRecipe(recipeId);
        if (recipe == null) {
            return false;
        }
        return recipe.addIngredient(ingredientId, quantity);
    }

    @Override
    public boolean updateIngredient(String recipeId, String ingredientId, int quantity) {
        Recipe recipe = getRecipe(recipeId);
        if (recipe == null) {
            return false;
        }
        return recipe.updateIngredient(ingredientId, quantity);
    }

    @Override
    public boolean removeIngredient(String recipeId, String ingredientId) {
        if (ingredientId == null || ingredientId.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingredient ID cannot be null or blank.");
        }
        Recipe recipe = getRecipe(recipeId);
        if (recipe == null) {
            return false;
        }
        return recipe.removeIngredient(ingredientId);
    }

    @Override
    public List<Ingredient> listIngredients(String recipeId) {
        Recipe recipe = getRecipe(recipeId);
        if (recipe == null) {
            return Collections.emptyList();
        }
        return recipe.getIngredients();
    }

    @Override
    public boolean evaluateRecipe(String recipeId, String evaluationId, int rating, long timestamp) {
        Recipe recipe = getRecipe(recipeId);
        if (recipe == null) {
            return false;
        }
        Evaluation evaluation = new Evaluation(evaluationId, recipeId, rating, timestamp);
        // This block ensures that we only add the evaluation to the recipe's internal state
        // if it was successfully added to the global evaluations map.
        synchronized (recipe) {
            if (evaluations.putIfAbsent(evaluationId, evaluation) != null) {
                return false;
            }
            recipe.addEvaluation(evaluation);
        }
        return true;
    }

    @Override
    public RatingSummary getRating(String recipeId) {
        Recipe recipe = getRecipe(recipeId);
        if (recipe == null) {
            return new RatingSummary(0, 0.0);
        }
        return new RatingSummary(recipe.getEvaluationCount(), recipe.getAverageRating());
    }

    @Override
    public List<String> topRated(int k) {
        if (k <= 0) {
            return Collections.emptyList();
        }

        Comparator<Recipe> comparator = Comparator.comparing(Recipe::getAverageRating)
                .thenComparing(Recipe::getEvaluationCount)
                .thenComparing(Recipe::getRecipeId, Comparator.reverseOrder());

        PriorityQueue<Recipe> topK = new PriorityQueue<>(k, comparator);

        for (Recipe recipe : recipes.values()) {
            if (recipe.getEvaluationCount() > 0) {
                if (topK.size() < k) {
                    topK.offer(recipe);
                } else if (comparator.compare(recipe, topK.peek()) > 0) {
                    topK.poll();
                    topK.offer(recipe);
                }
            }
        }

        return topK.stream()
                .sorted(comparator.reversed())
                .map(Recipe::getRecipeId)
                .collect(Collectors.toList());
    }

    @Override
    public RatingSummary ratingInWindow(String recipeId, long startTimestamp, long endTimestamp) {
        EvaluationReport report = evaluationReport(recipeId, startTimestamp, endTimestamp);
        return new RatingSummary(report.getEvaluationCount(), report.getAverageRating());
    }

    @Override
    public EvaluationReport evaluationReport(String recipeId, long startTimestamp, long endTimestamp) {
        if (startTimestamp < 0 || endTimestamp < 0 || startTimestamp > endTimestamp) {
            throw new IllegalArgumentException("Invalid timestamp range.");
        }
        Recipe recipe = getRecipe(recipeId);
        if (recipe == null) {
            return EvaluationReport.EMPTY;
        }

        NavigableMap<Long, List<Evaluation>> window;
        synchronized (recipe) {
            window = recipe.getEvaluationHistory().subMap(startTimestamp, true, endTimestamp, true);
        }

        if (window.isEmpty()) {
            return EvaluationReport.EMPTY;
        }

        List<Evaluation> evalsInWindow = window.values().stream().flatMap(List::stream).collect(Collectors.toList());
        if (evalsInWindow.isEmpty()) {
            return EvaluationReport.EMPTY;
        }

        long totalRating = 0;
        int minRating = 6;
        int maxRating = 0;
        for (Evaluation eval : evalsInWindow) {
            totalRating += eval.getRating();
            if (eval.getRating() < minRating) minRating = eval.getRating();
            if (eval.getRating() > maxRating) maxRating = eval.getRating();
        }

        double averageRating = (double) totalRating / evalsInWindow.size();

        return EvaluationReport.builder()
                .recipeId(recipeId)
                .evaluationCount(evalsInWindow.size())
                .totalRating(totalRating)
                .averageRating(averageRating)
                .minimumRating(minRating)
                .maximumRating(maxRating)
                .firstEvaluationTimestamp(window.firstKey())
                .lastEvaluationTimestamp(window.lastKey())
                .build();
    }

    @Override
    public List<String> topEvaluated(int k, long startTimestamp, long endTimestamp) {
        if (k <= 0) {
            return Collections.emptyList();
        }

        Map<String, EvaluationReport> reports = new ConcurrentHashMap<>();
        recipes.keySet().forEach(recipeId -> {
            EvaluationReport report = evaluationReport(recipeId, startTimestamp, endTimestamp);
            if (report.getEvaluationCount() > 0) {
                reports.put(recipeId, report);
            }
        });

        Comparator<EvaluationReport> comparator = Comparator.comparing(EvaluationReport::getEvaluationCount)
                .thenComparing(EvaluationReport::getAverageRating)
                .thenComparing(EvaluationReport::getRecipeId, Comparator.reverseOrder());

        PriorityQueue<EvaluationReport> topK = new PriorityQueue<>(k, comparator);

        for (EvaluationReport report : reports.values()) {
            if (topK.size() < k) {
                topK.offer(report);
            } else if (comparator.compare(report, topK.peek()) > 0) {
                topK.poll();
                topK.offer(report);
            }
        }

        return topK.stream()
                .sorted(comparator.reversed())
                .map(EvaluationReport::getRecipeId)
                .collect(Collectors.toList());
    }
}
