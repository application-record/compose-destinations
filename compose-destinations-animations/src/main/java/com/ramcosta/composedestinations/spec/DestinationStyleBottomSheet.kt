package com.ramcosta.composedestinations.spec

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.ramcosta.composedestinations.animations.scope.BottomSheetDestinationScopeImpl
import com.ramcosta.composedestinations.manualcomposablecalls.DestinationLambda
import com.ramcosta.composedestinations.manualcomposablecalls.ManualComposableCalls
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder

/**
 * Marks the destination to be shown with a bottom sheet style.
 * It requires "io.github.raamcosta.compose-destinations:animations-core" dependency.
 *
 * You will need to use a `ModalBottomSheetLayout` wrapping your
 * top level Composable.
 * Example:
 * ```
 * val navController = rememberNavController()
 * val bottomSheetNavigator = rememberBottomSheetNavigator()
 * navController.navigatorProvider += bottomSheetNavigator
 *
 * ModalBottomSheetLayout(
 *     bottomSheetNavigator = bottomSheetNavigator
 * ) {
 *     //...
 *     DestinationsNavHost(
 *         engine = rememberAnimatedNavHostEngine(),
 *         navController = navController
 *     )
 * ```
 */
object DestinationStyleBottomSheet : DestinationStyle

@ExperimentalMaterialNavigationApi
internal fun <T> NavGraphBuilder.addComposable(
    destination: DestinationSpec<T>,
    navController: NavHostController,
    dependenciesContainerBuilder: @Composable DependenciesContainerBuilder<*>.() -> Unit,
    manualComposableCalls: ManualComposableCalls
) {
    @SuppressLint("RestrictedApi")
    @Suppress("UNCHECKED_CAST")
    val contentWrapper = manualComposableCalls[destination.baseRoute] as? DestinationLambda<T>?

    bottomSheet(
        destination.route,
        destination.arguments,
        destination.deepLinks
    ) { navBackStackEntry ->
        CallComposable(
            destination,
            navController,
            navBackStackEntry,
            dependenciesContainerBuilder,
            contentWrapper
        )
    }
}

@Composable
private fun <T> ColumnScope.CallComposable(
    destination: DestinationSpec<T>,
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry,
    dependenciesContainerBuilder: @Composable DependenciesContainerBuilder<*>.() -> Unit,
    contentWrapper: DestinationLambda<T>?
) {
    val scope = remember(navBackStackEntry) {
        BottomSheetDestinationScopeImpl(
            destination,
            navBackStackEntry,
            navController,
            this,
            dependenciesContainerBuilder
        )
    }

    if (contentWrapper == null) {
        with(destination) { scope.Content() }
    } else {
        contentWrapper(scope)
    }
}