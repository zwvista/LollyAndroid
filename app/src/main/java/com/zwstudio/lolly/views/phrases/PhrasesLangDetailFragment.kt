package com.zwstudio.lolly.views.phrases

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.models.wpp.MLangPhrase
import com.zwstudio.lolly.viewmodels.phrases.PhrasesLangDetailViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesLangViewModel
import com.zwstudio.lolly.views.R
import com.zwstudio.lolly.views.databinding.FragmentPhrasesLangDetailBinding
import com.zwstudio.lolly.views.misc.autoCleared
import com.zwstudio.lolly.views.vmSettings

class PhrasesLangDetailFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PhrasesLangViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { PhrasesLangDetailViewModel(item) } }
    var binding by autoCleared<FragmentPhrasesLangDetailBinding>()
    lateinit var item: MLangPhrase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        item = PhrasesLangDetailFragmentArgs.fromBundle(requireArguments()).item
        binding = FragmentPhrasesLangDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vmDetail
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuSave -> {
                vmDetail.save()
                item.phrase = vmSettings.autoCorrectInput(item.phrase)
                if (item.id == 0)
                    vm.create(item)
                else
                    vm.update(item)
                setFragmentResult("PhrasesLangDetailFragment", bundleOf("result" to "1"))
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
}
