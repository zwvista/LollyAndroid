package com.zwstudio.lolly.ui.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.zwstudio.lolly.databinding.FragmentLangBlogPostsDetailBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.viewmodels.blogs.LangBlogPostsDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LangBlogPostsDetailFragment : Fragment() {

    val vmDetail by viewModel<LangBlogPostsDetailViewModel>{ parametersOf(item) }
    var binding by autoCleared<FragmentLangBlogPostsDetailBinding>()
    val args: LangBlogPostsDetailFragmentArgs by navArgs()
    val item get() = args.item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLangBlogPostsDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vmDetail
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
