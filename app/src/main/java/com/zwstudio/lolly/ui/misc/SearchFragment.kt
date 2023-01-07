package com.zwstudio.lolly.ui.misc

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.OnlineDict
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentSearchBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.ui.common.makeCustomAdapter2
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import com.zwstudio.lolly.viewmodels.misc.SearchViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), MenuProvider {

    val vm by viewModel<SearchViewModel>()
    var binding by autoCleared<FragmentSearchBinding>()
    var onlineDict by autoCleared<OnlineDict>()
    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
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

        vmSettings.lstLanguages_.onEach {
            binding.spnLanguage.adapter = makeCustomAdapter(requireContext(), vmSettings.lstLanguages) { it.langname }
        }.launchIn(vmSettings.viewModelScope)

        vmSettings.lstDictsReference_.onEach {
            binding.spnDictReference.makeCustomAdapter2(requireContext(), vmSettings.lstDictsReference,  { it.dictname }, { it.url })
        }.launchIn(vmSettings.viewModelScope)

        vmSettings.selectedDictReferenceIndex_.onEach {
            searchDict()
        }.launchIn(vmSettings.viewModelScope)

        compositeDisposable.add(vmSettings.getData().subscribe())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalUserViewModel.load(requireContext())
        if (GlobalUserViewModel.isLoggedIn)
            setup()
        else
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToLoginFragment())
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_logout, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuLogout -> {
                GlobalUserViewModel.remove(requireContext())
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToLoginFragment())
                true
            }
            else -> false
        }

    fun searchDict() {
        vm.word = binding.svWord.query.toString()
        binding.svWord.post { binding.svWord.clearFocus() }
        onlineDict.searchDict()
    }
}
