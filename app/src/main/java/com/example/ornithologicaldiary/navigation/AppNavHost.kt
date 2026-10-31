package com.example.ornithologicaldiary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ornithologicaldiary.ui.screens.diary.DiaryScreen
import com.example.ornithologicaldiary.ui.screens.guide.GuideScreen
import com.example.ornithologicaldiary.ui.screens.map.MapScreen
import com.example.ornithologicaldiary.ui.screens.observation.AddObservationScreen
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
                onAddObservation = { navController.navigate(Routes.ADD_OBSERVATION) }
            )
        }
        composable(TopLevelDestination.MAP.route) {
            MapScreen()
        }
        composable(TopLevelDestination.GUIDE.route) {
            GuideScreen()
        }
        composable(TopLevelDestination.STATS.route) {
            StatsScreen()
        }
        composable(Routes.ADD_OBSERVATION) {
            AddObservationScreen(onBack = { navController.popBackStack() })
        }
    }
}
