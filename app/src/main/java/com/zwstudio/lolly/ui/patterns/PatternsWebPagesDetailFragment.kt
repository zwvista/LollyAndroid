package com.zwstudio.lolly.ui.patterns

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.ui.R
import com.zwstudio.lolly.ui.databinding.FragmentPatternsWebpagesDetailBinding
import com.zwstudio.lolly.ui.misc.autoCleared
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPageDetailViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel
import com.zwstudio.lolly.models.wpp.MPatternWebPage
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PatternsWebPagesDetailFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PatternsWebPagesViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { PatternsWebPageDetailViewModel(item) } }
    var binding by autoCleared<FragmentPatternsWebpagesDetailBinding>()
    lateinit var item: MPatternWebPage

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        item = requireArguments().getSerializable("webpage") as MPatternWebPage
        binding = FragmentPatternsWebpagesDetailBinding.inflate(inflater, container, false).apply {
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
                if (item.id == 0)
                    compositeDisposable.add(vm.createPatternWebPage(item).subscribe())
                else
                    compositeDisposable.add(vm.updatePatternWebPage(item).subscribe())
                setFragmentResult("PatternsWebPagesDetailFragment", bundleOf("result" to "1"))
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
}
