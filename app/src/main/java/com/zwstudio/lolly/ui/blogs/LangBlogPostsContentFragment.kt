package com.zwstudio.lolly.ui.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.zwstudio.lolly.common.OnSwipeWebviewTouchListener
import com.zwstudio.lolly.common.TouchListener
import com.zwstudio.lolly.databinding.FragmentLangBlogPostsContentBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.viewmodels.blogs.LangBlogGroupsViewModel
import com.zwstudio.lolly.viewmodels.blogs.LangBlogPostsContentViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LangBlogPostsContentFragment : Fragment() {

    var binding by autoCleared<FragmentLangBlogPostsContentBinding>()
    val args: LangBlogPostsContentFragmentArgs by navArgs()
    val vmGroups by activityViewModel<LangBlogGroupsViewModel>()
    val vm by viewModel<LangBlogPostsContentViewModel>{ parametersOf(vmGroups, args.list.toList(), args.index) }
    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLangBlogPostsContentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.webViewClient = WebViewClient()
        binding.webView.setOnTouchListener(OnSwipeWebviewTouchListener(requireContext(), object : TouchListener {
            override fun onSwipeLeft() =
                vm.next(-1)
            override fun onSwipeRight() =
                vm.next(1)
        }))
        binding.spnLangBlogPost.adapter = makeCustomAdapter(requireContext(), vm.lstPosts) { it.title }

        vmGroups.postHtml_.onEach {
            binding.webView.loadData(it, null, null)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
