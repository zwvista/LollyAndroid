package com.zwstudio.lolly.ui.patterns

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
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.databinding.FragmentPatternsWebpageBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPageViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PatternsWebPageFragment : Fragment() {

    var binding by autoCleared<FragmentPatternsWebpageBinding>()
    val args: PatternsWebPageFragmentArgs by navArgs()
    val vm by viewModel<PatternsWebPageViewModel>{ parametersOf(args.list.toList(), args.index) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPatternsWebpageBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(vm.selectedPattern.url)
        binding.webView.setOnTouchListener(OnSwipeWebviewTouchListener(requireContext(), object : TouchListener {
            override fun onSwipeLeft() =
                vm.next(-1)
            override fun onSwipeRight() =
                vm.next(1)
        }))
        binding.spnPattern.adapter = makeCustomAdapter(requireContext(), vm.lstPatterns) { it.title }

        vm.selectedPatternIndex_.onEach {
            speak(vm.selectedPattern.title)
            binding.webView.loadUrl(vm.selectedPattern.url)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
