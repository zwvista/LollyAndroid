package com.zwstudio.lolly.android.misc

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.distinctUntilChanged
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.databinding.FragmentSearchBinding
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.misc.SearchViewModel
import com.zwstudio.lolly.data.misc.SettingsListener
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.misc.makeCustomAdapter2

class SearchFragment : Fragment(), SettingsListener {

    val vm by lazy { vita.with(VitaOwner.Single(this)).getViewModel<SearchViewModel>() }
    var binding by autoCleared<FragmentSearchBinding>()
    var onlineDict by autoCleared<OnlineDict>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onlineDict = OnlineDict(binding.wvDictReference, vm)
        onlineDict.initWebViewClient()

        binding.svWord.setQuery(vm.word, false)
        binding.svWord.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchDict()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                vm.word = newText
                return false
            }
        })

        vmSettings.selectedLangIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            vmSettings.updateLang()
        }

        binding.spnDictReference.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vmSettings.selectedDictReference == vmSettings.lstDictsReference[position]) return
                vmSettings.selectedDictReference = vmSettings.lstDictsReference[position]
                vmSettings.updateDictReference()
                searchDict()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        vmSettings.handler = Handler(Looper.getMainLooper())
        vmSettings.settingsListener = this
        vmSettings.getData()
    }

    override fun onDestroyView() {
        if (vmSettings.settingsListener == this)
            vmSettings.settingsListener = null
        super.onDestroyView()
    }

    override fun onGetData() {
        binding.spnLanguage.adapter = makeCustomAdapter(requireContext(), vmSettings.lstLanguages) { it.langname }
        binding.spnLanguage.setSelection(vmSettings.selectedLangIndex)
    }

    override fun onUpdateLang() {
        binding.spnDictReference.makeCustomAdapter2(requireContext(), vmSettings.lstDictsReference,  { it.dictname }, { it.url })
        binding.spnDictReference.setSelection(vmSettings.selectedDictReferenceIndex)
        searchDict()
    }

    fun searchDict() {
        vm.word = binding.svWord.query.toString()
        binding.svWord.post { binding.svWord.clearFocus() }
        onlineDict.searchDict()
    }
}
