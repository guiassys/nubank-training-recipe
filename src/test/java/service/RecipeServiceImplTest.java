package service;

import model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void allOperations_shouldThrowExceptionForNullOrBlankIds() {
        assertThrows(IllegalArgumentException.class, () -> recipeService.createRecipe(null, "name"));
        assertThrows(IllegalArgumentException.class, () -> recipeService.getRecipe(" "));
        assertThrows(IllegalArgumentException.class, () -> recipeService.addIngredient(null, "ing1", 1));
        assertThrows(IllegalArgumentException.class, () -> recipeService.updateIngredient(" ", "ing1", 1));
        assertThrows(IllegalArgumentException.class, () -> recipeService.removeIngredient(null, "ing1"));
        assertThrows(IllegalArgumentException.class, () -> recipeService.removeIngredient("id", " "));
        assertThrows(IllegalArgumentException.class, () -> recipeService.listIngredients(null));
    }
}
