package com.zwstudio.lolly.android.words

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentWordsDictBinding
import com.zwstudio.lolly.android.misc.OnlineDict
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.words.WordsDictViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

class WordsDictFragment : Fragment(), TouchListener {

    val vm by lazy { vita.with(VitaOwner.Single(this)).getViewModel<WordsDictViewModel>() }
    var binding by autoCleared<FragmentWordsDictBinding>()
    var onlineDict by autoCleared<OnlineDict>()
    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWordsDictBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        vm.lstWords = (intent.getSerializableExtra("list") as Array<String>).toMutableList()
//        vm.selectedWordIndex = intent.getIntExtra("index", 0)

        binding.webView.setOnTouchListener(OnSwipeWebviewTouchListener(requireContext(), this))

        run {
            val lst = vm.lstWords
            val adapter = makeAdapter(requireContext(), android.R.layout.simple_spinner_item, lst) { v, position ->
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)
                v
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            binding.spnWord.adapter = adapter

            binding.spnWord.setSelection(vm.selectedWordIndex)
        }

        run {
            val lst = vmSettings.lstDictsReference
            val adapter = makeAdapter(requireContext(), R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
                val item = getItem(position)!!
                var tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = item.dictname
                (tv as? CheckedTextView)?.isChecked = binding.spnDictReference.selectedItemPosition == position
                tv = v.findViewById<TextView>(android.R.id.text2)
                tv.text = item.url
                v
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            binding.spnDictReference.adapter = adapter

            binding.spnDictReference.setSelection(vmSettings.selectedDictReferenceIndex)
        }

        onlineDict = OnlineDict(binding.webView, vm, compositeDisposable)
        onlineDict.initWebViewClient()
        selectedWordChanged()
    }

//    fun spnWordItemSelected(selected: Boolean, position: Int) {
//        if (vm.selectedWordIndex == position) return
//        vm.selectedWordIndex = position
//        selectedWordChanged()
//    }

//    fun spnDictReferenceItemSelected(selected: Boolean, position: Int) {
//        if (vmSettings.selectedDictReferenceIndex == position) return
//        vmSettings.selectedDictReference = vmSettings.lstDictsReference[position]
//        Log.d("", String.format("Checked position:%d", position))
//        (spnDictReference.adapter as ArrayAdapter<*>).notifyDataSetChanged()
//        compositeDisposable.add(vmSettings.updateDictReference().subscribe())
//        selectedDictChanged()
//    }

    private fun selectedWordChanged() {
//        title = vm.selectedWord
        selectedDictChanged()
    }

    private fun selectedDictChanged() {
        onlineDict.searchDict()
    }

    override fun onSwipeLeft() {
        vm.next(-1);
        selectedWordChanged()
    }

    override fun onSwipeRight() {
        vm.next(1);
        selectedWordChanged()
    }
}
