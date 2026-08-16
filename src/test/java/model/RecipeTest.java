package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipe = new Recipe("recipe1", "Test Recipe");
    }

    @Test
    void constructor_shouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> new Recipe(null, "Test Recipe"));
    }

    @Test
    void constructor_shouldThrowExceptionForBlankId() {
        assertThrows(IllegalArgumentException.class, () -> new Recipe(" ", "Test Recipe"));
    }

    @Test
    void constructor_shouldThrowExceptionForNullName() {
        assertThrows(IllegalArgumentException.class, () -> new Recipe("recipe1", null));
    }

    @Test
    void constructor_shouldThrowExceptionForBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Recipe("recipe1", " "));
    }

    @Test
    void addIngredient_shouldReturnTrueForNewIngredient() {
        assertTrue(recipe.addIngredient("ing1", 100));
        assertEquals(1, recipe.getIngredients().size());
    }

    @Test
    void addIngredient_shouldReturnFalseForExistingIngredient() {
        recipe.addIngredient("ing1", 100);
        assertFalse(recipe.addIngredient("ing1", 200));
        assertEquals(1, recipe.getIngredients().size());
    }

    @Test
    void updateIngredient_shouldReturnTrueForExistingIngredient() {
        recipe.addIngredient("ing1", 100);
        assertTrue(recipe.updateIngredient("ing1", 200));
        assertEquals(200, recipe.getIngredients().get(0).getQuantity());
    }

    @Test
    void updateIngredient_shouldReturnFalseForNonExistingIngredient() {
        assertFalse(recipe.updateIngredient("ing1", 200));
    }

    @Test
    void removeIngredient_shouldReturnTrueForExistingIngredient() {
        recipe.addIngredient("ing1", 100);
        assertTrue(recipe.removeIngredient("ing1"));
        assertTrue(recipe.getIngredients().isEmpty());
    }

    @Test
    void removeIngredient_shouldReturnFalseForNonExistingIngredient() {
        assertFalse(recipe.removeIngredient("ing1"));
    }

    @Test
    void getIngredients_shouldReturnUnmodifiableList() {
        recipe.addIngredient("ing1", 100);
        var ingredients = recipe.getIngredients();
        assertThrows(UnsupportedOperationException.class, () -> ingredients.add(new Ingredient("ing2", 200)));
    }

    @Test
    void getAverageRating_shouldReturnZeroForNoEvaluations() {
        assertEquals(0.0, recipe.getAverageRating());
    }

    @Test
    void addEvaluation_shouldUpdateAverageRating() {
        recipe.addEvaluation(5);
        assertEquals(5.0, recipe.getAverageRating());
        recipe.addEvaluation(3);
        assertEquals(4.0, recipe.getAverageRating());
    }

    @Test
    void getEvaluationCount_shouldReturnCorrectCount() {
        assertEquals(0, recipe.getEvaluationCount());
        recipe.addEvaluation(5);
        assertEquals(1, recipe.getEvaluationCount());
    }
}
