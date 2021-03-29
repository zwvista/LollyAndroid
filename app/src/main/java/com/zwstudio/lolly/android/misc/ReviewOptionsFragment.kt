package com.zwstudio.lolly.android.misc

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentReviewOptionsBinding
import com.zwstudio.lolly.android.setNavigationResult
import com.zwstudio.lolly.data.misc.ReviewOptionsViewModel
import com.zwstudio.lolly.domain.misc.MReviewOptions

class ReviewOptionsFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Single(this)).getViewModel { ReviewOptionsViewModel(item) } }
    var binding by autoCleared<FragmentReviewOptionsBinding>()
    lateinit var item: MReviewOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        item = requireArguments().getSerializable("options") as MReviewOptions
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
                setNavigationResult(vm.options)
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
}