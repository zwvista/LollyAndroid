package com.zwstudio.lolly.ui.onlinetextbooks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.zwstudio.lolly.databinding.FragmentOnlineTextbooksWebpageBinding
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.viewmodels.onlinetextbooks.OnlineTextbooksDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class OnlineTextbooksWebPageFragment : Fragment() {

    val vm by viewModel<OnlineTextbooksDetailViewModel>{ parametersOf(item) }
    var binding by autoCleared<FragmentOnlineTextbooksWebpageBinding>()
    val args: OnlineTextbooksWebPageFragmentArgs by navArgs()
    val item get() = args.item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOnlineTextbooksWebpageBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(vm.item.url)
    }
}
