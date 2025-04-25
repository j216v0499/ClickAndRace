package com.dearos.clickandrace.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween

/*
class NavTrans {
}
*/

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

/**
 * Transición de entrada vertical desde abajo.
 */
fun transicionVerticalDeEntrada(): EnterTransition {
    return fadeIn() + slideInVertically { it }
}

/**
 * Transición de salida vertical hacia arriba.
 */
fun transicionVerticalDeSalida(): ExitTransition {
    return fadeOut() + slideOutVertically { -it }
}

/**
 * Transición de entrada vertical inversa desde arriba.
 */
fun transicionPopVerticalDeEntrada(): EnterTransition {
    return fadeIn() + slideInVertically { -it }
}

/**
 * Transición de salida vertical inversa hacia abajo.
 */
fun transicionPopVerticalDeSalida(): ExitTransition {
    return fadeOut() + slideOutVertically { it }
}

/**
 * Transición simple de entrada usando solo desvanecimiento.
 */
fun transicionDesvanecidaDeEntrada(): EnterTransition {
    return fadeIn(animationSpec = tween(300))
}

/**
 * Transición simple de salida usando solo desvanecimiento.
 */
fun transicionDesvanecidaDeSalida(): ExitTransition {
    return fadeOut(animationSpec = tween(300))
}

/**
 * Transición de entrada desde la izquierda con desvanecimiento.
 */
fun transicionDesdeIzquierda(): EnterTransition {
    return slideInHorizontally(
        animationSpec = tween(400, easing = LinearOutSlowInEasing)
    ) { -it } + fadeIn(animationSpec = tween(300))
}

/**
 * Transición de salida hacia la izquierda con desvanecimiento.
 */
fun transicionHaciaIzquierda(): ExitTransition {
    return slideOutHorizontally(
        animationSpec = tween(400, easing = FastOutLinearInEasing)
    ) { -it } + fadeOut(animationSpec = tween(300))
}

/**
 * Transición de entrada desde la derecha con desvanecimiento.
 */
fun transicionDesdeDerecha(): EnterTransition {
    return slideInHorizontally(
        animationSpec = tween(400, easing = LinearOutSlowInEasing)
    ) { it } + fadeIn(animationSpec = tween(300))
}

/**
 * Transición de salida hacia la derecha con desvanecimiento.
 */
fun transicionHaciaDerecha(): ExitTransition {
    return slideOutHorizontally(
        animationSpec = tween(400, easing = FastOutLinearInEasing)
    ) { it } + fadeOut(animationSpec = tween(300))
}

/**
 * Transición de entrada desde abajo con efecto de rebote suave.
 */
fun transicionDesdeAbajoRebote(): EnterTransition {
    return slideInVertically(
        animationSpec = tween(600, easing = LinearOutSlowInEasing)
    ) { it } + fadeIn(animationSpec = tween(300))
}

/**
 * Transición de salida hacia abajo con efecto de rebote suave.
 */
fun transicionHaciaAbajoRebote(): ExitTransition {
    return slideOutVertically(
        animationSpec = tween(600, easing = FastOutLinearInEasing)
    ) { it } + fadeOut(animationSpec = tween(300))
}

/**
 * Transición de entrada con efecto de zoom y desvanecimiento.
 */
fun transicionEscaladaEntrada(): EnterTransition {
    return scaleIn(animationSpec = tween(400)) + fadeIn(animationSpec = tween(300))
}

/**
 * Transición de salida con efecto de reducción y desvanecimiento.
 */
fun transicionEscaladaSalida(): ExitTransition {
    return scaleOut(animationSpec = tween(400)) + fadeOut(animationSpec = tween(300))
}
