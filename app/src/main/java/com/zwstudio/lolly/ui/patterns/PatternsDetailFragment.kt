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
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentPatternsDetailBinding
import com.zwstudio.lolly.viewmodels.patterns.PatternsDetailViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel
import com.zwstudio.lolly.ui.common.autoCleared
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PatternsDetailFragment : Fragment(), MenuProvider {

    val vm by lazy { requireParentFragment().getViewModel<PatternsViewModel>() }
    val vmDetail by viewModel<PatternsDetailViewModel>{ parametersOf(item) }
    var binding by autoCleared<FragmentPatternsDetailBinding>()
    val args: PatternsDetailFragmentArgs by navArgs()
    val item get() = args.item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentPatternsDetailBinding.inflate(inflater, container, false).apply {
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
                item.pattern = vmSettings.autoCorrectInput(item.pattern)
                if (item.id == 0)
                    vm.create(item)
                else
                    vm.update(item)
                setFragmentResult("PatternsDetailFragment", bundleOf("result" to "1"))
                findNavController().navigateUp()
                true
            }
            else -> false
        }
}