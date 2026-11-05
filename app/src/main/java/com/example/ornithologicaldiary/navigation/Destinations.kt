package com.example.ornithologicaldiary.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
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
    STATS("stats", "Статистика", Icons.Filled.BarChart),
    SETTINGS("settings", "Настройки", Icons.Filled.Settings)
}

object Routes {

    const val NEW_ID = -1L

    const val ARG_ID = "id"

    const val OBSERVATION_EDITOR = "observation_editor"

    const val SPECIES_EDITOR = "species_editor"

    const val BIRD_SEARCH = "bird_search"

    const val OBSERVATION_EDITOR_ROUTE = "$OBSERVATION_EDITOR?$ARG_ID={$ARG_ID}"

    const val SPECIES_EDITOR_ROUTE = "$SPECIES_EDITOR?$ARG_ID={$ARG_ID}"

    fun observationEditor(id: Long = NEW_ID): String = "$OBSERVATION_EDITOR?$ARG_ID=$id"

    fun speciesEditor(id: Long = NEW_ID): String = "$SPECIES_EDITOR?$ARG_ID=$id"
}
