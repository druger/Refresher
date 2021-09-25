package com.druger.refresher.ui

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.druger.refresher.R

object Theme {

    @Composable
    fun setupTheme(content: @Composable () -> Unit) {
        MaterialTheme(
            colors = Colors(
                primary = colorResource(R.color.primary),
                primaryVariant = colorResource(R.color.primaryVariant),
                secondary = colorResource(R.color.secondary),
                secondaryVariant = colorResource(R.color.secondaryVariant),
                background = colorResource(R.color.background),
                surface = colorResource(R.color.surface),
                error = colorResource(R.color.error),
                onPrimary = colorResource(R.color.onPrimary),
                onSecondary = colorResource(R.color.onSecondary),
                onBackground = colorResource(R.color.onBackground),
                onSurface = colorResource(R.color.onSurface),
                onError = colorResource(R.color.onError),
                isLight = true
            ),
            content = content
        )
    }
}