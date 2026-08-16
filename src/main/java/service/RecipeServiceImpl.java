package service;

import model.Evaluation;
import model.Ingredient;
import model.RatingSummary;
import model.Recipe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
        if (evaluations.putIfAbsent(evaluationId, evaluation) != null) {
            return false;
        }
        recipe.addEvaluation(rating);
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
}
