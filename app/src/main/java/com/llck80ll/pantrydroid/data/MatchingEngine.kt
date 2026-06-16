package com.llck80ll.pantrydroid.data

import com.llck80ll.pantrydroid.model.Recipe
import com.llck80ll.pantrydroid.model.RecipeMatch
import java.text.Normalizer

object MatchingEngine {
    fun ranked(
        recipes: List<Recipe>,
        pantry: Set<String>,
        cuisine: String? = null
    ): List<RecipeMatch> = recipes
        .filter { recipe -> cuisine == null || recipe.cuisine == cuisine }
        .map { recipe ->
            val owned = recipe.ingredients.filter { ingredient ->
                pantry.any { namesMatch(it, ingredient.name) }
            }
            RecipeMatch(
                recipe = recipe,
                owned = owned,
                missing = recipe.ingredients - owned.toSet()
            )
        }
        .filter { match -> pantry.isEmpty() || match.owned.isNotEmpty() }
        .sortedWith(
            compareByDescending<RecipeMatch> {
                it.percentage
            }
                .thenByDescending { it.owned.size }
                .thenBy { it.missing.size }
        )

    fun namesMatch(first: String, second: String): Boolean {
        val lhs = normalize(first)
        val rhs = normalize(second)
        return lhs == rhs || lhs.contains(rhs) || rhs.contains(lhs)
    }

    private fun normalize(value: String): String {
        var normalized = Normalizer.normalize(value.lowercase(), Normalizer.Form.NFD)
            .replace(Regex("\\p{M}+"), "")
            .replace("&", "and")
            .replace(Regex("[^a-z0-9]"), "")
        if (normalized.endsWith("s") && normalized.length > 3) {
            normalized = normalized.dropLast(1)
        }
        return normalized
    }
}
