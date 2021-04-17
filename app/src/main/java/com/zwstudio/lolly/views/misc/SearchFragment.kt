package com.zwstudio.lolly.views.misc

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.views.R
import com.zwstudio.lolly.views.databinding.FragmentSearchBinding
import com.zwstudio.lolly.views.vmSettings
import com.zwstudio.lolly.viewmodels.misc.*
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SearchFragment : Fragment(), SettingsListener {

    val vm by lazy { vita.with(VitaOwner.Single(this)).getViewModel<SearchViewModel>() }
    var binding by autoCleared<FragmentSearchBinding>()
    var onlineDict by autoCleared<OnlineDict>()
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
            modelSettings = vmSettings
        }
        return binding.root
    }

    fun setup() {
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
            if (!vmSettings.busy)
                compositeDisposable.add(vmSettings.updateLang().subscribe())
        }
        vmSettings.selectedDictReferenceIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vmSettings.busy)
                compositeDisposable.add(vmSettings.updateDictReference().subscribe())
        }
        vmSettings.selectedTextbookIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vmSettings.busy)
                compositeDisposable.add(vmSettings.updateTextbook().subscribe())
        }

        vmSettings.settingsListener = this
        compositeDisposable.add(vmSettings.getData().subscribe())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Global.userid = requireContext().getSharedPreferences("users", 0).getString("userid", "")!!
        if (Global.userid.isEmpty()) {
            findNavController().navigate(R.id.action_searchFragment_to_loginFragment)
        } else
            setup()
    }

    override fun onDestroyView() {
        if (vmSettings.settingsListener == this)
            vmSettings.settingsListener = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_logout, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menuLogout -> {
                requireContext().getSharedPreferences("users", 0)
                    .edit()
                    .remove("userid")
                    .apply()
                findNavController().navigate(R.id.action_searchFragment_to_loginFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
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
