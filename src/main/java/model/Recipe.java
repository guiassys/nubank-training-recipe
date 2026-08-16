package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Recipe {

    private final String recipeId;
    private final String name;
    private final Map<String, Ingredient> ingredients;

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
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public boolean addIngredient(String ingredientId, int quantity) {
        if (ingredients.containsKey(ingredientId)) {
            return false;
        }
        ingredients.put(ingredientId, new Ingredient(ingredientId, quantity));
        return true;
    }

    public boolean updateIngredient(String ingredientId, int quantity) {
        Ingredient ingredient = ingredients.get(ingredientId);
        if (ingredient == null) {
            return false;
        }
        ingredient.setQuantity(quantity);
        return true;
    }

    public boolean removeIngredient(String ingredientId) {
        return ingredients.remove(ingredientId) != null;
    }

    public List<Ingredient> getIngredients() {
        return Collections.unmodifiableList(new ArrayList<>(ingredients.values()));
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
                '}';
    }
}
