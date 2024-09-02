package com.zwstudio.lolly.ui.words

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.zwstudio.lolly.common.OnSwipeWebviewTouchListener
import com.zwstudio.lolly.common.OnlineDict
import com.zwstudio.lolly.common.TouchListener
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.words.WordsDictViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

@Composable
fun WordsDictScreen(lstWords: List<String>, index: Int, navController: NavHostController?) {

    val vm = koinViewModel<WordsDictViewModel>().apply {
        this.lstWords = lstWords
        this.selectedWordIndex = index
    }
    val onlineDict = remember { OnlineDict() }
    val context = LocalContext.current

    LaunchedEffect(Unit, block = {
        combine(vm.selectedWordIndex_, vmSettings.selectedDictReferenceIndex_, ::Pair).onEach {
            speak(vm.selectedWord)
            onlineDict.searchDict()
        }.launchIn(this)
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = stringResource(id = R.string.words_dict),
            navController = navController
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text3))
                    .fillMaxWidth()
                    .weight(1f),
                itemsStateFlow = vm.lstWords_,
                selectedItemIndexStateFlow = vm.selectedWordIndex_,
                itemText = { it }
            )
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .fillMaxWidth()
                    .weight(1f),
                itemsStateFlow = vmSettings.lstDictsReference_,
                selectedItemIndexStateFlow = vmSettings.selectedDictReferenceIndex_,
                itemText = { it.dictname }
            )
        }
        AndroidView(
            factory = {
                WebView(it).apply {
                    onlineDict.wv = this
                    onlineDict.iOnlineDict = vm
                    onlineDict.initWebViewClient()
                    setOnTouchListener(OnSwipeWebviewTouchListener(context, object : TouchListener {
                        override fun onSwipeLeft() =
                            vm.next(-1)
                        override fun onSwipeRight() =
                            vm.next(1)
                    }))
                }
            }
        )
    }
}
