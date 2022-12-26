package com.zwstudio.lolly.views.patterns

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.viewmodels.misc.makeAdapter
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel
import com.zwstudio.lolly.views.databinding.FragmentPatternsWebpagesBrowseBinding
import com.zwstudio.lolly.views.misc.autoCleared

class PatternsWebPagesBrowseFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PatternsWebPagesViewModel>() }
    var binding by autoCleared<FragmentPatternsWebpagesBrowseBinding>()
    val args: PatternsWebPagesBrowseFragmentArgs by navArgs()
    val item get() = args.item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPatternsWebpagesBrowseBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.webViewClient = WebViewClient()
        binding.spnWebPages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.webView.loadUrl(vm.lstWebPages[position].url)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        vm.getWebPages(item.id)
        binding.spnWebPages.adapter = makeAdapter(requireContext(), R.layout.simple_spinner_item, 0, vm.lstWebPages) { v, position ->
            val tv = v.findViewById<TextView>(R.id.text1)
            tv.text = vm.getWebPageText(position)
            v
        }.apply {
            setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        }
        binding.spnWebPages.setSelection(0)
    }
}
