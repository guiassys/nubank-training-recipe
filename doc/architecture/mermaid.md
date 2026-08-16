# Mermaid Diagrams

This document contains Mermaid diagrams illustrating the flow of the main algorithms in the Recipe Management System.

## Flow - createRecipe
```mermaid
flowchart TD
    Start([Start: createRecipe recipeId, name]) --> CheckExists{"Recipe already exists?\nrecipes.putIfAbsent(recipeId, ...)"}
    
    CheckExists -- Yes (already present) --> ReturnFalse[Return false]
    CheckExists -- No (was absent) --> CreateRecipe[Create new Recipe object]
    
    CreateRecipe --> PutInMap["Add to 'recipes' map"]
    PutInMap --> ReturnTrue[Return true]

    ReturnFalse --> End([End])
    ReturnTrue --> End
```
---

## Flow - evaluateRecipe
```mermaid
flowchart TD
    Start([Start: evaluateRecipe recipeId, evalId, ...]) --> GetRecipe["Find recipe by ID\ngetRecipe(recipeId)"]
    
    GetRecipe --> CheckRecipe{Recipe exists?}
    CheckRecipe -- No --> ReturnFalse[Return false]
    CheckRecipe -- Yes --> CreateEvaluation[Create new Evaluation object]
    
    CreateEvaluation --> CheckEvalExists{"Evaluation already exists?\nevaluations.putIfAbsent(evalId, ...)"}
    CheckEvalExists -- Yes (already present) --> ReturnFalse
    CheckEvalExists -- No (was absent) --> AddToRecipe["Add evaluation to recipe\nrecipe.addEvaluation(evaluation)"]
    
    AddToRecipe --> ReturnTrue[Return true]
    
    ReturnFalse --> End([End])
    ReturnTrue --> End
```
---

## Algorithm - topRated(k)
```mermaid
flowchart TD
    Start([Start: topRated k]) --> InitHeap["Initialize a min-PriorityQueue of size k\n(with custom comparator)"]
    
    InitHeap --> LoopRecipes{"Loop through all recipes in 'recipes' map"}
    LoopRecipes -- For each recipe --> CheckEvaluations{"Recipe has evaluations?\nrecipe.getEvaluationCount() > 0"}
    
    CheckEvaluations -- No --> LoopRecipes
    CheckEvaluations -- Yes --> CheckHeapSize{"Is heap size < k?"}
    
    CheckHeapSize -- Yes --> AddToHeap["Add recipe to heap\n topK.offer(recipe)"]
    AddToHeap --> LoopRecipes
    
    CheckHeapSize -- No --> CompareWithHeapPeek{"Is recipe's rating > heap's lowest rating?\ncomparator.compare(recipe, topK.peek()) > 0"}
    CompareWithHeapPeek -- No --> LoopRecipes
    CompareWithHeapPeek -- Yes --> RemoveLowest["Remove lowest rated recipe from heap\ntopK.poll()"]
    RemoveLowest --> AddToHeap
    
    LoopRecipes -- End of loop --> SortHeap["Sort the final k recipes in descending order"]
    SortHeap --> MapToId["Map sorted recipes to their IDs"]
    MapToId --> ReturnList[Return list of recipe IDs]
    ReturnList --> End([End])
```
---

## Algorithm - evaluationReport
```mermaid
flowchart TD
    Start([Start: evaluationReport recipeId, start, end]) --> ValidateTimestamps{"Are timestamps valid?\nstart <= end"}
    ValidateTimestamps -- No --> ThrowException[Throw IllegalArgumentException]
    ValidateTimestamps -- Yes --> GetRecipe["Find recipe by ID\ngetRecipe(recipeId)"]
    
    GetRecipe --> CheckRecipe{Recipe exists?}
    CheckRecipe -- No --> ReturnEmptyReport[Return EvaluationReport.EMPTY]
    
    CheckRecipe -- Yes --> GetHistory["Get recipe's evaluation history (TreeMap)"]
    GetHistory --> QuerySubMap["Get view of evaluations in time window\nhistory.subMap(start, true, end, true)"]
    
    QuerySubMap --> CheckWindow{Is window empty?}
    CheckWindow -- Yes --> ReturnEmptyReport
    
    CheckWindow -- No --> ProcessWindow{"Iterate through evaluations in the window"}
    ProcessWindow --> CalculateStats["Calculate count, total, average, min, and max ratings"]
    CalculateStats --> CreateReport["Create new EvaluationReport object with stats"]
    CreateReport --> ReturnReport[Return EvaluationReport]
    
    ThrowException --> End([End])
    ReturnEmptyReport --> End
    ReturnReport --> End
```
---