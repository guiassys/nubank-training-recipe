package model;

import java.util.Objects;

public class Ingredient {

    private final String ingredientId;
    private int quantity;

    public Ingredient(String ingredientId, int quantity) {
        if (ingredientId == null || ingredientId.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingredient ID cannot be null or blank.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(ingredientId, that.ingredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredientId='" + ingredientId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
