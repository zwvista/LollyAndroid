package com.zwstudio.lolly.android.misc

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentSearchBinding
import com.zwstudio.lolly.data.misc.SearchViewModel
import com.zwstudio.lolly.data.misc.SettingsListener
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.domain.misc.MDictionary
import com.zwstudio.lolly.domain.misc.MLanguage
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ItemSelect


@EFragment(R.layout.fragment_search)
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

    @AfterViews
    fun afterViews() {
        // http://stackoverflow.com/questions/3488664/android-launcher-label-vs-activity-title
        requireActivity().title = resources.getString(R.string.search)

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

        vm.vmSettings.handler = Handler(Looper.getMainLooper())
        vm.vmSettings.settingsListener = this
        compositeDisposable.add(vm.vmSettings.getData().subscribe())
    }

    override fun onGetData() {
        val lst = vm.vmSettings.lstLanguages
        val adapter = makeAdapter(requireContext(), android.R.layout.simple_spinner_item, lst) { v, position ->
            val ctv = v.findViewById<TextView>(android.R.id.text1)
            ctv.text = lst[position].langname
            ctv.setTextColor(Color.BLUE)
            v
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        binding.spnLanguage.adapter = adapter

        binding.spnLanguage.setSelection(vm.vmSettings.selectedLangIndex)
    }

    @ItemSelect
    fun spnLanguageItemSelected(selected: Boolean, selectedItem: MLanguage) {
        if (vm.vmSettings.selectedLang == selectedItem) return
        compositeDisposable.add(vm.vmSettings.setSelectedLang(selectedItem).subscribe())
    }

    override fun onUpdateLang() {
        val lst = vm.vmSettings.lstDictsReference
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

        binding.spnDictReference.setSelection(vm.vmSettings.selectedDictReferenceIndex)
        searchDict()
    }

    @ItemSelect
    fun spnDictReferenceItemSelected(selected: Boolean, selectedItem: MDictionary) {
        if (vm.vmSettings.selectedDictReference == selectedItem) return
        vm.vmSettings.selectedDictReference = selectedItem
        compositeDisposable.add(vm.vmSettings.updateDictReference().subscribe())
        searchDict()
    }

    fun searchDict() {
        vm.word = binding.svWord.query.toString()
        binding.svWord.post { binding.svWord.clearFocus() }
        onlineDict.searchDict()
    }

}
