package com.zwstudio.lolly.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun TopBarMenu(
    title: String = "",
    onButtonClicked: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title
            )
        },
        navigationIcon = {
            IconButton(onClick = { onButtonClicked() } ) {
                Icon(Icons.Filled.Menu, contentDescription = "")
            }
        },
        actions = actions,
        backgroundColor = MaterialTheme.colors.primaryVariant
    )
}

@Composable
fun TopBarArrow(
    title: String = "",
    navController: NavHostController?,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController?.navigateUp() } ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "")
            }
        },
        actions = actions,
        backgroundColor = MaterialTheme.colors.primaryVariant
    )
}
