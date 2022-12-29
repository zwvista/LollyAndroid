package com.zwstudio.lolly.views.misc

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.common.GlobalUser
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.viewmodels.misc.SearchViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsListener
import com.zwstudio.lolly.views.R
import com.zwstudio.lolly.views.databinding.FragmentSearchBinding

class SearchFragment : Fragment(), SettingsListener, MenuProvider {

    val vm by lazy { vita.with(VitaOwner.Single(this)).getViewModel<SearchViewModel>() }
    var binding by autoCleared<FragmentSearchBinding>()
    var onlineDict by autoCleared<OnlineDict>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
            modelSettings = vmSettings
        }
        return binding.root
    }

    fun setup() {
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
            if (!vmSettings.busy)
                vmSettings.updateLang()
        }
        vmSettings.selectedDictReferenceIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vmSettings.busy)
                vmSettings.updateDictReference()
        }
        vmSettings.selectedTextbookIndex_.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (!vmSettings.busy)
                vmSettings.updateTextbook()
        }

        vmSettings.settingsListener = this
        vmSettings.getData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalUser.userid = requireContext().getSharedPreferences("users", 0).getString("userid", "")!!
        if (GlobalUser.userid.isEmpty()) {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToLoginFragment())
        } else
            setup()
    }

    override fun onDestroyView() {
        if (vmSettings.settingsListener == this)
            vmSettings.settingsListener = null
        super.onDestroyView()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_logout, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuLogout -> {
                requireContext().getSharedPreferences("users", 0)
                    .edit()
                    .remove("userid")
                    .apply()
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToLoginFragment())
                true
            }
            else -> false
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
