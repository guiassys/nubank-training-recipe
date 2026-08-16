package service;

import model.RatingSummary;
import model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeServiceImplTest {

    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeServiceImpl();
    }

    @Test
    void createRecipe_shouldReturnTrueForNewRecipe() {
        assertTrue(recipeService.createRecipe("recipe1", "Test Recipe"));
    }

    @Test
    void createRecipe_shouldReturnFalseForExistingRecipe() {
        recipeService.createRecipe("recipe1", "Test Recipe");
        assertFalse(recipeService.createRecipe("recipe1", "Another Recipe"));
    }

    @Test
    void getRecipe_shouldReturnRecipeForExistingId() {
        recipeService.createRecipe("recipe1", "Test Recipe");
        Recipe recipe = recipeService.getRecipe("recipe1");
        assertNotNull(recipe);
        assertEquals("recipe1", recipe.getRecipeId());
    }

    @Test
    void getRecipe_shouldReturnNullForNonExistingId() {
        assertNull(recipeService.getRecipe("non-existing"));
    }

    @Test
    void addIngredient_shouldReturnTrueWhenAdded() {
        recipeService.createRecipe("recipe1", "Test Recipe");
        assertTrue(recipeService.addIngredient("recipe1", "ing1", 100));
    }

    @Test
    void addIngredient_shouldReturnFalseForNonExistingRecipe() {
        assertFalse(recipeService.addIngredient("non-existing", "ing1", 100));
    }

    @Test
    void updateIngredient_shouldReturnTrueWhenUpdated() {
        recipeService.createRecipe("recipe1", "Test Recipe");
        recipeService.addIngredient("recipe1", "ing1", 100);
        assertTrue(recipeService.updateIngredient("recipe1", "ing1", 200));
    }

    @Test
    void updateIngredient_shouldReturnFalseForNonExistingRecipe() {
        assertFalse(recipeService.updateIngredient("non-existing", "ing1", 100));
    }

    @Test
    void removeIngredient_shouldReturnTrueWhenRemoved() {
        recipeService.createRecipe("recipe1", "Test Recipe");
        recipeService.addIngredient("recipe1", "ing1", 100);
        assertTrue(recipeService.removeIngredient("recipe1", "ing1"));
    }

    @Test
    void removeIngredient_shouldReturnFalseForNonExistingRecipe() {
        assertFalse(recipeService.removeIngredient("non-existing", "ing1"));
    }

    @Test
    void listIngredients_shouldReturnIngredientsForExistingRecipe() {
        recipeService.createRecipe("recipe1", "Test Recipe");
        recipeService.addIngredient("recipe1", "ing1", 100);
        assertEquals(1, recipeService.listIngredients("recipe1").size());
    }

    @Test
    void listIngredients_shouldReturnEmptyListForNonExistingRecipe() {
        assertTrue(recipeService.listIngredients("non-existing").isEmpty());
    }

    @Test
    void evaluateRecipe_shouldReturnTrueForNewEvaluation() {
        recipeService.createRecipe("recipe1", "Test Recipe");
        assertTrue(recipeService.evaluateRecipe("recipe1", "eval1", 5, 1L));
    }

    @Test
    void evaluateRecipe_shouldReturnFalseForExistingEvaluationId() {
        recipeService.createRecipe("recipe1", "Test Recipe");
        recipeService.evaluateRecipe("recipe1", "eval1", 5, 1L);
        assertFalse(recipeService.evaluateRecipe("recipe1", "eval1", 3, 2L));
    }

    @Test
    void evaluateRecipe_shouldReturnFalseForNonExistingRecipe() {
        assertFalse(recipeService.evaluateRecipe("non-existing", "eval1", 5, 1L));
    }

    @Test
    void getRating_shouldReturnCorrectSummary() {
        recipeService.createRecipe("recipe1", "Test Recipe");
        recipeService.evaluateRecipe("recipe1", "eval1", 5, 1L);
        recipeService.evaluateRecipe("recipe1", "eval2", 3, 2L);
        RatingSummary summary = recipeService.getRating("recipe1");
        assertEquals(2, summary.getTotalEvaluations());
        assertEquals(4.0, summary.getAverageRating());
    }

    @Test
    void getRating_shouldReturnEmptySummaryForNonExistingRecipe() {
        RatingSummary summary = recipeService.getRating("non-existing");
        assertEquals(0, summary.getTotalEvaluations());
        assertEquals(0.0, summary.getAverageRating());
    }

    @Test
    void topRated_shouldReturnEmptyListForZeroK() {
        assertTrue(recipeService.topRated(0).isEmpty());
    }

    @Test
    void topRated_shouldReturnCorrectlyOrderedRecipes() {
        recipeService.createRecipe("recipe-b", "B"); // avg 5.0, 1 eval
        recipeService.evaluateRecipe("recipe-b", "e1", 5, 1L);

        recipeService.createRecipe("recipe-a", "A"); // avg 4.5, 2 evals
        recipeService.evaluateRecipe("recipe-a", "e2", 5, 2L);
        recipeService.evaluateRecipe("recipe-a", "e3", 4, 3L);
        
        recipeService.createRecipe("recipe-c", "C"); // avg 4.5, 1 eval
        recipeService.evaluateRecipe("recipe-c", "e4", 4, 4L);
        recipeService.getRecipe("recipe-c").addEvaluation(5);


        List<String> top2 = recipeService.topRated(2);
        assertEquals(2, top2.size());
        assertEquals("recipe-b", top2.get(0)); // Higher avg rating
        assertEquals("recipe-a", top2.get(1)); // Same avg as C, but more evals
    }
    
    @Test
    void topRated_shouldHandleTieBreakingWithId() {
        recipeService.createRecipe("recipe-b", "B"); // avg 5, 1 eval
        recipeService.evaluateRecipe("recipe-b", "e1", 5, 1L);

        recipeService.createRecipe("recipe-a", "A"); // avg 5, 1 eval
        recipeService.evaluateRecipe("recipe-a", "e2", 5, 2L);

        List<String> top2 = recipeService.topRated(2);
        assertEquals(2, top2.size());
        assertEquals("recipe-a", top2.get(0)); // Lower recipeId
        assertEquals("recipe-b", top2.get(1));
    }
}
