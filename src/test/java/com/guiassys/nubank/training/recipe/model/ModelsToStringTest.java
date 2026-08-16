package com.guiassys.nubank.training.recipe.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModelsToStringTest {

    @Test
    void testIngredientToString() {
        Ingredient ingredient = new Ingredient("ing1", 100);
        assertNotNull(ingredient.toString());
    }

    @Test
    void testEvaluationToString() {
        Evaluation evaluation = new Evaluation("eval1", "r1", 5, 1L);
        assertNotNull(evaluation.toString());
    }

    @Test
    void testRatingSummaryToString() {
        RatingSummary summary = new RatingSummary(10, 4.5);
        assertNotNull(summary.toString());
    }

    @Test
    void testRecipeToString() {
        Recipe recipe = new Recipe("r1", "Test Recipe");
        assertNotNull(recipe.toString());
    }

    @Test
    void testEvaluationReportToString() {
        // The EvaluationReport has no public constructor, so we rely on the service
        // to create it. We can test its toString via the service test, but for completeness,
        // we can call the package-private constructor if needed or just acknowledge this.
        // For now, let's assume the service test will cover its creation and usage.
        // A direct test would be:
        EvaluationReport report = new EvaluationReport("r1", 1, 5, 5.0, 5, 5, 1L, 1L);
        assertNotNull(report.toString());
    }
}
