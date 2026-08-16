package service;

import model.EvaluationReport;
import model.Ingredient;
import model.RatingSummary;
import model.Recipe;

import java.util.List;

public interface RecipeService {

    boolean createRecipe(String recipeId, String name);

    Recipe getRecipe(String recipeId);

    boolean addIngredient(
        String recipeId,
        String ingredientId,
        int quantity
    );

    boolean updateIngredient(
        String recipeId,
        String ingredientId,
        int quantity
    );

    boolean removeIngredient(
        String recipeId,
        String ingredientId
    );

    List<Ingredient> listIngredients(String recipeId);

    boolean evaluateRecipe(
        String recipeId,
        String evaluationId,
        int rating,
        long timestamp
    );

    RatingSummary getRating(String recipeId);

    List<String> topRated(int k);

    RatingSummary ratingInWindow(
        String recipeId,
        long startTimestamp,
        long endTimestamp
    );

    EvaluationReport evaluationReport(
        String recipeId,
        long startTimestamp,
        long endTimestamp
    );

    List<String> topEvaluated(
        int k,
        long startTimestamp,
        long endTimestamp
    );
}
