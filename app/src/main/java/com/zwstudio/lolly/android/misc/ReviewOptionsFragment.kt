package com.zwstudio.lolly.android.misc

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentReviewOptionsBinding
import com.zwstudio.lolly.data.misc.ReviewOptionsViewModel
import com.zwstudio.lolly.domain.misc.MReviewOptions

class ReviewOptionsFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Single(this)).getViewModel { ReviewOptionsViewModel(options) } }
    var binding by autoCleared<FragmentReviewOptionsBinding>()
    lateinit var options: MReviewOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        options = requireArguments().getSerializable("options") as MReviewOptions
        binding = FragmentReviewOptionsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuSave -> {
                vm.save()
                setFragmentResult("ReviewOptionsFragment", bundleOf("result" to options))
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
}