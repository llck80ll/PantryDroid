package com.llck80ll.pantrydroid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.llck80ll.pantrydroid.PantryViewModel
import com.llck80ll.pantrydroid.data.MatchingEngine
import com.llck80ll.pantrydroid.model.GroceryItem
import com.llck80ll.pantrydroid.model.MealSlot
import com.llck80ll.pantrydroid.model.Recipe
import com.llck80ll.pantrydroid.model.RecipeMatch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

private enum class AppTab(val label: String, val icon: ImageVector) {
    PANTRY("Pantry", Icons.Default.Inventory2),
    COOK("Cook", Icons.Default.Restaurant),
    PLAN("Plan", Icons.Default.CalendarMonth),
    SAVED("Saved", Icons.Default.Favorite)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryApp(viewModel: PantryViewModel) {
    var selectedTab by remember { mutableStateOf(AppTab.PANTRY) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        when (selectedTab) {
                            AppTab.PANTRY -> "My Pantry"
                            AppTab.COOK -> "What Can I Cook?"
                            AppTab.PLAN -> "Meal Planner"
                            AppTab.SAVED -> "Saved Recipes"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                AppTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = tab == selectedTab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = null) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedTab) {
                AppTab.PANTRY -> PantryScreen(viewModel)
                AppTab.COOK -> CookScreen(viewModel)
                AppTab.PLAN -> PlannerScreen(viewModel)
                AppTab.SAVED -> SavedScreen(viewModel)
            }
        }
    }

    viewModel.selectedRecipe?.let { recipe ->
        RecipeDialog(
            recipe = recipe,
            viewModel = viewModel,
            onDismiss = { viewModel.selectedRecipe = null }
        )
    }
}

private val ingredientGroups = listOf(
    "Protein" to listOf("Chicken Breast", "Ground Beef", "Salmon", "Shrimp", "Eggs", "Bacon"),
    "Produce" to listOf("Garlic", "Onion", "Tomato", "Spinach", "Broccoli", "Mushrooms", "Potatoes", "Avocado"),
    "Dairy" to listOf("Milk", "Butter", "Cheddar Cheese", "Parmesan Cheese", "Greek Yogurt"),
    "Pantry" to listOf("Pasta", "Rice", "Bread", "Tortillas", "Black Beans", "Canned Tomatoes"),
    "Flavor" to listOf("Olive Oil", "Soy Sauce", "Honey", "Lemon", "Lime", "Salt & Pepper")
)

@Composable
private fun PantryScreen(viewModel: PantryViewModel) {
    var customIngredient by remember { mutableStateOf("") }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    Icons.Default.Inventory2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(42.dp)
                )
                Text("Cook with what you have", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(
                    "Choose ingredients already in your kitchen. Recipe matches update instantly.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = customIngredient,
                    onValueChange = { customIngredient = it },
                    label = { Text("Add another ingredient") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    viewModel.addPantry(customIngredient)
                    customIngredient = ""
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add ingredient")
                }
            }
        }

        ingredientGroups.forEach { (category, ingredients) ->
            item {
                Text(category, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ingredients) { ingredient ->
                        FilterChip(
                            selected = ingredient in viewModel.pantry,
                            onClick = { viewModel.togglePantry(ingredient) },
                            label = { Text(ingredient) },
                            leadingIcon = if (ingredient in viewModel.pantry) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else {
                                null
                            }
                        )
                    }
                }
            }
        }

        if (viewModel.pantry.isNotEmpty()) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${viewModel.pantry.size} items on your shelf",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = viewModel::clearPantry) { Text("Clear") }
                }
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(viewModel.pantry.sorted()) { ingredient ->
                        AssistChip(
                            onClick = { viewModel.togglePantry(ingredient) },
                            label = { Text(ingredient) },
                            leadingIcon = {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CookScreen(viewModel: PantryViewModel) {
    if (viewModel.pantry.isEmpty()) {
        EmptyState(
            icon = Icons.Default.Inventory2,
            title = "Stock your pantry",
            body = "Choose a few ingredients to see personalized recipe matches."
        )
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = viewModel.selectedCuisine == null,
                        onClick = { viewModel.selectedCuisine = null },
                        label = { Text("Any") }
                    )
                }
                items(viewModel.cuisines) { cuisine ->
                    FilterChip(
                        selected = viewModel.selectedCuisine == cuisine,
                        onClick = { viewModel.selectedCuisine = cuisine },
                        label = { Text(cuisine) }
                    )
                }
            }
        }

        items(viewModel.matches, key = { it.recipe.id }) { match ->
            RecipeMatchCard(match) {
                viewModel.selectedRecipe = match.recipe
            }
        }
    }
}

@Composable
private fun RecipeMatchCard(match: RecipeMatch, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.size(78.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(match.recipe.icon, style = MaterialTheme.typography.headlineLarge)
                }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row {
                    Text(
                        match.recipe.name,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "${match.percentage}%",
                        color = if (match.percentage >= 75) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    match.recipe.summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${match.recipe.totalMinutes} min  |  ${match.owned.size} owned  |  ${match.missing.size} missing",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SavedScreen(viewModel: PantryViewModel) {
    if (viewModel.favorites.isEmpty()) {
        EmptyState(
            icon = Icons.Default.FavoriteBorder,
            title = "No saved recipes",
            body = "Open a recipe and tap the heart to keep it here."
        )
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(viewModel.favorites, key = { it.id }) { recipe ->
            Card(onClick = { viewModel.selectedRecipe = recipe }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(recipe.icon, style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.size(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(recipe.name, fontWeight = FontWeight.Bold)
                        Text(
                            "${recipe.cuisine} | ${recipe.totalMinutes} min",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@Composable
private fun PlannerScreen(viewModel: PantryViewModel) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("This week", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        items(7) { offset ->
            val meals = viewModel.plannedMeals
                .filter { it.dayOffset == offset }
                .sortedBy { it.slot.ordinal }
            Card {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(dayName(offset), fontWeight = FontWeight.Bold)
                    if (meals.isEmpty()) {
                        Text(
                            "Nothing planned",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    meals.forEach { meal ->
                        viewModel.recipeFor(meal)?.let { recipe ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(recipe.icon)
                                Spacer(Modifier.size(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(recipe.name, fontWeight = FontWeight.SemiBold)
                                    Text(
                                        meal.slot.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = { viewModel.removeFromPlan(meal) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove meal")
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(8.dp))
            Text("Grocery list", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        if (viewModel.groceryItems.isEmpty()) {
            item {
                Text(
                    "Add recipes to your plan to generate a shopping list.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(viewModel.groceryItems, key = { it.name }) { item ->
                GroceryRow(item, item.name.lowercase() in viewModel.checkedGroceries) {
                    viewModel.toggleGrocery(item)
                }
            }
        }
    }
}

@Composable
private fun GroceryRow(item: GroceryItem, checked: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            if (checked) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.size(10.dp))
        Column {
            Text(
                item.name,
                fontWeight = FontWeight.SemiBold,
                textDecoration = if (checked) TextDecoration.LineThrough else null
            )
            Text(
                "${item.category} | ${item.amounts.joinToString(" + ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RecipeDialog(
    recipe: Recipe,
    viewModel: PantryViewModel,
    onDismiss: () -> Unit
) {
    var showPlanner by remember { mutableStateOf(false) }
    val owned = recipe.ingredients.filter { ingredient ->
        viewModel.pantry.any { MatchingEngine.namesMatch(it, ingredient.name) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(recipe.icon, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.size(10.dp))
                Text(recipe.name, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Text(recipe.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "${recipe.totalMinutes} min | ${recipe.servings} servings | ${recipe.nutrition.calories} cal",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                item {
                    Text("Ingredients", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                items(recipe.ingredients) { ingredient ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (ingredient in owned) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = null,
                            tint = if (ingredient in owned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(ingredient.name, modifier = Modifier.weight(1f))
                        Text(ingredient.amount, style = MaterialTheme.typography.bodySmall)
                    }
                }
                item {
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))
                    Text("Cooking steps", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                itemsIndexed(recipe.steps) { index, step ->
                    Row(verticalAlignment = Alignment.Top) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("${index + 1}", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                        Spacer(Modifier.size(8.dp))
                        Text(step, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { showPlanner = true }) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null)
                Spacer(Modifier.size(6.dp))
                Text("Plan")
            }
        },
        dismissButton = {
            Row {
                IconButton(onClick = { viewModel.toggleFavorite(recipe) }) {
                    Icon(
                        if (recipe.id in viewModel.favoriteIds) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                }
                TextButton(onClick = onDismiss) { Text("Close") }
            }
        }
    )

    if (showPlanner) {
        AddToPlanDialog(
            recipe = recipe,
            onDismiss = { showPlanner = false },
            onConfirm = { day, slot ->
                viewModel.addToPlan(recipe, day, slot)
                showPlanner = false
            }
        )
    }
}

@Composable
private fun AddToPlanDialog(
    recipe: Recipe,
    onDismiss: () -> Unit,
    onConfirm: (Int, MealSlot) -> Unit
) {
    var day by remember { mutableIntStateOf(0) }
    var slot by remember { mutableStateOf(MealSlot.DINNER) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Plan ${recipe.name}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Day", fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(7) { offset ->
                        FilterChip(
                            selected = day == offset,
                            onClick = { day = offset },
                            label = { Text(dayName(offset).take(3)) }
                        )
                    }
                }
                Text("Meal", fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(MealSlot.entries) { meal ->
                        FilterChip(
                            selected = slot == meal,
                            onClick = { slot = meal },
                            label = { Text(meal.label) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(day, slot) }) { Text("Add") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun EmptyState(icon: ImageVector, title: String, body: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun dayName(offset: Int): String = LocalDate.now()
    .plusDays(offset.toLong())
    .dayOfWeek
    .getDisplayName(TextStyle.FULL, Locale.getDefault())

