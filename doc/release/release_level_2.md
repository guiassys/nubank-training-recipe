# Release Notes - Level 2

## Summary

This release enhances the Recipe Management System with evaluation and ranking capabilities. Building on the core functionality of Level 1, the system now allows users to submit ratings for recipes and provides analytics based on this feedback.

The key architectural addition is the introduction of evaluation tracking and a Top-K ranking algorithm. This transforms the system from a simple data repository into a dynamic platform that can measure and highlight recipe quality and popularity.

### Implemented Features

- **Recipe Evaluation**: Users can now submit a rating (from 1 to 5) for any existing recipe. Each evaluation is uniquely identified and stored.
- **Rating Aggregation**: The system automatically calculates and maintains the average rating and total number of evaluations for each recipe.
- **Rating Summary**: A new feature allows retrieving a summary for any recipe, showing its total evaluations and average rating.
- **Top-K Recipe Ranking**: Implemented a `topRated(k)` function that returns the `k` best-rated recipes. The ranking is determined by average rating (descending), then by the number of evaluations (descending), and finally by recipe ID (ascending) for deterministic tie-breaking.
- **Duplicate Prevention**: The system now prevents duplicate evaluations by checking for unique evaluation IDs.

---

## Learnings & Notes

| Topic | Annotations |
|:----------------------|:--------------------|
| **Top-K Algorithm** | A `PriorityQueue` (min-heap) was used to implement the `topRated(k)` feature. This approach is highly efficient for finding the top `k` elements, with a time complexity of O(N log K), which is significantly better than sorting the entire collection (O(N log N)) when N is large and K is small. |
| **Custom Comparator** | A multi-level `Comparator` was created to handle the specified ranking logic. It prioritizes average rating, then the number of evaluations, and finally the recipe ID. This ensures the ranking is always stable and deterministic, as required by the specification. |
| **Data Integrity** | An additional `ConcurrentHashMap` was introduced to store all evaluations, indexed by their unique `evaluationId`. Using `putIfAbsent` ensures that even under concurrent conditions (in future levels), an evaluation can only be registered once, preventing data duplication and race conditions. |
| **Aggregate State** | The `Recipe` model was extended to maintain its own aggregate rating state (`totalRating` and `evaluationCount`). This avoids costly recalculations (e.g., iterating through all evaluations) every time an average is requested, making rating lookups an O(1) operation. |
| **Immutability** | The `RatingSummary` class was designed as an immutable value object. This is a good practice as it makes the data transfer object thread-safe and ensures the summary represents a consistent snapshot of the data at the time it was created. |
| **CodeSignal Standards** | The implementation of the Top-K algorithm and the deterministic tie-breaking logic aligns with the complexity and correctness standards expected in senior-level engineering assessments like those on CodeSignal. The focus was on both algorithmic efficiency and robust handling of edge cases. |