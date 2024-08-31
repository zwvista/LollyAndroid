package com.zwstudio.lolly.ui.webtextbooks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.zwstudio.lolly.databinding.FragmentWebTextbooksDetailBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.viewmodels.webtextbooks.WebTextbooksDetailViewModel
import com.zwstudio.lolly.viewmodels.webtextbooks.WebTextbooksViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WebTextbooksDetailFragment : Fragment() {

    val vm by lazy { requireParentFragment().getViewModel<WebTextbooksViewModel>() }
    val vmDetail by viewModel<WebTextbooksDetailViewModel>{ parametersOf(item) }
    var binding by autoCleared<FragmentWebTextbooksDetailBinding>()
    val args: WebTextbooksDetailFragmentArgs by navArgs()
    val item get() = args.item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWebTextbooksDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vmDetail
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
