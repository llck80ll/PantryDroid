package com.llck80ll.pantrydroid

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.llck80ll.pantrydroid.data.MatchingEngine
import com.llck80ll.pantrydroid.data.PantryPreferences
import com.llck80ll.pantrydroid.data.RecipeRepository
import com.llck80ll.pantrydroid.model.GroceryItem
import com.llck80ll.pantrydroid.model.MealSlot
import com.llck80ll.pantrydroid.model.PlannedMeal
import com.llck80ll.pantrydroid.model.Recipe
import java.util.UUID

class PantryViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = PantryPreferences(application)

    val recipes = RecipeRepository(application).load()
    var pantry by mutableStateOf(preferences.loadPantry())
        private set
    var favoriteIds by mutableStateOf(preferences.loadFavorites())
        private set
    var plannedMeals by mutableStateOf(preferences.loadPlan())
        private set
    var checkedGroceries by mutableStateOf(preferences.loadCheckedGroceries())
        private set
    var selectedCuisine by mutableStateOf<String?>(null)
    var selectedRecipe by mutableStateOf<Recipe?>(null)
    var strictMatch by mutableStateOf(false)
    var servings by mutableStateOf(4)

    val cuisines: List<String> get() = recipes.map { it.cuisine }.distinct().sorted()
    val matches
        get() = MatchingEngine
            .ranked(recipes, pantry, selectedCuisine)
            .let { list -> if (strictMatch) list.filter { it.percentage >= 90 } else list }
    val favorites get() = recipes.filter { it.id in favoriteIds }

    val groceryItems: List<GroceryItem>
        get() {
            val ingredients = plannedMeals
                .mapNotNull { meal -> recipes.firstOrNull { it.id == meal.recipeId } }
                .flatMap { it.ingredients }
                .filter { ingredient ->
                    pantry.none { MatchingEngine.namesMatch(it, ingredient.name) }
                }
            return ingredients
                .groupBy { it.name }
                .map { (name, items) ->
                    GroceryItem(
                        name = name,
                        category = items.first().category,
                        amounts = items.map { it.amount }
                    )
                }
                .sortedWith(compareBy<GroceryItem> { it.category }.thenBy { it.name })
        }

    fun togglePantry(item: String) {
        pantry = if (item in pantry) pantry - item else pantry + item
        preferences.savePantry(pantry)
    }

    fun addPantry(item: String) {
        val trimmed = item.trim()
        if (trimmed.isNotEmpty()) {
            pantry = pantry + trimmed
            preferences.savePantry(pantry)
        }
    }

    fun clearPantry() {
        pantry = emptySet()
        preferences.savePantry(pantry)
    }

    fun toggleFavorite(recipe: Recipe) {
        favoriteIds = if (recipe.id in favoriteIds) {
            favoriteIds - recipe.id
        } else {
            favoriteIds + recipe.id
        }
        preferences.saveFavorites(favoriteIds)
    }

    fun addToPlan(recipe: Recipe, dayOffset: Int, slot: MealSlot) {
        plannedMeals = plannedMeals
            .filterNot { it.dayOffset == dayOffset && it.slot == slot } +
            PlannedMeal(
                id = UUID.randomUUID().toString(),
                recipeId = recipe.id,
                dayOffset = dayOffset,
                slot = slot
            )
        preferences.savePlan(plannedMeals)
    }

    fun removeFromPlan(meal: PlannedMeal) {
        plannedMeals = plannedMeals.filterNot { it.id == meal.id }
        preferences.savePlan(plannedMeals)
    }

    fun recipeFor(meal: PlannedMeal): Recipe? = recipes.firstOrNull { it.id == meal.recipeId }

    fun toggleGrocery(item: GroceryItem) {
        val key = item.name.lowercase()
        checkedGroceries = if (key in checkedGroceries) {
            checkedGroceries - key
        } else {
            checkedGroceries + key
        }
        preferences.saveCheckedGroceries(checkedGroceries)
    }
}
