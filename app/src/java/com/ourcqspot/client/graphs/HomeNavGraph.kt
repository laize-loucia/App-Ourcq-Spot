package com.ourcqspot.client.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.ourcqspot.client.MainScreensData
import com.ourcqspot.client.MainScreensData.Companion.checkIfDestinationRouteIsBefore
import com.ourcqspot.client.MainScreensData.Companion.checkIfDestinationRouteIsDifferent
import com.ourcqspot.client.screens.home.MapScreenContent
import com.ourcqspot.client.screens.ScreenContent
import com.ourcqspot.client.screens.home.AccountScreenContent
import com.ourcqspot.client.screens.home.AgendaScreenContent
import com.ourcqspot.client.screens.home.NewsScreenContent
import com.ourcqspot.client.screens.home.PointsScreenContent

@Composable
fun HomeNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = MainScreensData.Map.route,
        enterTransition = {
            if ( initialState.destination.route?.let {
                    checkIfDestinationRouteIsDifferent(it)
                } == true ) {
                var towardsDirection = AnimatedContentTransitionScope.SlideDirection.Right
                if (initialState.destination.route?.let {
                        checkIfDestinationRouteIsBefore(it)
                    } == true) {
                    towardsDirection = AnimatedContentTransitionScope.SlideDirection.Left
                }
                slideIntoContainer(
                    towards = towardsDirection,
                    animationSpec = tween(700)
                )
            } else {
                EnterTransition.None
            }
        },
        exitTransition = {
            if ( initialState.destination.route?.let {
                    checkIfDestinationRouteIsDifferent(it)
                } == true ) {
                var towardsDirection = AnimatedContentTransitionScope.SlideDirection.Right
                if ( initialState.destination.route?.let {
                        checkIfDestinationRouteIsBefore(it)
                    } == true) {
                    towardsDirection = AnimatedContentTransitionScope.SlideDirection.Left
                }
                slideOutOfContainer(
                    towards = towardsDirection,
                    animationSpec = tween(700)
                )
            } else {
                ExitTransition.None
            }
        },
    ) {
        val screens = MainScreensData.getScreensList()
        composable(route = screens[0].route) {
            NewsScreenContent()
            /*ScreenContent (
                name = screens[0].route,
                useHypertextStyle = true,
                onClick = {
                    navController.navigate(Graph.DETAILS)
                }
                //onClick = BottomBarScreen.SCREENS[0].onClick
            )*/
        }
        composable(route = screens[1].route) {
            AgendaScreenContent()
        }
        composable(route = screens[2].route) {
            MapScreenContent()
        }
        composable(route = screens[3].route) {
            PointsScreenContent()
        }
        composable(route = screens[4].route) {
            AccountScreenContent()
        }
        detailsNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = DetailsScreen.Information.route
    ) {
        composable(route = DetailsScreen.Information.route) {
            ScreenContent(name = DetailsScreen.Information.route) {
                navController.navigate(DetailsScreen.Overview.route)
            }
        }
        composable(route = DetailsScreen.Overview.route) {
            ScreenContent(name = DetailsScreen.Overview.route) {
                navController.popBackStack(
                    route = DetailsScreen.Information.route,
                    inclusive = false
                )
            }
        }
    }
}

sealed class DetailsScreen(val route: String) {
    object Information : DetailsScreen(route = "INFORMATION")
    object Overview : DetailsScreen(route = "OVERVIEW")
}
