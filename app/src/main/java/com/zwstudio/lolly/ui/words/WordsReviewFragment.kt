package com.zwstudio.lolly.ui.words

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.models.misc.MReviewOptions
import com.zwstudio.lolly.ui.R
import com.zwstudio.lolly.ui.databinding.FragmentWordsReviewBinding
import com.zwstudio.lolly.ui.misc.autoCleared
import com.zwstudio.lolly.ui.speak
import com.zwstudio.lolly.viewmodels.words.WordsReviewViewModel

class WordsReviewFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsReviewViewModel>() }
    var binding by autoCleared<FragmentWordsReviewBinding>()
    var mAlreadyLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWordsReviewBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCheck.setOnClickListener { vm.check() }
        binding.chkSpeak.setOnClickListener {
            if (binding.chkSpeak.isChecked)
                speak(vm.currentWord)
        }

        setFragmentResultListener("ReviewOptionsFragment") { _, bundle ->
            vm.options = bundle.getSerializable("result") as MReviewOptions
            binding.progressBar1.visibility = View.VISIBLE
            vm.newTest()
            binding.progressBar1.visibility = View.INVISIBLE
        }

        // https://stackoverflow.com/questions/7919681/how-to-determine-fragment-restored-from-backstack
        if (!mAlreadyLoaded) {
            mAlreadyLoaded = true
            newTest()
        }
    }

    override fun onDestroyView() {
        vm.subscriptionTimer?.dispose()
        super.onDestroyView()
    }

    fun newTest() =
        findNavController().navigate(R.id.action_wordsReviewFragment_to_reviewOptionsFragment,
            bundleOf("options" to vm.options))

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_new_test, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menuNewTest -> {
                newTest()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}