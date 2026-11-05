package com.example.ornithologicaldiary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ornithologicaldiary.ui.screens.diary.DiaryScreen
import com.example.ornithologicaldiary.ui.screens.guide.GuideScreen
import com.example.ornithologicaldiary.ui.screens.map.MapScreen
import com.example.ornithologicaldiary.ui.screens.observation.ObservationEditorScreen
import com.example.ornithologicaldiary.ui.screens.settings.SettingsScreen
import com.example.ornithologicaldiary.ui.screens.species.SpeciesEditorScreen
import com.example.ornithologicaldiary.ui.screens.stats.StatsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = TopLevelDestination.DIARY.route,
        modifier = modifier
    ) {
        composable(TopLevelDestination.DIARY.route) {
            DiaryScreen(
                onAddObservation = {
                    navController.navigate(Routes.observationEditor())
                },
                onEditObservation = { id ->
                    navController.navigate(Routes.observationEditor(id))
                }
            )
        }
        composable(TopLevelDestination.MAP.route) {
            MapScreen()
        }
        composable(TopLevelDestination.GUIDE.route) {
            GuideScreen(
                onAddSpecies = {
                    navController.navigate(Routes.speciesEditor())
                },
                onEditSpecies = { id ->
                    navController.navigate(Routes.speciesEditor(id))
                }
            )
        }
        composable(TopLevelDestination.STATS.route) {
            StatsScreen()
        }
        composable(TopLevelDestination.SETTINGS.route) {
            SettingsScreen()
        }
        composable(
            route = Routes.OBSERVATION_EDITOR_ROUTE,
            arguments = listOf(
                navArgument(Routes.ARG_ID) {
                    type = NavType.LongType
                    defaultValue = Routes.NEW_ID
                }
            )
        ) {
            ObservationEditorScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = Routes.SPECIES_EDITOR_ROUTE,
            arguments = listOf(
                navArgument(Routes.ARG_ID) {
                    type = NavType.LongType
                    defaultValue = Routes.NEW_ID
                }
            )
        ) {
            SpeciesEditorScreen(onBack = { navController.popBackStack() })
        }
    }
}
