package com.zwstudio.lolly.android.words

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.databinding.FragmentWordsDictBinding
import com.zwstudio.lolly.android.misc.OnlineDict
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.misc.makeCustomAdapter2
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
        vm.lstWords = (requireArguments().getSerializable("list") as Array<String>).toMutableList()
        vm.selectedWordIndex = requireArguments().getInt("index", 0)

        binding.webView.setOnTouchListener(OnSwipeWebviewTouchListener(requireContext(), this))

        binding.spnWord.adapter = makeCustomAdapter(requireContext(), vm.lstWords) { it }
        vm.selectedWordIndex_.observe(viewLifecycleOwner) {
            selectedWordChanged()
        }

        binding.spnDictReference.makeCustomAdapter2(requireContext(), vmSettings.lstDictsReference, { it.dictname },  { it.url })
        binding.spnDictReference.setSelection(vmSettings.selectedDictReferenceIndex)
        binding.spnDictReference.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (vmSettings.selectedDictReferenceIndex == position) return
                vmSettings.selectedDictReference = vmSettings.lstDictsReference[position]
                Log.d("", String.format("Checked position:%d", position))
                (binding.spnDictReference.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                compositeDisposable.add(vmSettings.updateDictReference().subscribe())
                selectedDictChanged()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        onlineDict = OnlineDict(binding.webView, vm, compositeDisposable)
        onlineDict.initWebViewClient()
        selectedWordChanged()
    }

    private fun selectedWordChanged() {
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
