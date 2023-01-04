package com.zwstudio.lolly.ui.patterns

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.MainActivity
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.databinding.FragmentPatternsBinding
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.ui.*
import com.zwstudio.lolly.ui.common.*
import com.zwstudio.lolly.ui.misc.*
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class PatternsFragment : DrawerListFragment(), MenuProvider {

    val vm by viewModel<PatternsViewModel>()
    override val vmDrawerList: DrawerListViewModel get() = vm
    var binding by autoCleared<FragmentPatternsBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentPatternsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.svTextFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                vm.applyFilters()
                refreshListView()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                vm.textFilter = newText
                if (newText.isEmpty())
                    refreshListView()
                return false
            }
        })

        binding.spnScopeFilter.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstScopePatternFilters) { it.label }
        vm.scopeFilterIndex.onEach {
            vm.applyFilters()
            refreshListView()
        }

        setupListView()
        compositeDisposable.add(vm.getData().subscribe {
            refreshListView()
            progressBar1.visibility = View.GONE
        })
    }

    private fun refreshListView() {
        val listAdapter = PatternsItemAdapter(vm, mDragListView, compositeDisposable)
        mDragListView.setAdapter(listAdapter, true)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_patterns, menu)
        setEditMode(menu.findItem(if (vm.isEditMode) R.id.menuEditMode else R.id.menuNormalMode), vm.isEditMode)
    }

    private fun setEditMode(menuItem: MenuItem, isEditMode: Boolean) {
        vm.isEditMode = isEditMode
        menuItem.isChecked = true
        refreshListView()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuNormalMode -> {
                setEditMode(menuItem, false)
                true
            }
            R.id.menuEditMode -> {
                setEditMode(menuItem, true)
                true
            }
            R.id.menuAdd -> {
                findNavController().navigate(PatternsFragmentDirections.actionPatternsFragmentToPatternsDetailFragment(vm.newPattern()))
                true
            }
            else -> false
        }

//    fun onResult(resultCode: Int) {
//        if (resultCode == Activity.RESULT_OK)
//            mDragListView.resetSwipedViews(null)
//    }

    private class PatternsItemAdapter(val vm: PatternsViewModel, val mDragListView: DragListView, val compositeDisposable: CompositeDisposable) : DragItemAdapter<MPattern, PatternsItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstPatterns
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_patterns_edit, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.pattern
            holder.mText2.text = item.tags
            holder.itemView.tag = item
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView = itemView.findViewById(R.id.text1)
            var mText2: TextView = itemView.findViewById(R.id.text2)
            var mEdit: TextView = itemView.findViewById(R.id.item_edit)
            var mDelete: TextView = itemView.findViewById(R.id.item_delete)
            var mMore: TextView = itemView.findViewById(R.id.item_more)
            var mForward: ImageView = itemView.findViewById(R.id.image_forward)
            val navController get() = (itemView.context as MainActivity).getNavController()

            init {
                initButtons()
            }

            fun edit(item: MPattern) =
                navController.navigate(PatternsFragmentDirections.actionPatternsFragmentToPatternsDetailFragment(item))

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
                fun delete(item: MPattern) {
                    yesNoDialog(itemView.context, "Are you sure you want to delete the pattern \"${item.pattern}\"?", {
                        val pos = mDragListView.adapter.getPositionForItem(item)
                        mDragListView.adapter.removeItem(pos)
                        compositeDisposable.add(vm.delete(item.id).subscribe())
                        vm.isSwipeStarted = false
                    }, {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false
                    })
                }
                mEdit.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MPattern
                        edit(item)
                    }
                    true
                }
                mDelete.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MPattern
                        delete(item)
                    }
                    true
                }
                mMore.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false

                        val item = itemView.tag as MPattern
                        // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                        AlertDialog.Builder(itemView.context)
                            .setTitle(item.pattern)
                            .setItems(arrayOf("Delete", "Edit", "Browse Web Pages", "Edit Web Pages", "Copy Pattern", "Google Pattern", "Cancel")) { _, which ->
                                when (which) {
                                    0 -> delete(item)
                                    1 -> edit(item)
                                    2 -> navController.navigate(PatternsFragmentDirections.actionPatternsFragmentToPatternsWebPagesBrowseFragment(item))
                                    3 -> navController.navigate(PatternsFragmentDirections.actionPatternsFragmentToPatternsWebPagesListFragment(item))
                                    4 -> itemView.copyText(item.pattern)
                                    5 -> itemView.googleString(item.pattern)
                                    else -> {}
                                }
                            }.show()
                    }
                    true
                }
                mForward.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MPattern
                        navController.navigate(PatternsFragmentDirections.actionPatternsFragmentToPatternsWebPagesBrowseFragment(item))
                    }
                    true
                }
            }

            override fun onItemClicked(view: View?) {
                if (vm.isSwipeStarted) {
                    mDragListView.resetSwipedViews(null)
                    vm.isSwipeStarted = false
                } else {
                    val item = view!!.tag as MPattern
                    if (vm.isEditMode)
                        edit(item)
                    else
                        speak(item.pattern)
                }
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }
}
