package com.zwstudio.lolly.android.patterns

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.databinding.FragmentPatternsWebpagesBrowseBinding
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.patterns.PatternsWebPagesViewModel
import com.zwstudio.lolly.domain.wpp.MPattern
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PatternsWebPagesBrowseFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PatternsWebPagesViewModel>() }
    var binding by autoCleared<FragmentPatternsWebpagesBrowseBinding>()
    lateinit var item: MPattern

    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        item = requireArguments().getSerializable("pattern") as MPattern
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
        val lst = vm.lstWebPages
        val adapter = makeAdapter(requireContext(), android.R.layout.simple_spinner_item, lst) { v, position ->
            val tv = v.findViewById<TextView>(android.R.id.text1)
            tv.text = getItem(position)!!.title
            v
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        binding.spnWebPages.adapter = adapter
        binding.spnWebPages.setSelection(0)
    }
}
