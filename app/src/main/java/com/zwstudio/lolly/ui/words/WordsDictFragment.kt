package com.zwstudio.lolly.ui.words

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentWordsDictBinding
import com.zwstudio.lolly.ui.common.OnlineDict
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.ui.common.makeCustomAdapter2
import com.zwstudio.lolly.viewmodels.misc.SettingsListener
import com.zwstudio.lolly.viewmodels.words.WordsDictViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class WordsDictFragment : Fragment(), TouchListener, SettingsListener {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.lstWords = args.list.toList()
        vm.selectedWordIndex = args.index

        binding.webView.setOnTouchListener(OnSwipeWebviewTouchListener(requireContext(), this))

        binding.spnWord.adapter = makeCustomAdapter(requireContext(), vm.lstWords) { it }
        vm.selectedWordIndex_.onEach {
            selectedWordChanged()
        }.launchIn(vm.viewModelScope)

        vmSettings.settingsListener = this
        onlineDict = OnlineDict(binding.webView, vm)
        onlineDict.initWebViewClient()

        binding.spnDictReference.makeCustomAdapter2(requireContext(), vmSettings.lstDictsReference, { it.dictname },  { it.url })
        vmSettings.selectedDictReferenceIndex_.onEach {
            if (!vmSettings.busy)
                vmSettings.updateDictReference()
        }.launchIn(vm.viewModelScope)
    }

    override fun onDestroyView() {
        if (vmSettings.settingsListener == this)
            vmSettings.settingsListener = null
        super.onDestroyView()
    }

    private fun selectedWordChanged() {
        selectedDictChanged()
    }

    private fun selectedDictChanged() {
        onlineDict.searchDict()
    }

    override fun onUpdateDictReference() {
        selectedDictChanged()
    }

    override fun onSwipeLeft() {
        vm.next(-1)
        selectedWordChanged()
    }

    override fun onSwipeRight() {
        vm.next(1)
        selectedWordChanged()
    }
}
