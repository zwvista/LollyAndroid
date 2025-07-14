package com.zwstudio.lolly.ui.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.zwstudio.lolly.databinding.FragmentLangBlogGroupsDetailBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.viewmodels.blogs.LangBlogGroupsDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LangBlogGroupsDetailFragment : Fragment() {

    val vmDetail by viewModel<LangBlogGroupsDetailViewModel>{ parametersOf(item) }
    var binding by autoCleared<FragmentLangBlogGroupsDetailBinding>()
    val args: LangBlogGroupsDetailFragmentArgs by navArgs()
    val item get() = args.item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLangBlogGroupsDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vmDetail
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
