# Release Notes - Level 3

## Summary

This release introduces historical and time-based analysis capabilities to the Recipe Management System. Building upon the evaluation features of Level 2, the system now preserves the full history of every evaluation, allowing for powerful temporal queries and trend analysis.

The key architectural change is the integration of an ordered data structure (`TreeMap`) to store evaluation history, enabling efficient time-window queries. This enhancement transforms the system into a business intelligence tool capable of providing insights into recipe performance over specific periods.

### Implemented Features

- **Historical Evaluation Storage**: All evaluations are now stored with a timestamp in a time-ordered data structure within each recipe.
- **Time-Window Queries**: Implemented `ratingInWindow` to calculate the average rating and total evaluations for a recipe within a specified `[startTimestamp, endTimestamp]` range.
- **Detailed Evaluation Reports**: A new `evaluationReport` feature provides a comprehensive report for a recipe in a time window, including count, average, total, min/max ratings, and first/last evaluation timestamps.
- **Time-Based Ranking**: Added a `topEvaluated(k, start, end)` function to rank recipes based on the number of evaluations they received within a specific time window. The ranking uses evaluation count, then average rating, and finally recipe ID as tie-breakers.
- **Efficient Range Queries**: The implementation uses `NavigableMap.subMap` to ensure that time-window queries are efficient, avoiding a full scan of a recipe's entire evaluation history.

---

## Learnings & Notes

| Topic | Annotations |
|:----------------------|:--------------------|
| **Temporal Data Structures** | A `TreeMap` was chosen to store evaluation history, with the timestamp as the key. This `NavigableMap` implementation is ideal for temporal queries, as its `subMap` method provides an efficient way to retrieve a view of the data within a specific time range in O(log N) time. |
| **Range Query Efficiency** | The core of the historical query implementation relies on `subMap(start, true, end, true)`. This is a critical optimization that prevents iterating over the entire set of evaluations, making the performance of windowed queries dependent on the size of the window (V) and the cost of the initial lookup (log N), i.e., O(log N + V). |
| **Handling Multiple Events at Same Timestamp** | The `evaluationHistory` map was designed as `NavigableMap<Long, List<Evaluation>>`. This correctly handles the edge case where multiple evaluations occur at the exact same timestamp by storing them in a list under a single timestamp key. |
| **Composite Data Aggregation** | The `evaluationReport` feature demonstrates how to aggregate multiple metrics (count, sum, min, max, etc.) from a data stream. The logic iterates through the evaluations within the specified window just once to calculate all required statistics. |
| **Immutability for Reports** | The `EvaluationReport` and `RatingSummary` classes are immutable. This is particularly important for reporting features, as it ensures that the generated report is a consistent, thread-safe snapshot of the data at a specific point in time. An `EMPTY` static final instance was added to `EvaluationReport` to avoid creating new empty objects. |
| **CodeSignal Standards** | The implementation of efficient range queries using ordered maps and the application of the Top-K pattern to a new set of time-based criteria are common challenges in advanced algorithmic assessments. This level's implementation demonstrates proficiency in handling such temporal data problems. |