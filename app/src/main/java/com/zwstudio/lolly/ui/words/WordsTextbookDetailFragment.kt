package com.zwstudio.lolly.ui.words

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentWordsTextbookDetailBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.viewmodels.words.WordsUnitDetailViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WordsTextbookDetailFragment : Fragment(), MenuProvider {

    val vm by lazy { requireParentFragment().getViewModel<WordsUnitViewModel>() }
    val vmDetail by viewModel<WordsUnitDetailViewModel>{ parametersOf(item) }
    var binding by autoCleared<FragmentWordsTextbookDetailBinding>()
    val args: WordsTextbookDetailFragmentArgs by navArgs()
    val item get() = args.item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentWordsTextbookDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vmDetail
        }
        binding.spnUnit.adapter = makeCustomAdapter(requireContext(), vmSettings.lstUnits) { it.label }
        binding.spnPart.adapter = makeCustomAdapter(requireContext(), vmSettings.lstParts) { it.label }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmDetail.saveEnabled.onEach {
            requireActivity().invalidateOptionsMenu()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_save, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuSave -> {
                vmDetail.save()
                vm.update(item)
                setFragmentResult("WordsTextbookDetailFragment", bundleOf())
                findNavController().navigateUp()
                true
            }
            else -> false
        }

    override fun onPrepareMenu(menu: Menu) {
        menu.findItem(R.id.menuSave).isEnabled = vmDetail.saveEnabled.value
    }
}
