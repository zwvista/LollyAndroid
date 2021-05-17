package com.zwstudio.lolly.views.patterns

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.viewmodels.misc.makeCustomAdapter
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel
import com.zwstudio.lolly.views.databinding.FragmentPatternsWebpagesBrowseBinding
import com.zwstudio.lolly.views.misc.autoCleared
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PatternsWebPagesBrowseFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PatternsWebPagesViewModel>() }
    var binding by autoCleared<FragmentPatternsWebpagesBrowseBinding>()
    val args: PatternsWebPagesBrowseFragmentArgs by navArgs()
    val item get() = args.item

    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPatternsWebpagesBrowseBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spnWebPages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.webView.loadUrl(vm.lstWebPages[position].url)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        vm.getWebPages(item.id)
        binding.spnWebPages.adapter = makeCustomAdapter(requireContext(), vm.lstWebPages) { it.title }
        binding.spnWebPages.setSelection(0)
    }
}
