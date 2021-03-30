package com.zwstudio.lolly.android.words

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentWordsUnitDetailBinding
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.words.WordsUnitDetailViewModel
import com.zwstudio.lolly.data.words.WordsUnitViewModel
import com.zwstudio.lolly.domain.wpp.MUnitWord
import io.reactivex.rxjava3.disposables.CompositeDisposable

class WordsUnitDetailFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsUnitViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { WordsUnitDetailViewModel(item) } }
    var binding by autoCleared<FragmentWordsUnitDetailBinding>()
    lateinit var item: MUnitWord

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        item = requireArguments().getSerializable("word") as MUnitWord
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
                    compositeDisposable.add(vm.create(item).subscribe())
                else
                    compositeDisposable.add(vm.update(item).subscribe())
                setFragmentResult("WordsUnitDetailFragment", bundleOf("result" to "1"))
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
}
