package com.guiassys.nubank.training.recipe.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

public class Recipe {

    private final String recipeId;
    private final String name;
    private final Map<String, Ingredient> ingredients;
    private long totalRating;
    private int evaluationCount;
    private final NavigableMap<Long, List<Evaluation>> evaluationHistory;

    public Recipe(String recipeId, String name) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipe ID cannot be null or blank.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipe name cannot be null or blank.");
        }
        this.recipeId = recipeId;
        this.name = name;
        this.ingredients = new LinkedHashMap<>();
        this.totalRating = 0;
        this.evaluationCount = 0;
        this.evaluationHistory = new TreeMap<>();
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public synchronized boolean addIngredient(String ingredientId, int quantity) {
        if (ingredients.containsKey(ingredientId)) {
            return false;
        }
        ingredients.put(ingredientId, new Ingredient(ingredientId, quantity));
        return true;
    }

    public synchronized boolean updateIngredient(String ingredientId, int quantity) {
        Ingredient ingredient = ingredients.get(ingredientId);
        if (ingredient == null) {
            return false;
        }
        ingredient.setQuantity(quantity);
        return true;
    }

    public synchronized boolean removeIngredient(String ingredientId) {
        return ingredients.remove(ingredientId) != null;
    }

    public synchronized List<Ingredient> getIngredients() {
        return Collections.unmodifiableList(new ArrayList<>(ingredients.values()));
    }

    public synchronized void addEvaluation(Evaluation evaluation) {
        this.totalRating += evaluation.getRating();
        this.evaluationCount++;
        this.evaluationHistory
                .computeIfAbsent(evaluation.getTimestamp(), k -> new ArrayList<>())
                .add(evaluation);
    }

    public synchronized double getAverageRating() {
        if (evaluationCount == 0) {
            return 0.0;
        }
        return (double) totalRating / evaluationCount;
    }

    public synchronized int getEvaluationCount() {
        return evaluationCount;
    }

    public synchronized NavigableMap<Long, List<Evaluation>> getEvaluationHistory() {
        return new TreeMap<>(evaluationHistory);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(recipeId, recipe.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId='" + recipeId + '\'' +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients.size() +
                ", averageRating=" + getAverageRating() +
                '}';
    }
}
