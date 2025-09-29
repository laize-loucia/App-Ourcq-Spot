package com.ourcqspot.client

import android.util.Log
import androidx.navigation.NavHostController
import com.ourcqspot.client.graphs.Graph

sealed class MainScreensData(
    val route: String,
    val frTitle: String,
    val enTitle: String,
    val iconPainterId: Int,
    val iconContentDescription: String,
    // val onClick: (navController: NavHostController?) -> Unit = {}
) {
    companion object {
        val SCREENS = getScreensList()
        var lastScreen = getScreensList().first()

        fun getScreensList() : List<MainScreensData> {
            return listOf(
                News,
                Agenda,
                Map,
                Points,
                Account
            )
        }

        fun checkIfDestinationRouteIsDifferent(destinationRoute: String): Boolean {
            return (destinationRoute != lastScreen.route)
        }

        fun checkIfDestinationRouteIsBefore(destinationRoute: String): Boolean {
            checkIfDestinationRouteIsDifferent(destinationRoute)
            val screens = listOf(
                News,
                Agenda,
                Map,
                Points,
                Account
            )
            for (screen in screens) {
                when (screen.route) {
                    lastScreen.route -> {
                        return false
                    }
                    destinationRoute -> {
                        return true
                    }
                    else -> {}
                }
            }
            return false
        }
    }

    data object News : MainScreensData(
        route = "NEWS",
        frTitle = "Actualités",
        enTitle = "News",
        iconPainterId = R.drawable.icon_news,
        iconContentDescription = "Actualités",
        /* onClick = fun(navController: NavHostController?) {
            navController?.navigate(Graph.DETAILS)
        } */
    )

    data object Agenda : MainScreensData(
        route = "AGENDA",
        frTitle = "Agenda",
        enTitle = "Agenda",
        iconPainterId = R.drawable.icon_agenda,
        iconContentDescription = "Agenda"
    )

    data object Map : MainScreensData(
        route = "MAP",
        frTitle = "Carte",
        enTitle = "Map",
        iconPainterId = R.drawable.icon_map,
        iconContentDescription = "Carte"
    )

    data object Points : MainScreensData(
        route = "POINTS",
        frTitle = "Points",
        enTitle = "Points",
        iconPainterId = R.drawable.icon_points,
        iconContentDescription = "Points"
    )

    data object Account : MainScreensData(
        route = "ACCOUNT",
        frTitle = "Compte",
        enTitle = "Account",
        iconPainterId = R.drawable.icon_account,
        iconContentDescription = "Compte"
    )
}
