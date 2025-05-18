package com.dearos.clickandrace.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween

/**
 * Transición de entrada horizontal con desvanecimiento desde la derecha.
 */
fun transicionDeEntrada(): EnterTransition {
    return fadeIn(animationSpec = tween(300)) + slideInHorizontally(
        animationSpec = tween(300, easing = LinearOutSlowInEasing)
    ) { it }
}

/**
 * Transición de salida horizontal con desvanecimiento hacia la izquierda.
 */
fun transicionDeSalida(): ExitTransition {
    return fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
        animationSpec = tween(300, easing = FastOutLinearInEasing)
    ) { -it }
}

/**
 * Transición de entrada inversa (pop) desde la izquierda.
 */
fun transicionPopDeEntrada(): EnterTransition {
    return fadeIn(animationSpec = tween(300)) + slideInHorizontally(
        animationSpec = tween(300, easing = LinearOutSlowInEasing)
    ) { -it }
}

/**
 * Transición de salida inversa (pop) hacia la derecha.
 */
fun transicionPopDeSalida(): ExitTransition {
    return fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
        animationSpec = tween(300, easing = FastOutLinearInEasing)
    ) { it }
}