package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    @Test
    void constructor_shouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> new Ingredient(null, 100));
    }

    @Test
    void constructor_shouldThrowExceptionForBlankId() {
        assertThrows(IllegalArgumentException.class, () -> new Ingredient(" ", 100));
    }

    @Test
    void constructor_shouldThrowExceptionForZeroQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new Ingredient("ing1", 0));
    }

    @Test
    void constructor_shouldThrowExceptionForNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new Ingredient("ing1", -1));
    }

    @Test
    void setQuantity_shouldThrowExceptionForZeroQuantity() {
        Ingredient ingredient = new Ingredient("ing1", 100);
        assertThrows(IllegalArgumentException.class, () -> ingredient.setQuantity(0));
    }

    @Test
    void setQuantity_shouldThrowExceptionForNegativeQuantity() {
        Ingredient ingredient = new Ingredient("ing1", 100);
        assertThrows(IllegalArgumentException.class, () -> ingredient.setQuantity(-1));
    }

    @Test
    void equals_shouldReturnTrueForSameId() {
        Ingredient ing1 = new Ingredient("ing1", 100);
        Ingredient ing2 = new Ingredient("ing1", 200);
        assertEquals(ing1, ing2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentId() {
        Ingredient ing1 = new Ingredient("ing1", 100);
        Ingredient ing2 = new Ingredient("ing2", 100);
        assertNotEquals(ing1, ing2);
    }

    @Test
    void equals_shouldReturnFalseForNull() {
        Ingredient ing1 = new Ingredient("ing1", 100);
        assertNotEquals(ing1, null);
    }

    @Test
    void equals_shouldReturnFalseForDifferentClass() {
        Ingredient ing1 = new Ingredient("ing1", 100);
        assertNotEquals(ing1, new Object());
    }

    @Test
    void hashCode_shouldBeSameForSameId() {
        Ingredient ing1 = new Ingredient("ing1", 100);
        Ingredient ing2 = new Ingredient("ing1", 200);
        assertEquals(ing1.hashCode(), ing2.hashCode());
    }
}
