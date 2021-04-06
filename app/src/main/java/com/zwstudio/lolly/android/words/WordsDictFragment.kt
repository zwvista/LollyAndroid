package com.zwstudio.lolly.android.words

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.distinctUntilChanged
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.databinding.FragmentWordsDictBinding
import com.zwstudio.lolly.android.misc.OnlineDict
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.misc.SettingsListener
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.misc.makeCustomAdapter2
import com.zwstudio.lolly.data.words.WordsDictViewModel

class WordsDictFragment : Fragment(), TouchListener, SettingsListener {

    val vm by lazy { vita.with(VitaOwner.Single(this)).getViewModel<WordsDictViewModel>() }
    var binding by autoCleared<FragmentWordsDictBinding>()
    var onlineDict by autoCleared<OnlineDict>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWordsDictBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
            modelSettings = vmSettings
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.lstWords = (requireArguments().getSerializable("list") as Array<String>).toMutableList()
        vm.selectedWordIndex = requireArguments().getInt("index", 0)

        binding.webView.setOnTouchListener(OnSwipeWebviewTouchListener(requireContext(), this))

        binding.spnWord.adapter = makeCustomAdapter(requireContext(), vm.lstWords) { it }
        vm.selectedWordIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            selectedWordChanged()
        }

        vmSettings.settingsListener = this
        onlineDict = OnlineDict(binding.webView, vm)
        onlineDict.initWebViewClient()

        binding.spnDictReference.makeCustomAdapter2(requireContext(), vmSettings.lstDictsReference, { it.dictname },  { it.url })
        vmSettings.selectedDictReferenceIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (it != -1)
                vmSettings.updateDictReference()
        }
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
        vm.next(-1);
        selectedWordChanged()
    }

    override fun onSwipeRight() {
        vm.next(1);
        selectedWordChanged()
    }
}
