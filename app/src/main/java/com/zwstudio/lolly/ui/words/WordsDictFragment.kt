package com.zwstudio.lolly.ui.words

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.zwstudio.lolly.common.*
import com.zwstudio.lolly.databinding.FragmentWordsDictBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.ui.common.makeCustomAdapter2
import com.zwstudio.lolly.viewmodels.words.WordsDictViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class WordsDictFragment : Fragment() {

    val vm by viewModel<WordsDictViewModel>()
    var binding by autoCleared<FragmentWordsDictBinding>()
    var onlineDict by autoCleared<OnlineDict>()
    val args: WordsDictFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWordsDictBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
            modelSettings = vmSettings
        }
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.lstWords = args.list.toList()
        vm.selectedWordIndex = args.index

        binding.webView.setOnTouchListener(OnSwipeWebviewTouchListener(requireContext(), object : TouchListener {
            override fun onSwipeLeft() =
                vm.next(-1)
            override fun onSwipeRight() =
                vm.next(1)
        }))

        onlineDict = OnlineDict().apply {
            wv = binding.webView
            iOnlineDict = vm
            initWebViewClient()
        }

        binding.spnWord.adapter = makeCustomAdapter(requireContext(), vm.lstWords) { it }
        binding.spnDictReference.makeCustomAdapter2(requireContext(), vmSettings.lstDictsReference, { it.dictname },  { it.url })

        combine(vm.selectedWordIndex_, vmSettings.selectedDictReferenceIndex_, ::Pair).onEach {
            speak(vm.selectedWord)
            selectedWordChanged()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun selectedWordChanged() {
        selectedDictChanged()
    }

    private fun selectedDictChanged() {
        onlineDict.searchDict()
    }
}
