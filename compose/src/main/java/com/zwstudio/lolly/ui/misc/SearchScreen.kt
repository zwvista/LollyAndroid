package com.zwstudio.lolly.ui.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.zwstudio.lolly.ui.theme.LollyAndroidTheme
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel

@Composable
fun SearchScreen(openDrawer: () -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Search") },
            navigationIcon = {
                IconButton(onClick = { openDrawer() } ) {
                    Icon(Icons.Filled.Menu, contentDescription = "")
                }
            },
            actions = {
                IconButton(onClick = {
                    GlobalUserViewModel.remove(context)
                }) {
                    Icon(Icons.Filled.ExitToApp, null)
                }
            },
            backgroundColor = MaterialTheme.colors.primaryVariant
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Home Page content here.")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    LollyAndroidTheme {
        SearchScreen() {}
    }
}
