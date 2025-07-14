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
import com.zwstudio.lolly.services.misc.BlogService
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
    val vm by viewModel<LangBlogPostsContentViewModel>{ parametersOf(args.list.toList(), args.index) }
    val vmGroup by activityViewModel<LangBlogGroupsViewModel>()
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
        binding.spnLangBlogPost.adapter = makeCustomAdapter(requireContext(), vm.lstLangBlogPosts) { it.title }

        vm.selectedLangBlogPostIndex_.onEach {
            compositeDisposable.add(vmGroup.selectPost(vm.selectedLangBlogPost).subscribe())
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vmGroup.postContent_.onEach {
            val str = BlogService().markedToHtml(vmGroup.postContent)
            binding.webView.loadData(str, null, null)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
