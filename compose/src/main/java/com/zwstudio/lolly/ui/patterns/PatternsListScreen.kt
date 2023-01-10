package com.zwstudio.lolly.ui.patterns

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.ui.common.DrawerScreens
import com.zwstudio.lolly.ui.common.TopBarMenu
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel

@Composable
fun PatternsListScreen(vm: PatternsViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstPatterns = vm.lstPatterns_.collectAsState().value

    LaunchedEffect(Unit, block = {
        vm.getData()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.Patterns.title,
            onButtonClicked = { openDrawer() }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(lstPatterns) { item ->
                Row() {
                    Column() {
                        Text(
                            text = item.pattern,
                            color = colorResource(R.color.color_text2)
                        )
                        Text(
                            text = item.tags,
                            color = colorResource(R.color.color_text3)
                        )
                    }
                }
            }
        }
    }
}
