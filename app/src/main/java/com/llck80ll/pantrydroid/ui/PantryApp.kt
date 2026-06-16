package com.llck80ll.pantrydroid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.llck80ll.pantrydroid.PantryViewModel
import com.llck80ll.pantrydroid.data.MatchingEngine
import com.llck80ll.pantrydroid.model.GroceryItem
import com.llck80ll.pantrydroid.model.Ingredient
import com.llck80ll.pantrydroid.model.MealSlot
import com.llck80ll.pantrydroid.model.Recipe
import com.llck80ll.pantrydroid.model.RecipeMatch
import com.llck80ll.pantrydroid.ui.theme.PantryAmber
import com.llck80ll.pantrydroid.ui.theme.PantryCream
import com.llck80ll.pantrydroid.ui.theme.PantryIndigo
import com.llck80ll.pantrydroid.ui.theme.PantryPurple
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

private enum class AppTab(val label: String) {
    MATCH("Match Maker"),
    SAVED("Saved"),
    PLANNER("Planner")
}

private data class IngredientGroup(
    val name: String,
    val shortName: String,
    val emoji: String,
    val items: List<String>
)

private val ingredientGroups = listOf(
    IngredientGroup("Meats & Seafood", "Meats", "MEAT", listOf("Chicken Breast", "Ground Beef", "Bacon", "Salmon", "Shrimp", "Cod Fillet", "Canned Tuna", "Sirloin Steak")),
    IngredientGroup("Dairy & Eggs", "Dairy", "DAIRY", listOf("Eggs", "Milk", "Butter", "Cheddar Cheese", "Parmesan Cheese", "Greek Yogurt")),
    IngredientGroup("Vegetables & Greens", "Veggies", "VEG", listOf("Garlic", "Onion", "Tomato", "Spinach", "Broccoli", "Mushrooms", "Potatoes", "Avocado")),
    IngredientGroup("Fruits", "Fruits", "FRUIT", listOf("Lemon", "Lime")),
    IngredientGroup("Grains & Pantry", "Grains", "GRAIN", listOf("Pasta", "Rice", "Bread", "Tortillas", "Black Beans", "Canned Tomatoes")),
    IngredientGroup("Spices & Seasonings", "Spices", "SPICE", listOf("Salt & Pepper")),
    IngredientGroup("Sauces & Oils", "Sauces", "SAUCE", listOf("Olive Oil", "Soy Sauce", "Honey"))
)

@Composable
fun PantryApp(viewModel: PantryViewModel) {
    var selectedTab by remember { mutableStateOf(AppTab.MATCH) }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, top = 18.dp, end = 16.dp, bottom = 26.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item { AppHeader(selectedTab, viewModel, onSelect = { selectedTab = it }) }

            when (selectedTab) {
                AppTab.MATCH -> {
                    item { IntroCard() }
                    item { PantryBuilder(viewModel) }
                    item { CustomizeCard(viewModel) }
                    item { FireUpCard(viewModel) }
                    item { StrictMatchCard(viewModel) }
                    item { RecipeResults(viewModel) }
                    item { ChefTipsCard() }
                }

                AppTab.SAVED -> {
                    item { SectionTitle("S", "Bookmarks Favorites", "${viewModel.favorites.size} saved") }
                    if (viewModel.favorites.isEmpty()) {
                        item { EmptyPremiumCard("Your bookmarks bar is empty", "Save recipes from the match cards and they will appear here.") }
                    } else {
                        items(viewModel.favorites, key = { it.id }) { recipe ->
                            val match = viewModel.matches.firstOrNull { it.recipe.id == recipe.id }
                                ?: RecipeMatch(recipe, emptyList(), recipe.ingredients)
                            RecipeCard(match, viewModel)
                        }
                    }
                }

                AppTab.PLANNER -> {
                    item { PlannerScreen(viewModel) }
                }
            }
        }
    }

    viewModel.selectedRecipe?.let { recipe ->
        RecipeGuideDialog(
            recipe = recipe,
            viewModel = viewModel,
            onDismiss = { viewModel.selectedRecipe = null }
        )
    }
}

@Composable
private fun AppHeader(selectedTab: AppTab, viewModel: PantryViewModel, onSelect: (AppTab) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = PantryIndigo.copy(alpha = 0.10f),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.RestaurantMenu, contentDescription = null, tint = PantryIndigo)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Pantry", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Text("SMART KITCHEN COMPANIONS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(14.dp))
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
            tonalElevation = 2.dp
        ) {
            Row(Modifier.padding(4.dp), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                HeaderTab(AppTab.MATCH.label, selectedTab == AppTab.MATCH) { onSelect(AppTab.MATCH) }
                HeaderTab("Saved (${viewModel.favoriteIds.size})", selectedTab == AppTab.SAVED) { onSelect(AppTab.SAVED) }
                HeaderTab("Planner (${viewModel.plannedMeals.size})", selectedTab == AppTab.PLANNER) { onSelect(AppTab.PLANNER) }
            }
        }
    }
}

@Composable
private fun HeaderTab(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = CircleShape,
        color = if (selected) MaterialTheme.colorScheme.surface else Color.Transparent,
        shadowElevation = if (selected) 2.dp else 0.dp
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Black,
            color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun IntroCard() {
    PremiumCard {
        SectionTitle("*", "What can you cook today?", null)
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color.Transparent,
            modifier = Modifier.border(1.4.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(18.dp))
        ) {
            Row(Modifier.padding(18.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = PantryAmber, modifier = Modifier.size(34.dp))
                Column {
                    Text("Welcome to Pantry!", fontWeight = FontWeight.Black)
                    Text(
                        "Turn your ingredients into custom chef-quality recipes with our matching database. No food waste, no hassle.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun PantryBuilder(viewModel: PantryViewModel) {
    var activeGroup by remember { mutableStateOf(ingredientGroups.first()) }
    var customIngredient by remember { mutableStateOf("") }

    PremiumCard {
        NumberedTitle(1, "Your Kitchen Pantry", "Select ingredients you have on hand to unlock customized recipe matching.")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("YOUR PANTRY", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, letterSpacing = MaterialTheme.typography.labelMedium.letterSpacing)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = viewModel::clearPantry) {
                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(4.dp))
                Text("RESET PANTRY", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
            }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(ingredientGroups) { group ->
                FilterChip(
                    selected = activeGroup == group,
                    onClick = { activeGroup = group },
                    label = { Text("${group.emoji} ${group.shortName}", fontWeight = FontWeight.Bold) }
                )
            }
        }
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.border(1.2.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(18.dp))
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(activeGroup.items) { ingredient ->
                        IngredientPill(
                            ingredient,
                            selected = ingredient in viewModel.pantry,
                            onClick = { viewModel.togglePantry(ingredient) }
                        )
                    }
                }
                Button(
                    onClick = { activeGroup.items.forEach { if (it !in viewModel.pantry) viewModel.togglePantry(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PantryIndigo)
                ) {
                    Text("Add All ${activeGroup.shortName}", fontWeight = FontWeight.Black)
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = customIngredient,
                onValueChange = { customIngredient = it },
                label = { Text("Additional Pantry Items") },
                placeholder = { Text("E.g., Spinach, Soy Sauce, Basil") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                viewModel.addPantry(customIngredient)
                customIngredient = ""
            }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
        PantryInventory(viewModel)
    }
}

@Composable
private fun IngredientPill(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) PantryIndigo else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) PantryIndigo else MaterialTheme.colorScheme.outline)
    ) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(if (selected) Icons.Default.Check else Icons.Default.Add, contentDescription = null, modifier = Modifier.size(15.dp), tint = if (selected) Color.White else PantryIndigo)
            Spacer(Modifier.width(5.dp))
            Text(label, fontWeight = FontWeight.Bold, color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun PantryInventory(viewModel: PantryViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("PANTRY INVENTORY BY CATEGORY", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
            Spacer(Modifier.weight(1f))
            Badge("${viewModel.pantry.size} items")
        }
        Text(
            "Your active kitchen ingredients sorted by pantry category. Click any item chip to quickly remove it.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (viewModel.pantry.isEmpty()) {
            Text("No ingredients selected yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            ingredientGroups.forEach { group ->
                val selected = group.items.filter { it in viewModel.pantry }
                if (selected.isNotEmpty()) {
                    Text("${group.emoji} ${group.shortName}  ${selected.size}", fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelMedium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(selected) { item ->
                            AssistChip(onClick = { viewModel.togglePantry(item) }, label = { Text(item) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomizeCard(viewModel: PantryViewModel) {
    PremiumCard {
        NumberedTitle(2, "Customize Cuisine & Portion Sizes", "Personalize the flavor direction and portions for custom recipe guides.")
        Text("Choose Cuisine Preference", fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(selected = viewModel.selectedCuisine == null, onClick = { viewModel.selectedCuisine = null }, label = { Text("Any Cuisine") })
            }
            items(viewModel.cuisines) { cuisine ->
                FilterChip(selected = viewModel.selectedCuisine == cuisine, onClick = { viewModel.selectedCuisine = cuisine }, label = { Text(cuisineEmoji(cuisine) + " " + cuisine) })
            }
        }
        Text("Select Number of Servings", fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelMedium)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { if (viewModel.servings > 1) viewModel.servings-- }) { Text("-") }
            Text("${viewModel.servings}", modifier = Modifier.padding(horizontal = 28.dp), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            OutlinedButton(onClick = { if (viewModel.servings < 12) viewModel.servings++ }) { Text("+") }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf(1 to "Single", 2 to "Couple", 4 to "Family", 6 to "Group")) { option ->
                FilterChip(
                    selected = viewModel.servings == option.first,
                    onClick = { viewModel.servings = option.first },
                    label = { Text("${option.first} Servs ${option.second}") }
                )
            }
        }
    }
}

@Composable
private fun FireUpCard(viewModel: PantryViewModel) {
    PremiumCard {
        NumberedTitle(3, "Fire Up the Stove!", "Find delicious guides tailored perfectly to whatever ingredients you have.")
        StatRow(
            listOf(
                "Engine Status" to if (viewModel.pantry.isEmpty()) "Standby" else "Active",
                "Loaded Items" to "${viewModel.pantry.size} Ingredients",
                "Portion Sizes" to "${viewModel.servings} Servings"
            )
        )
        Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)) {
            Text(
                "Combinations: ${viewModel.pantry.sorted().take(1).firstOrNull() ?: "None"} ${if (viewModel.pantry.size > 1) "+ ${viewModel.pantry.size - 1} more" else ""}  READY",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PantryIndigo)
        ) {
            Icon(Icons.Default.Restaurant, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("LET'S START COOKING!", fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun StrictMatchCard(viewModel: PantryViewModel) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = PantryIndigo.copy(alpha = 0.06f),
        border = androidx.compose.foundation.BorderStroke(1.dp, PantryIndigo.copy(alpha = 0.16f))
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(12.dp), color = PantryPurple.copy(alpha = 0.14f), modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Search, contentDescription = null, tint = PantryPurple) }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("STRICT PANTRY MATCH (90%+)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
                Text("Only showing formulas where you own at least 90% of ingredients.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(if (viewModel.strictMatch) "ON (Strict)" else "OFF (Show All)", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
            }
            Switch(checked = viewModel.strictMatch, onCheckedChange = { viewModel.strictMatch = it })
        }
    }
}

@Composable
private fun RecipeResults(viewModel: PantryViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle("R", "Gourmet Recipes", "${viewModel.matches.size} total")
        Text("A custom culinary selection built around your pantry.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (viewModel.matches.isEmpty()) {
            EmptyPremiumCard("No Matches Found", "No recipes fit your selected filters. Try relaxing strict matching or selecting more ingredients.")
        } else {
            viewModel.matches.forEach { RecipeCard(it, viewModel) }
        }
    }
}

@Composable
private fun RecipeCard(match: RecipeMatch, viewModel: PantryViewModel) {
    val recipe = match.recipe
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            HeroPanel(recipe, Modifier.fillMaxWidth().height(180.dp)) {
                IconButton(onClick = { viewModel.toggleFavorite(recipe) }) {
                    Icon(
                        if (recipe.id in viewModel.favoriteIds) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.White
                    )
                }
            }
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(recipe.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Text(recipe.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
                StatRow(
                    listOf(
                        "${scaledCalories(recipe, viewModel)} CAL" to "",
                        "${scaledMacro(recipe.nutrition.protein, recipe, viewModel)}g PROT" to "",
                        "${scaledMacro(recipe.nutrition.carbs, recipe, viewModel)}g CARB" to ""
                    ),
                    compact = true
                )
                MatchBox(match)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { viewModel.selectedRecipe = recipe },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = PantryIndigo)
                    ) { Text("Chef Guide", fontWeight = FontWeight.Black) }
                    OutlinedButton(
                        onClick = { viewModel.addToPlan(recipe, 0, MealSlot.DINNER) },
                        modifier = Modifier.weight(1f)
                    ) { Text("Add to Meal Planning", textAlign = TextAlign.Center) }
                }
                Text("Comments (0)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun HeroPanel(recipe: Recipe, modifier: Modifier = Modifier, trailing: @Composable () -> Unit = {}) {
    val gradient = Brush.linearGradient(listOf(PantryIndigo, PantryPurple, Color(0xFF111827)))
    Box(modifier.clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)).background(gradient)) {
        Text(recipe.icon, modifier = Modifier.align(Alignment.Center), color = Color.White.copy(alpha = 0.92f), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black)
        Row(
            modifier = Modifier.align(Alignment.TopStart).padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MiniBadge(difficulty(recipe))
            MiniBadge("${recipe.totalMinutes} MINS")
        }
        Box(Modifier.align(Alignment.TopEnd).padding(8.dp)) { trailing() }
    }
}

@Composable
private fun MatchBox(match: RecipeMatch) {
    Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface, border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${match.percentage}%", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = PantryAmber)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("MATCH RATE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = PantryAmber)
                    Text("${match.owned.size}/${match.recipe.ingredients.size} OWNED", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
            LinearProgressIndicator(progress = { match.percentage / 100f }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape), color = PantryAmber)
            if (match.missing.isNotEmpty()) {
                Text("MISSING INGREDIENTS:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Color(0xFFE11D48))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(match.missing.take(4)) { ing -> MiniBadge(ing.name, bg = Color(0xFFFFEEF2), fg = Color(0xFFE11D48)) }
                }
            } else {
                Text("Chef Ready!", fontWeight = FontWeight.Black, color = PantryIndigo)
            }
        }
    }
}

@Composable
private fun RecipeGuideDialog(recipe: Recipe, viewModel: PantryViewModel, onDismiss: () -> Unit) {
    var showPlanner by remember { mutableStateOf(false) }
    var completed by remember { mutableStateOf(setOf<Int>()) }
    val owned = recipe.ingredients.filter { ingredient -> viewModel.pantry.any { MatchingEngine.namesMatch(it, ingredient.name) } }
    val progress = if (recipe.steps.isEmpty()) 0f else completed.size / recipe.steps.size.toFloat()

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Box {
                        HeroPanel(recipe, Modifier.fillMaxWidth().height(190.dp)) {
                            Row {
                                IconButton(onClick = { viewModel.toggleFavorite(recipe) }) {
                                    Icon(if (recipe.id in viewModel.favoriteIds) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint = Color.White)
                                }
                                IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, null, tint = Color.White) }
                            }
                        }
                        Column(Modifier.align(Alignment.BottomStart).padding(18.dp)) {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(recipe.tags.take(4)) { MiniBadge(it.uppercase()) }
                            }
                            Text(recipe.name, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        }
                    }
                }
                item {
                    Column(Modifier.padding(horizontal = 18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(shape = RoundedCornerShape(16.dp), color = PantryAmber.copy(alpha = 0.10f)) {
                            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Surface(shape = RoundedCornerShape(12.dp), color = PantryAmber.copy(alpha = 0.65f), modifier = Modifier.size(48.dp)) {
                                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.RestaurantMenu, null, tint = Color.White) }
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text("COOKING JOURNEY STATUS", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                                    Text("${completed.size} of ${recipe.steps.size} steps completed", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Text("RECIPE JOURNEY STEPS      ${(progress * 100).toInt()}% COOKED", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = PantryAmber)
                        LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape), color = PantryAmber)
                        StatRow(listOf("TIME" to "${recipe.totalMinutes} MINS", "SERVINGS" to "${viewModel.servings} SERV", "LEVEL" to difficulty(recipe).uppercase()))
                        Button(
                            onClick = { showPlanner = true },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PantryIndigo)
                        ) {
                            Icon(Icons.Default.CalendarMonth, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Add to Weekly Meal Plan", fontWeight = FontWeight.Black)
                        }
                        Text("NUTRITION INFO", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Black)
                        StatRow(listOf("CAL" to "${scaledCalories(recipe, viewModel)}", "PROT" to "${scaledMacro(recipe.nutrition.protein, recipe, viewModel)} g", "CARB" to "${scaledMacro(recipe.nutrition.carbs, recipe, viewModel)} g", "FAT" to "${scaledMacro(recipe.nutrition.fat, recipe, viewModel)} g"))
                        InventoryChecklist(recipe.ingredients, owned)
                        Text("INTERACTIVE COOKING STEPS", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
                    }
                }
                itemsIndexed(recipe.steps) { index, step ->
                    StepCard(
                        index = index,
                        step = step,
                        ingredients = ingredientsForStep(step, recipe.ingredients),
                        owned = owned,
                        completed = index in completed,
                        onClick = { completed = if (index in completed) completed - index else completed + index }
                    )
                }
                item {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.padding(horizontal = 18.dp).fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF18181B))
                    ) { Text("Close Recipe") }
                }
            }
        }
    }

    if (showPlanner) {
        AddToPlanDialog(recipe, onDismiss = { showPlanner = false }) { day, slot ->
            viewModel.addToPlan(recipe, day, slot)
            showPlanner = false
        }
    }
}

@Composable
private fun InventoryChecklist(ingredients: List<Ingredient>, owned: List<Ingredient>) {
    Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f), border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row {
                Text("INVENTORY CHECKLIST", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                Spacer(Modifier.weight(1f))
                Badge("${ingredients.size} ITEMS")
            }
            Text("Gather and check off ingredients before starting. Indigo labels match your active pantry.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            ingredients.forEach { ing ->
                val hasIt = ing in owned
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (hasIt) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank, null, tint = if (hasIt) PantryIndigo else MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(8.dp))
                    Text(ing.name, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    Text(ing.amount, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(6.dp))
                    MiniBadge(if (hasIt) "OWNED" else "NEED", bg = if (hasIt) PantryIndigo.copy(alpha = 0.10f) else Color(0xFFFFEEF2), fg = if (hasIt) PantryIndigo else Color(0xFFE11D48))
                }
            }
        }
    }
}

@Composable
private fun StepCard(index: Int, step: String, ingredients: List<Ingredient>, owned: List<Ingredient>, completed: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.padding(horizontal = 18.dp).fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = if (completed) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f) else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Surface(shape = RoundedCornerShape(10.dp), color = if (completed) MaterialTheme.colorScheme.surfaceVariant else PantryIndigo, modifier = Modifier.size(34.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text("${index + 1}", color = if (completed) MaterialTheme.colorScheme.onSurfaceVariant else Color.White, fontWeight = FontWeight.Black) }
                }
                Spacer(Modifier.width(10.dp))
                Text(step, modifier = Modifier.weight(1f), textDecoration = if (completed) TextDecoration.LineThrough else null, fontWeight = FontWeight.SemiBold)
            }
            if (ingredients.isNotEmpty()) {
                Text("REQUIRED:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(ingredients) { ing ->
                        val hasIt = ing in owned
                        MiniBadge("${ing.name.uppercase()} (${ing.amount})${if (hasIt) "" else " NEED"}", bg = if (hasIt) PantryIndigo.copy(alpha = 0.10f) else Color(0xFFFFEEF2), fg = if (hasIt) PantryIndigo else Color(0xFFE11D48))
                    }
                }
            }
        }
    }
}

@Composable
private fun PlannerScreen(viewModel: PantryViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionTitle("P", "Weekly Smart Meal Planner", "${viewModel.plannedMeals.size} slots")
        (0 until 7).forEach { offset ->
            val meals = viewModel.plannedMeals.filter { it.dayOffset == offset }.sortedBy { it.slot.ordinal }
            Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surface, border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(dayName(offset), fontWeight = FontWeight.Black)
                    if (meals.isEmpty()) Text("No meals planned", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    meals.forEach { meal ->
                        viewModel.recipeFor(meal)?.let { recipe ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(recipe.icon)
                                Spacer(Modifier.width(8.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(recipe.name, fontWeight = FontWeight.Bold)
                                    Text(meal.slot.label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                IconButton(onClick = { viewModel.removeFromPlan(meal) }) { Icon(Icons.Default.Delete, null) }
                            }
                        }
                    }
                }
            }
        }
        Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surface, border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Grocery Checklist", fontWeight = FontWeight.Black)
                if (viewModel.groceryItems.isEmpty()) {
                    Text("Schedule kitchen formulas on your calendar to auto-compile your grocery shopping list.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    viewModel.groceryItems.forEach { item ->
                        GroceryRow(item, item.name.lowercase() in viewModel.checkedGroceries) { viewModel.toggleGrocery(item) }
                    }
                }
            }
        }
    }
}

@Composable
private fun GroceryRow(item: GroceryItem, checked: Boolean, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(if (checked) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank, null, tint = PantryIndigo)
        Spacer(Modifier.width(9.dp))
        Column {
            Text(item.name, fontWeight = FontWeight.Bold, textDecoration = if (checked) TextDecoration.LineThrough else null)
            Text(item.amounts.joinToString(" + "), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AddToPlanDialog(recipe: Recipe, onDismiss: () -> Unit, onConfirm: (Int, MealSlot) -> Unit) {
    var day by remember { mutableIntStateOf(0) }
    var slot by remember { mutableStateOf(MealSlot.DINNER) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Assign to Planner") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(recipe.name, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("1. Choose Day of Week", fontWeight = FontWeight.Black)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(7) { offset -> FilterChip(selected = day == offset, onClick = { day = offset }, label = { Text(dayName(offset).take(3)) }) }
                }
                Text("2. Choose Meal Slot", fontWeight = FontWeight.Black)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(MealSlot.entries) { meal -> FilterChip(selected = slot == meal, onClick = { slot = meal }, label = { Text(meal.label) }) }
                }
            }
        },
        confirmButton = { Button(onClick = { onConfirm(day, slot) }) { Text("Confirm Meal Slot") } },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun ChefTipsCard() {
    Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFFFFBEB), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Default.TipsAndUpdates, null, tint = PantryAmber)
            Column {
                Text("Kitchen Chef's Intelligence Hub", fontWeight = FontWeight.Black)
                Text("Replacing refined grains with whole grains like brown rice, quinoa, or whole-wheat pasta doubles your fiber intake and helps stabilize day-long energy levels.", color = Color(0xFF713F12))
                Text("SMART CHEF ADVICE ACTIVE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Color(0xFF92400E))
            }
        }
    }
}

@Composable
private fun PremiumCard(content: @Composable ColumnScope.() -> Unit) {
    Card(shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), content = content)
    }
}

@Composable
private fun SectionTitle(icon: String, title: String, badge: String?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = RoundedCornerShape(10.dp), color = PantryIndigo.copy(alpha = 0.10f), modifier = Modifier.size(34.dp)) {
            Box(contentAlignment = Alignment.Center) { Text(icon) }
        }
        Spacer(Modifier.width(10.dp))
        Text(title.uppercase(), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = PantryIndigo, modifier = Modifier.weight(1f))
        if (badge != null) Badge(badge)
    }
}

@Composable
private fun NumberedTitle(number: Int, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = RoundedCornerShape(10.dp), color = PantryIndigo, modifier = Modifier.size(34.dp)) {
            Box(contentAlignment = Alignment.Center) { Text("$number", color = Color.White, fontWeight = FontWeight.Black) }
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.Black)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun Badge(text: String) {
    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.onSurface) {
        Text(text.uppercase(), modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp), color = MaterialTheme.colorScheme.surface, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun MiniBadge(text: String, bg: Color = Color.White.copy(alpha = 0.88f), fg: Color = MaterialTheme.colorScheme.onSurface) {
    Surface(shape = RoundedCornerShape(10.dp), color = bg) {
        Text(text, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp), color = fg, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun StatRow(values: List<Pair<String, String>>, compact: Boolean = false) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        values.forEach { (label, value) ->
            Surface(shape = RoundedCornerShape(if (compact) 8.dp else 14.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f), border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline), modifier = Modifier.weight(1f)) {
                Column(Modifier.padding(if (compact) 7.dp else 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    if (value.isNotEmpty()) Text(value, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun EmptyPremiumCard(title: String, body: String) {
    PremiumCard {
        Text(title, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
        Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun cuisineEmoji(cuisine: String) = when (cuisine) {
    "Asian" -> "AS"
    "American" -> "US"
    "Italian" -> "IT"
    "Mexican" -> "MX"
    "French" -> "FR"
    "Mediterranean" -> "MED"
    else -> "FOOD"
}

private fun difficulty(recipe: Recipe) = when {
    recipe.totalMinutes <= 20 -> "Easy"
    recipe.totalMinutes <= 30 -> "Medium"
    else -> "Hard"
}

private fun scaledCalories(recipe: Recipe, viewModel: PantryViewModel): Int =
    (recipe.nutrition.calories * (viewModel.servings.toDouble() / recipe.servings)).toInt()

private fun scaledMacro(value: Int, recipe: Recipe, viewModel: PantryViewModel): Int =
    (value * (viewModel.servings.toDouble() / recipe.servings)).toInt()

private fun ingredientsForStep(step: String, ingredients: List<Ingredient>): List<Ingredient> {
    val lower = step.lowercase()
    return ingredients.filter { lower.contains(it.name.lowercase().split(" ").first()) }
}

private fun dayName(offset: Int): String = LocalDate.now()
    .plusDays(offset.toLong())
    .dayOfWeek
    .getDisplayName(TextStyle.FULL, Locale.getDefault())

