package com.zwstudio.lolly.ui.phrases

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.ui.common.DrawerScreens
import com.zwstudio.lolly.ui.common.TopBarMenu
import com.zwstudio.lolly.ui.theme.LollyAndroidTheme
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitViewModel

@Composable
fun PhrasesUnitListScreen(vm: PhrasesUnitViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstPhrases = vm.lstPhrases_.collectAsState().value

    LaunchedEffect(Unit, block = {
        vm.getDataInTextbook()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.PhrasesUnit.title,
            onButtonClicked = { openDrawer() }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(lstPhrases) { item ->
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
                            text = item.phrase,
                            color = colorResource(R.color.color_text2)
                        )
                        Text(
                            text = item.translation,
                            color = colorResource(R.color.color_text3)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhrasesUnitScreenPreview() {
    LollyAndroidTheme {
        PhrasesUnitScreen() {}
    }
}
