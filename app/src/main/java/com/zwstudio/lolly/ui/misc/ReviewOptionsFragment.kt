package com.zwstudio.lolly.ui.misc

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zwstudio.lolly.R
import com.zwstudio.lolly.databinding.FragmentReviewOptionsBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.viewmodels.misc.ReviewOptionsViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ReviewOptionsFragment : Fragment(), MenuProvider {

    val vm by viewModel<ReviewOptionsViewModel> { parametersOf(options) }
    var binding by autoCleared<FragmentReviewOptionsBinding>()
    val args: ReviewOptionsFragmentArgs by navArgs()
    val options get() = args.options

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentReviewOptionsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spnMode.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstReviewModes) { it.label }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_save, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuSave -> {
                vm.save()
                setFragmentResult("ReviewOptionsFragment", bundleOf("result" to options))
                findNavController().navigateUp()
                true
            }
            else -> false
        }
}