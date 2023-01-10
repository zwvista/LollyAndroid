package com.zwstudio.lolly.ui.words

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.ui.common.DrawerScreens
import com.zwstudio.lolly.ui.common.TopBarMenu
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel

@Composable
fun WordsTextbookListScreen(vm: WordsUnitViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstWords = vm.lstWords_.collectAsState().value

    LaunchedEffect(Unit, block = {
        vm.getDataInLang()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.WordsTextbook.title,
            onButtonClicked = { openDrawer() }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(lstWords) { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CompositionLocalProvider(
                        LocalTextStyle provides TextStyle(fontSize = 11.sp),
                        LocalContentColor provides colorResource(R.color.color_text1)
                    ) {
                        Column(modifier = Modifier.padding(end = 16.dp)) {
                            Text(text = item.unitstr)
                            Text(text = item.partstr)
                            Text(text = "${item.seqnum}")
                        }
                    }
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