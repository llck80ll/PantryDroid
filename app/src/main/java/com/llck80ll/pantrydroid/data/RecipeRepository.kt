package com.llck80ll.pantrydroid.data

import android.content.Context
import com.llck80ll.pantrydroid.model.Recipe
import kotlinx.serialization.json.Json

class RecipeRepository(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    fun load(): List<Recipe> {
        val content = context.assets.open("recipes.json")
            .bufferedReader()
            .use { it.readText() }
            .trimStart('\uFEFF')
        return json.decodeFromString<List<Recipe>>(content)
    }
}
