package com.ramcosta.destinations.sample.wear

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.wear.compose.material.rememberSwipeToDismissBoxState
import androidx.wear.compose.navigation.rememberSwipeDismissableNavHostState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.wear.rememberWearNavHostEngine
import com.ramcosta.destinations.sample.wear.core.viewmodel.activityViewModel
import com.ramcosta.destinations.sample.wear.destinations.Destination
import com.ramcosta.destinations.sample.wear.destinations.LoginScreenDestination
import com.ramcosta.destinations.sample.wear.ui.composables.SampleScaffold

@Composable
fun SampleApp() {
    val engine = rememberWearNavHostEngine()
    val navController = engine.rememberNavController()

    val vm = activityViewModel<MainViewModel>()
    // 👇 this avoids a jump in the UI that would happen if we relied only on ShowLoginWhenLoggedOut
    val startRoute = if (!vm.isLoggedIn) LoginScreenDestination else NavGraphs.root.startRoute

    SampleScaffold(
        navController = navController,
        startRoute = startRoute,
    ) {
        DestinationsNavHost(
            engine = engine,
            navController = navController,
            navGraph = NavGraphs.root,
            startRoute = startRoute
        )

        // Has to be called after calling DestinationsNavHost because only
        // then does NavController have a graph associated that we need for
        // `appCurrentDestinationAsState` method
        ShowLoginWhenLoggedOut(vm, navController)
    }
}

val Destination.shouldShowScaffoldElements get() = this !is LoginScreenDestination

@Composable
private fun ShowLoginWhenLoggedOut(
    vm: MainViewModel,
    navController: NavHostController
) {
    val currentDestination by navController.appCurrentDestinationAsState()
    val isLoggedIn by vm.isLoggedInFlow.collectAsState()

    if (!isLoggedIn && currentDestination != LoginScreenDestination) {
        // everytime destination changes or logged in state we check
        // if we have to show Login screen and navigate to it if so
        navController.navigate(LoginScreenDestination) {
            launchSingleTop = true
        }
    }
}