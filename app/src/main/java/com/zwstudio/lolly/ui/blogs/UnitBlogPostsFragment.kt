package com.zwstudio.lolly.ui.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.zwstudio.lolly.common.OnSwipeWebviewTouchListener
import com.zwstudio.lolly.common.TouchListener
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentUnitBlogPostsBinding
import com.zwstudio.lolly.services.misc.BlogService
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.viewmodels.blogs.UnitBlogPostsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class UnitBlogPostsFragment : Fragment() {

    var binding by autoCleared<FragmentUnitBlogPostsBinding>()
    val vm by viewModel<UnitBlogPostsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUnitBlogPostsBinding.inflate(inflater, container, false).apply {
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
        binding.spnUnit.adapter = makeCustomAdapter(requireContext(), vm.lstUnits) { it.label }

        vm.selectedUnitIndex_.onEach {
            val content = vmSettings.getBlogContent(vm.selectedUnit)
            val str = BlogService().markedToHtml(content)
            binding.webView.loadData(str, null, null)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
