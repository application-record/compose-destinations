@file:OptIn(InternalDestinationsApi::class)

package com.ramcosta.composedestinations.animations

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.animations.defaults.*
import com.ramcosta.composedestinations.annotation.InternalDestinationsApi
import com.ramcosta.composedestinations.manualcomposablecalls.ManualComposableCalls
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.*

/**
 * Remembers and returns an instance of a [NavHostEngine]
 * suitable for navigation animations and bottom sheet styled
 * destinations.
 *
 * @param navHostContentAlignment content alignment for the NavHost.
 * @param rootDefaultAnimations animations to set as default for all destinations that don't specify
 * a destination style via `Destination` annotation's `style` argument. If [rootDefaultAnimations] is not
 * passed in, then no animations will happen by default.
 * @param defaultAnimationsForNestedNavGraph lambda called for each nested navigation graph that
 * allows you to override the default animations of [rootDefaultAnimations] with defaults just for
 * that specific nested navigation graph. Return null for all nested nav graphs, you don't wish
 * to override animations for.
 */
@ExperimentalMaterialNavigationApi
@ExperimentalAnimationApi
@Composable
fun rememberAnimatedNavHostEngine(
    navHostContentAlignment: Alignment = Alignment.Center,
    rootDefaultAnimations: RootNavGraphDefaultAnimations = RootNavGraphDefaultAnimations(),
    defaultAnimationsForNestedNavGraph: Map<NavGraphSpec, NestedNavGraphDefaultAnimations> = mapOf()
): NavHostEngine {
    additionalAddComposable = lambda

    return rememberNavHostEngine(
        navHostContentAlignment,
        rootDefaultAnimations,
        defaultAnimationsForNestedNavGraph,
    )
}

@ExperimentalMaterialNavigationApi
private val lambda: (
    NavGraphBuilder,
    DestinationSpec<*>,
    NavHostController,
    @Composable DependenciesContainerBuilder<*>.() -> Unit,
    ManualComposableCalls
) -> Unit =
    { navGraphBuilder, destinationSpec, navHostController, depContainerBuilder, manualComposableCalls ->
        navGraphBuilder.addComposable(
            destinationSpec,
            navHostController,
            depContainerBuilder,
            manualComposableCalls
        )
    }