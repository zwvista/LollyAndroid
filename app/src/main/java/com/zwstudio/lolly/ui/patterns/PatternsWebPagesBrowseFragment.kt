package com.zwstudio.lolly.ui.patterns

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.zwstudio.lolly.databinding.FragmentPatternsWebpagesBrowseBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeAdapter
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PatternsWebPagesBrowseFragment : Fragment() {

    val vm by viewModel<PatternsWebPagesViewModel>()
    var binding by autoCleared<FragmentPatternsWebpagesBrowseBinding>()
    val args: PatternsWebPagesBrowseFragmentArgs by navArgs()
    val item get() = args.item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPatternsWebpagesBrowseBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.webViewClient = WebViewClient()

        vm.currentWebPageIndex_.filter { it != -1 }.onEach {
            binding.webView.loadUrl(vm.lstWebPages[it].url)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            vm.getWebPages(item.id)
            binding.spnWebPages.adapter = makeAdapter(requireContext(), android.R.layout.simple_spinner_item, 0, vm.lstWebPages) { v, position ->
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = vm.getWebPageText(position)
                v
            }.apply {
                setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            }
            binding.spnWebPages.setSelection(0)
        }
    }
}
