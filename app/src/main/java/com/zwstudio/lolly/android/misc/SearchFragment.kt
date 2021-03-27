package com.zwstudio.lolly.android.misc

import android.graphics.Color
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

        binding.spnLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vmSettings.selectedLang == vmSettings.lstLanguages[position]) return
                vmSettings.setSelectedLang(vmSettings.lstLanguages[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
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

    override fun onGetData() {
        val lst = vmSettings.lstLanguages
        val adapter = makeAdapter(requireContext(), android.R.layout.simple_spinner_item, lst) { v, position ->
            val ctv = v.findViewById<TextView>(android.R.id.text1)
            ctv.text = lst[position].langname
            ctv.setTextColor(Color.BLUE)
            v
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        binding.spnLanguage.adapter = adapter

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
