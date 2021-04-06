package com.zwstudio.lolly.android.misc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SearchFragment : Fragment(), SettingsListener {

    val vm by lazy { vita.with(VitaOwner.Single(this)).getViewModel<SearchViewModel>() }
    var binding by autoCleared<FragmentSearchBinding>()
    var onlineDict by autoCleared<OnlineDict>()
    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
            modelSettings = vmSettings
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onlineDict = OnlineDict(binding.wvDictReference, vm, compositeDisposable)
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
            if (it != -1)
                compositeDisposable.add(vmSettings.updateLang().subscribe())
        }
        vmSettings.selectedDictReferenceIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (it != -1)
                compositeDisposable.add(vmSettings.updateDictReference().subscribe())
        }
        vmSettings.selectedTextbookIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (it != -1)
                compositeDisposable.add(vmSettings.updateTextbook().subscribe())
        }

        vmSettings.settingsListener = this
        compositeDisposable.add(vmSettings.getData().subscribe())
    }

    override fun onDestroyView() {
        if (vmSettings.settingsListener == this)
            vmSettings.settingsListener = null
        super.onDestroyView()
    }

    override fun onGetData() {
        binding.spnLanguage.adapter = makeCustomAdapter(requireContext(), vmSettings.lstLanguages) { it.langname }
    }

    override fun onUpdateLang() {
        binding.spnDictReference.makeCustomAdapter2(requireContext(), vmSettings.lstDictsReference,  { it.dictname }, { it.url })
    }

    override fun onUpdateDictReference() {
        searchDict()
    }

    fun searchDict() {
        vm.word = binding.svWord.query.toString()
        binding.svWord.post { binding.svWord.clearFocus() }
        onlineDict.searchDict()
    }
}
