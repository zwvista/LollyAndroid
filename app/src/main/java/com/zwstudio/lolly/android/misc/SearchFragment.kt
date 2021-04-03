package com.zwstudio.lolly.android.misc

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckedTextView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentSearchBinding
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.misc.SearchViewModel
import com.zwstudio.lolly.data.misc.SettingsListener
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.misc.makeCustomAdapter
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

        binding.spnLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vmSettings.selectedLang == vmSettings.lstLanguages[position]) return
                compositeDisposable.add(vmSettings.setSelectedLang(vmSettings.lstLanguages[position]).subscribe())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spnDictReference.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vmSettings.selectedDictReference == vmSettings.lstDictsReference[position]) return
                vmSettings.selectedDictReference = vmSettings.lstDictsReference[position]
                compositeDisposable.add(vmSettings.updateDictReference().subscribe())
                searchDict()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        vmSettings.handler = Handler(Looper.getMainLooper())
        vmSettings.settingsListener = this
        compositeDisposable.add(vmSettings.getData().subscribe())
    }

    override fun onDestroyView() {
        vmSettings.settingsListener = null
        super.onDestroyView()
    }

    override fun onGetData() {
        binding.spnLanguage.adapter = makeCustomAdapter(requireContext(), vmSettings.lstLanguages) { it.langname }
        binding.spnLanguage.setSelection(vmSettings.selectedLangIndex)
    }

    override fun onUpdateLang() {
        val lst = vmSettings.lstDictsReference
        val adapter = makeAdapter(requireActivity(), R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
            val item = getItem(position)!!
            var tv = v.findViewById<TextView>(android.R.id.text1)
            tv.text = item.dictname
            (tv as? CheckedTextView)?.isChecked = binding.spnDictReference.selectedItemPosition == position
            tv = v.findViewById<TextView>(android.R.id.text2)
            tv.text = item.url
            v
        }
        adapter.setDropDownViewResource(R.layout.list_item_2)
        binding.spnDictReference.adapter = adapter

        binding.spnDictReference.setSelection(vmSettings.selectedDictReferenceIndex)
        searchDict()
    }

    fun searchDict() {
        vm.word = binding.svWord.query.toString()
        binding.svWord.post { binding.svWord.clearFocus() }
        onlineDict.searchDict()
    }
}
