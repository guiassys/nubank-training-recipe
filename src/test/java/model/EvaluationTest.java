package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvaluationTest {

    @Test
    void constructor_shouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> new Evaluation(null, "r1", 5, 1L));
    }

    @Test
    void constructor_shouldThrowExceptionForBlankId() {
        assertThrows(IllegalArgumentException.class, () -> new Evaluation(" ", "r1", 5, 1L));
    }

    @Test
    void constructor_shouldThrowExceptionForRatingTooLow() {
        assertThrows(IllegalArgumentException.class, () -> new Evaluation("e1", "r1", 0, 1L));
    }

    @Test
    void constructor_shouldThrowExceptionForRatingTooHigh() {
        assertThrows(IllegalArgumentException.class, () -> new Evaluation("e1", "r1", 6, 1L));
    }

    @Test
    void constructor_shouldThrowExceptionForNegativeTimestamp() {
        assertThrows(IllegalArgumentException.class, () -> new Evaluation("e1", "r1", 5, -1L));
    }

    @Test
    void constructor_shouldCreateEvaluationForValidInput() {
        Evaluation eval = new Evaluation("e1", "r1", 5, 1L);
        assertEquals("e1", eval.getEvaluationId());
        assertEquals("r1", eval.getRecipeId());
        assertEquals(5, eval.getRating());
        assertEquals(1L, eval.getTimestamp());
    }
}
