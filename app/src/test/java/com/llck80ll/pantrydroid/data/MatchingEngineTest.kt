package com.llck80ll.pantrydroid.data

import com.llck80ll.pantrydroid.model.Ingredient
import com.llck80ll.pantrydroid.model.Nutrition
import com.llck80ll.pantrydroid.model.Recipe
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MatchingEngineTest {
    @Test
    fun `recipe with a complete match ranks first`() {
        val complete = recipe("complete", listOf("Eggs", "Butter"))
        val partial = recipe("partial", listOf("Eggs", "Butter", "Milk"))

        val result = MatchingEngine.ranked(
            recipes = listOf(partial, complete),
            pantry = setOf("Eggs", "Butter")
        )

        assertEquals("complete", result.first().recipe.id)
        assertEquals(100, result.first().percentage)
    }

    @Test
    fun `simple plurals match`() {
        assertTrue(MatchingEngine.namesMatch("Tomatoes", "Tomato"))
    }

    private fun recipe(id: String, ingredients: List<String>) = Recipe(
        id = id,
        name = id,
        summary = "",
        icon = "MEAL",
        cuisine = "Test",
        tags = emptyList(),
        prepMinutes = 1,
        cookMinutes = 1,
        servings = 1,
        ingredients = ingredients.map {
            Ingredient(name = it, amount = "1", category = "Test")
        },
        steps = emptyList(),
        nutrition = Nutrition(calories = 1, protein = 1, carbs = 1, fat = 1)
    )
}

