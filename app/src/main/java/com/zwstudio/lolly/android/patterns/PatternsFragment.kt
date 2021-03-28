package com.zwstudio.lolly.android.patterns

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.android.*
import com.zwstudio.lolly.android.databinding.FragmentPatternsBinding
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.data.DrawerListViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.copyText
import com.zwstudio.lolly.data.misc.googleString
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.patterns.PatternsViewModel
import com.zwstudio.lolly.domain.wpp.MPattern
import kotlinx.coroutines.launch

class PatternsFragment : DrawerListFragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PatternsViewModel>() }
    override val vmDrawerList: DrawerListViewModel? get() = vm
    var binding by autoCleared<FragmentPatternsBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        binding.spnScopeFilter.setSelection(0)

        binding.spnScopeFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                vm.scopeFilter = SettingsViewModel.lstScopePatternFilters[position].label
                vm.applyFilters()
                refreshListView()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        setupListView()
        vm.viewModelScope.launch {
            vm.getData()
            refreshListView()
            progressBar1.visibility = View.GONE
        }
    }

    private fun refreshListView() {
        val listAdapter = PatternsItemAdapter(vm, mDragListView)
        mDragListView.setAdapter(listAdapter, true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_patterns, menu)
        setEditMode(menu.findItem(if (vm.isEditMode) R.id.menuEditMode else R.id.menuNormalMode), vm.isEditMode)
    }

    fun setEditMode(item: MenuItem, isEditMode: Boolean) {
        vm.isEditMode = isEditMode
        item.isChecked = true
        refreshListView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menuNormalMode -> {
                setEditMode(item,false)
                true
            }
            R.id.menuEditMode -> {
                setEditMode(item,true)
                true
            }
            R.id.menuAdd -> {
                findNavController().navigate(R.id.action_patternsFragment_to_patternsDetailFragment,
                    bundleOf("pattern" to vm.newPattern()))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

//    fun onResult(resultCode: Int) {
//        if (resultCode == Activity.RESULT_OK)
//            mDragListView.resetSwipedViews(null)
//    }

    private class PatternsItemAdapter(val vm: PatternsViewModel, val mDragListView: DragListView) : DragItemAdapter<MPattern, PatternsItemAdapter.ViewHolder>() {

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
                navController.navigate(R.id.action_patternsFragment_to_patternsDetailFragment,
                    bundleOf("pattern" to item))

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
                fun delete(item: MPattern) {
                    yesNoDialog(itemView.context, "Are you sure you want to delete the pattern \"${item.pattern}\"?", {
                        val pos = mDragListView.adapter.getPositionForItem(item)
                        mDragListView.adapter.removeItem(pos)
                        vm.delete(item.id)
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
                        val builder = AlertDialog.Builder(itemView.context)
                            .setTitle(item.pattern)
                            .setItems(arrayOf("Delete", "Edit", "Browse Web Pages", "Edit Web Pages", "Copy Pattern", "Google Pattern", "Cancel")) { _, which ->
                                when (which) {
                                    0 -> delete(item)
                                    1 -> edit(item)
                                    2 -> navController.navigate(R.id.action_patternsFragment_to_patternsWebPagesBrowseFragment,
                                            bundleOf("pattern" to item))
                                    3 -> navController.navigate(R.id.action_patternsFragment_to_patternsWebPagesListFragment,
                                            bundleOf("pattern" to item))
                                    4 -> itemView.copyText(item.pattern)
                                    5 -> itemView.googleString(item.pattern)
                                    else -> {}
                                }
                            }
                        builder.show()
                    }
                    true
                }
                mForward.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MPattern
                        navController.navigate(R.id.action_patternsFragment_to_patternsWebPagesBrowseFragment,
                            bundleOf("pattern" to item))
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
