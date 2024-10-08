package com.zwstudio.lolly.ui.words

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.databinding.FragmentWordsReviewBinding
import com.zwstudio.lolly.models.misc.MReviewOptions
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.viewmodels.words.WordsReviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WordsReviewFragment : Fragment(), MenuProvider {

    val vm by viewModel<WordsReviewViewModel>{ parametersOf({ self: WordsReviewViewModel -> self.run {
        if (hasCurrent && isSpeaking.value)
            speak(currentWord)
    }})}
    var binding by autoCleared<FragmentWordsReviewBinding>()
    var mAlreadyLoaded = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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