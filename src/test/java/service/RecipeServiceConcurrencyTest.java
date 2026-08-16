package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeServiceConcurrencyTest {

    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeServiceImpl();
    }

    @Test
    void createRecipe_shouldBeAtomic() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger successCount = new AtomicInteger(0);
        int numThreads = 10;

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                if (recipeService.createRecipe("recipe1", "Concurrent Recipe")) {
                    successCount.incrementAndGet();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(1, successCount.get());
        assertNotNull(recipeService.getRecipe("recipe1"));
    }

    @Test
    void evaluateRecipe_shouldBeAtomic() throws InterruptedException {
        recipeService.createRecipe("recipe1", "Test Recipe");
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger successCount = new AtomicInteger(0);
        int numThreads = 10;

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                if (recipeService.evaluateRecipe("recipe1", "eval1", 5, 1L)) {
                    successCount.incrementAndGet();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(1, successCount.get());
        assertEquals(1, recipeService.getRating("recipe1").getTotalEvaluations());
    }

    @Test
    void concurrentEvaluations_shouldMaintainConsistentState() throws InterruptedException {
        recipeService.createRecipe("recipe1", "Test Recipe");
        ExecutorService executor = Executors.newFixedThreadPool(100);
        int numEvaluations = 1000;

        for (int i = 0; i < numEvaluations; i++) {
            final int evalId = i;
            executor.submit(() -> {
                recipeService.evaluateRecipe("recipe1", "eval" + evalId, 4, evalId);
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(numEvaluations, recipeService.getRating("recipe1").getTotalEvaluations());
        assertEquals(4.0, recipeService.getRating("recipe1").getAverageRating());
    }

    @Test
    void concurrentReadAndWrite_shouldNotThrowException() throws InterruptedException {
        recipeService.createRecipe("recipe1", "Test Recipe");
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Writer thread
        executor.submit(() -> {
            for (int i = 0; i < 1000; i++) {
                recipeService.evaluateRecipe("recipe1", "eval" + i, 5, i);
            }
        });

        // Reader thread
        executor.submit(() -> {
            for (int i = 0; i < 1000; i++) {
                recipeService.evaluationReport("recipe1", 0, i);
            }
        });

        executor.shutdown();
        assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));
    }
}
