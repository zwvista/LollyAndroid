package com.zwstudio.lolly.views.phrases

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.views.R
import com.zwstudio.lolly.views.databinding.FragmentPhrasesTextbookDetailBinding
import com.zwstudio.lolly.views.misc.autoCleared
import com.zwstudio.lolly.views.vmSettings
import com.zwstudio.lolly.viewmodels.misc.makeCustomAdapter
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitDetailViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitViewModel
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PhrasesTextbookDetailFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PhrasesUnitViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { PhrasesUnitDetailViewModel(item) } }
    var binding by autoCleared<FragmentPhrasesTextbookDetailBinding>()
    lateinit var item: MUnitPhrase

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        item = requireArguments().getSerializable("phrase") as MUnitPhrase
        binding = FragmentPhrasesTextbookDetailBinding.inflate(inflater, container, false).apply {
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
                item.phrase = vmSettings.autoCorrectInput(item.phrase)
                compositeDisposable.add(vm.update(item).subscribe())
                setFragmentResult("PhrasesTextbookDetailFragment", bundleOf("result" to "1"))
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
}
