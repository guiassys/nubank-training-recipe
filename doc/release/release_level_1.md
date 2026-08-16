# Release Notes - Level 1

## Summary

This release establishes the foundational core of the Recipe Management System. The primary focus was on implementing the basic domain models and the essential CRUD (Create, Read, Update, Delete) operations for managing recipes and their ingredients.

The architecture follows clean design principles, with a clear separation between the domain model and the service layer. The implementation ensures data integrity through input validation and encapsulation, providing a solid and reliable base for future enhancements.

### Implemented Features

- **Recipe Creation**: Implemented the ability to create a new recipe with a unique identifier and a name.
- **Recipe Retrieval**: Added functionality to retrieve a recipe by its unique ID.
- **Ingredient Addition**: Users can now add ingredients with a specific quantity to an existing recipe.
- **Ingredient Modification**: The quantity of an existing ingredient within a recipe can be updated.
- **Ingredient Removal**: Implemented the ability to remove an ingredient from a recipe.
- **Ingredient Listing**: Provided a method to retrieve all ingredients associated with a specific recipe.
- **Input Validation**: Enforced strict validation for all identifiers, names, and quantities to maintain data consistency.
- **Encapsulation**: The internal state of domain models is protected to prevent uncontrolled modifications.

---

## Learnings & Notes

| Topic | Annotations |
|:----------------------|:--------------------|
| **Data Structures** | A `ConcurrentHashMap` was used for the main recipe storage to ensure thread-safety from the outset, anticipating future concurrency needs. A `LinkedHashMap` was chosen for storing ingredients within a recipe to maintain a deterministic insertion order. |
| **Domain-Driven Design** | The `Recipe` class acts as an aggregate root, encapsulating the logic for managing its own ingredients. This approach keeps the domain model rich and the service layer lean, aligning with DDD principles. |
| **Encapsulation** | To protect the internal state of the `Recipe` entity, the `listIngredients` method returns an unmodifiable view of the ingredients list. This prevents external clients from making uncontrolled changes. |
| **API Contract** | The `RecipeService` interface defines a clear and concise contract for all available operations. The implementation uses `IllegalArgumentException` to signal violations of these contracts, such as providing null or blank identifiers. |
| **Testing** | Comprehensive unit tests were created for both the domain models and the service layer. The tests cover all specified success paths, failure conditions, and edge cases, ensuring the implementation is robust and correct according to the specification. |
| **Failure Atomicity** | Operations are designed to be atomic. For instance, if adding an ingredient fails, the recipe's state remains unchanged. This was achieved by validating conditions before applying state changes. |