package com.example.ornithologicaldiary.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Today
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    DIARY("diary", "Дневник", Icons.Filled.Today),
    MAP("map", "Карта", Icons.Filled.Map),
    GUIDE("guide", "Справочник", Icons.Filled.MenuBook),
    STATS("stats", "Статистика", Icons.Filled.BarChart)
}

object Routes {
    const val ADD_OBSERVATION = "add_observation"
}
