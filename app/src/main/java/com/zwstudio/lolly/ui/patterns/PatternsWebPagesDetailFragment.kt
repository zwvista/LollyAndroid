package com.zwstudio.lolly.ui.patterns

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zwstudio.lolly.R
import com.zwstudio.lolly.databinding.FragmentPatternsWebpagesDetailBinding
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPageDetailViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel
import com.zwstudio.lolly.ui.common.autoCleared
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PatternsWebPagesDetailFragment : Fragment(), MenuProvider {

    val vm by lazy { requireParentFragment().getViewModel<PatternsWebPagesViewModel>() }
    val vmDetail by viewModel<PatternsWebPageDetailViewModel>{ parametersOf(item) }
    var binding by autoCleared<FragmentPatternsWebpagesDetailBinding>()
    val args: PatternsWebPagesDetailFragmentArgs by navArgs()
    val item get() = args.item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentPatternsWebpagesDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vmDetail
        }
        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_save, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuSave -> {
                vmDetail.save()
                if (item.id == 0)
                    vm.createPatternWebPage(item)
                else
                    vm.updatePatternWebPage(item)
                setFragmentResult("PatternsWebPagesDetailFragment", bundleOf("result" to "1"))
                findNavController().navigateUp()
                true
            }
            else -> false
        }
}