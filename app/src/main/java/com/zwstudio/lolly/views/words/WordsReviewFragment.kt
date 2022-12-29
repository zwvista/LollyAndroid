package com.zwstudio.lolly.views.words

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.models.misc.MReviewOptions
import com.zwstudio.lolly.speak
import com.zwstudio.lolly.viewmodels.words.WordsReviewViewModel
import com.zwstudio.lolly.views.R
import com.zwstudio.lolly.views.databinding.FragmentWordsReviewBinding
import com.zwstudio.lolly.views.misc.autoCleared

class WordsReviewFragment : Fragment(), MenuProvider {

    val vm by lazy { vita.with(VitaOwner.Single(this)).getViewModel {
        WordsReviewViewModel {
            if (hasCurrent && isSpeaking.value!!)
                speak(currentWord)
        }
    }}
    var binding by autoCleared<FragmentWordsReviewBinding>()
    var mAlreadyLoaded = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentWordsReviewBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCheckNext.setOnClickListener { vm.check(true) }
        binding.btnCheckPrev.setOnClickListener { vm.check(false) }
        binding.btnSpeak.setOnClickListener { speak(vm.currentWord) }

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
        vm.stopTimer()
        super.onDestroyView()
    }

    private fun newTest() =
        findNavController().navigate(WordsReviewFragmentDirections.actionWordsReviewFragmentToReviewOptionsFragment(vm.options))

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_new_test, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuNewTest -> {
                newTest()
                true
            }
            else -> false
        }
}