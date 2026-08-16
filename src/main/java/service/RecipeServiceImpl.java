package service;

import model.Ingredient;
import model.Recipe;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RecipeServiceImpl implements RecipeService {

    private final Map<String, Recipe> recipes = new ConcurrentHashMap<>();

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
}
