package com.zwstudio.lolly.views.phrases

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.models.misc.MReviewOptions
import com.zwstudio.lolly.viewmodels.phrases.PhrasesReviewViewModel
import com.zwstudio.lolly.views.R
import com.zwstudio.lolly.views.databinding.FragmentPhrasesReviewBinding
import com.zwstudio.lolly.views.misc.autoCleared
import com.zwstudio.lolly.views.speak

class PhrasesReviewFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Single(this)).getViewModel {
        PhrasesReviewViewModel {
            if (hasCurrent && isSpeaking.value!!)
                speak(currentPhrase)
        }
    }}
    var binding by autoCleared<FragmentPhrasesReviewBinding>()
    var mAlreadyLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPhrasesReviewBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCheckNext.setOnClickListener { vm.check(true) }
        binding.btnCheckPrev.setOnClickListener { vm.check(false) }
        binding.btnSpeak.setOnClickListener { speak(vm.currentPhrase) }

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

    private fun newTest() =
        findNavController().navigate(PhrasesReviewFragmentDirections.actionPhrasesReviewFragmentToReviewOptionsFragment(vm.options))

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