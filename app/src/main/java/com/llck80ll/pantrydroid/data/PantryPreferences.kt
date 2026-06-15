package com.llck80ll.pantrydroid.data

import android.content.Context
import com.llck80ll.pantrydroid.model.PlannedMeal
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PantryPreferences(context: Context) {
    private val preferences = context.getSharedPreferences("pantry_droid", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    fun loadPantry(): Set<String> = preferences.getStringSet(KEY_PANTRY, emptySet()).orEmpty()
    fun savePantry(value: Set<String>) = preferences.edit().putStringSet(KEY_PANTRY, value).apply()

    fun loadFavorites(): Set<String> = preferences.getStringSet(KEY_FAVORITES, emptySet()).orEmpty()
    fun saveFavorites(value: Set<String>) = preferences.edit().putStringSet(KEY_FAVORITES, value).apply()

    fun loadCheckedGroceries(): Set<String> =
        preferences.getStringSet(KEY_GROCERIES, emptySet()).orEmpty()

    fun saveCheckedGroceries(value: Set<String>) =
        preferences.edit().putStringSet(KEY_GROCERIES, value).apply()

    fun loadPlan(): List<PlannedMeal> = runCatching {
        json.decodeFromString<List<PlannedMeal>>(
            preferences.getString(KEY_PLAN, "[]") ?: "[]"
        )
    }.getOrDefault(emptyList())

    fun savePlan(value: List<PlannedMeal>) {
        preferences.edit().putString(KEY_PLAN, json.encodeToString(value)).apply()
    }

    private companion object {
        const val KEY_PANTRY = "pantry"
        const val KEY_FAVORITES = "favorites"
        const val KEY_PLAN = "plan"
        const val KEY_GROCERIES = "groceries"
    }
}

