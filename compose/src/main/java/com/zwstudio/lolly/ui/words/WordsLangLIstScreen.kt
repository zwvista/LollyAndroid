package com.zwstudio.lolly.ui.words

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.ui.common.DrawerScreens
import com.zwstudio.lolly.ui.common.TopBarMenu
import com.zwstudio.lolly.viewmodels.words.WordsLangViewModel

@Composable
fun WordsLangListScreen(vm: WordsLangViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstWords = vm.lstWords_.collectAsState().value

    LaunchedEffect(Unit, block = {
        vm.getData()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.WordsLang.title,
            onButtonClicked = { openDrawer() }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(lstWords) { item ->
                Row() {
                    Column() {
                        Text(
                            text = item.word,
                            color = colorResource(R.color.color_text2),
                            style = TextStyle(fontSize = 25.sp)
                        )
                        Text(
                            text = item.note,
                            color = colorResource(R.color.color_text3),
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                }
            }
        }
    }
}
