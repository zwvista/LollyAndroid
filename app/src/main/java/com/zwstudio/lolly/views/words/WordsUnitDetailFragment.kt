package com.zwstudio.lolly.views.words

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.viewmodels.misc.makeCustomAdapter
import com.zwstudio.lolly.viewmodels.words.WordsUnitDetailViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import com.zwstudio.lolly.views.R
import com.zwstudio.lolly.views.databinding.FragmentWordsUnitDetailBinding
import com.zwstudio.lolly.views.misc.autoCleared
import com.zwstudio.lolly.views.vmSettings

class WordsUnitDetailFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsUnitViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { WordsUnitDetailViewModel(item) } }
    var binding by autoCleared<FragmentWordsUnitDetailBinding>()
    val args: WordsUnitDetailFragmentArgs by navArgs()
    val item get() = args.item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWordsUnitDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vmDetail
        }
        binding.spnUnit.adapter = makeCustomAdapter(requireContext(), vmSettings.lstUnits) { it.label }
        binding.spnPart.adapter = makeCustomAdapter(requireContext(), vmSettings.lstParts) { it.label }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuSave -> {
                vmDetail.save()
                item.word = vmSettings.autoCorrectInput(item.word)
                if (item.id == 0)
                    vm.create(item)
                else
                    vm.update(item)
                setFragmentResult("WordsUnitDetailFragment", bundleOf("result" to "1"))
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
}
