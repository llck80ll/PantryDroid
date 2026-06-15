package com.llck80ll.pantrydroid.model

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val name: String,
    val amount: String,
    val category: String
)

@Serializable
data class Nutrition(
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)

@Serializable
data class Recipe(
    val id: String,
    val name: String,
    val summary: String,
    val icon: String,
    val cuisine: String,
    val tags: List<String>,
    val prepMinutes: Int,
    val cookMinutes: Int,
    val servings: Int,
    val ingredients: List<Ingredient>,
    val steps: List<String>,
    val nutrition: Nutrition
) {
    val totalMinutes: Int get() = prepMinutes + cookMinutes
}

data class RecipeMatch(
    val recipe: Recipe,
    val owned: List<Ingredient>,
    val missing: List<Ingredient>
) {
    val percentage: Int
        get() = if (recipe.ingredients.isEmpty()) {
            0
        } else {
            ((owned.size.toDouble() / recipe.ingredients.size) * 100).toInt()
        }
}

@Serializable
enum class MealSlot(val label: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner")
}

@Serializable
data class PlannedMeal(
    val id: String,
    val recipeId: String,
    val dayOffset: Int,
    val slot: MealSlot
)

data class GroceryItem(
    val name: String,
    val category: String,
    val amounts: List<String>
)

