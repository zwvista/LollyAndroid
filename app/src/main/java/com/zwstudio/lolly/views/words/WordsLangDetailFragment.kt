package com.zwstudio.lolly.views.words

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.viewmodels.words.WordsLangDetailViewModel
import com.zwstudio.lolly.viewmodels.words.WordsLangViewModel
import com.zwstudio.lolly.views.R
import com.zwstudio.lolly.views.databinding.FragmentWordsLangDetailBinding
import com.zwstudio.lolly.views.misc.autoCleared
import io.reactivex.rxjava3.disposables.CompositeDisposable
import com.zwstudio.lolly.vmSettings

class WordsLangDetailFragment : Fragment(), MenuProvider {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsLangViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { WordsLangDetailViewModel(item) } }
    var binding by autoCleared<FragmentWordsLangDetailBinding>()
    val args: WordsLangDetailFragmentArgs by navArgs()
    val item get() = args.item

    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentWordsLangDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vmDetail
        }
        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_save, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuSave -> {
                vmDetail.save()
                item.word = vmSettings.autoCorrectInput(item.word)
                if (item.id == 0)
                    compositeDisposable.add(vm.create(item).subscribe())
                else
                    compositeDisposable.add(vm.update(item).subscribe())
                setFragmentResult("WordsLangDetailFragment", bundleOf("result" to "1"))
                findNavController().navigateUp()
                true
            }
            else -> false
        }
}
