package com.zwstudio.lolly.ui.onlinetextbooks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.zwstudio.lolly.databinding.FragmentOnlineTextbooksDetailBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.viewmodels.onlinetextbooks.OnlineTextbooksDetailViewModel
import com.zwstudio.lolly.viewmodels.onlinetextbooks.OnlineTextbooksViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class OnlineTextbooksDetailFragment : Fragment() {

    val vm by lazy { requireParentFragment().getViewModel<OnlineTextbooksViewModel>() }
    val vmDetail by viewModel<OnlineTextbooksDetailViewModel>{ parametersOf(item) }
    var binding by autoCleared<FragmentOnlineTextbooksDetailBinding>()
    val args: OnlineTextbooksDetailFragmentArgs by navArgs()
    val item get() = args.item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOnlineTextbooksDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vmDetail
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
