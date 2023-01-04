package com.zwstudio.lolly.ui.words

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
import androidx.lifecycle.viewModelScope
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.MainActivity
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentWordsTextbookBinding
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.ui.*
import com.zwstudio.lolly.ui.common.*
import com.zwstudio.lolly.ui.misc.*
import kotlinx.coroutines.launch
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class WordsTextbookFragment : DrawerListFragment(), MenuProvider {

    val vm by viewModel<WordsUnitViewModel>()
    override val vmDrawerList: DrawerListViewModel get() = vm
    var binding by autoCleared<FragmentWordsTextbookBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentWordsTextbookBinding.inflate(inflater, container, false).apply {
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

        binding.spnTextbookFilter.adapter = makeCustomAdapter(requireContext(), vmSettings.lstTextbookFilters) { it.label }
        binding.spnScopeFilter.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstScopeWordFilters) { it.label }
        vm.textbookFilterIndex.onEach {
            vm.applyFilters()
            refreshListView()
        }
        vm.scopeFilterIndex.onEach {
            vm.applyFilters()
            refreshListView()
        }

        setupListView()
        vm.viewModelScope.launch {
            vm.getDataInLang()
            refreshListView()
            progressBar1.visibility = View.GONE
        }
    }

    private fun refreshListView() {
        val listAdapter = WordsTextbookItemAdapter(vm, mDragListView)
        mDragListView.setAdapter(listAdapter, true)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_words_textbook, menu)
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
            else -> false
        }

    private class WordsTextbookItemAdapter(val vm: WordsUnitViewModel, val mDragListView: DragListView) : DragItemAdapter<MUnitWord, WordsTextbookItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstWords
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_words_textbook_edit, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.word
            holder.mText2.text = item.unitpartseqnum
            holder.mText3.text = item.note
            holder.itemView.tag = item
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView = itemView.findViewById(R.id.text1)
            var mText2: TextView = itemView.findViewById(R.id.text2)
            var mText3: TextView = itemView.findViewById(R.id.text3)
            var mEdit: TextView = itemView.findViewById(R.id.item_edit)
            var mDelete: TextView = itemView.findViewById(R.id.item_delete)
            var mMore: TextView = itemView.findViewById(R.id.item_more)
            var mForward: ImageView = itemView.findViewById(R.id.image_forward)
            val navController get() = (itemView.context as MainActivity).getNavController()

            init {
                initButtons()
            }

            fun edit(item: MUnitWord) =
                navController.navigate(WordsTextbookFragmentDirections.actionWordsTextbookFragmentToWordsTextbookDetailFragment(item))

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
                fun delete(item: MUnitWord) {
                    yesNoDialog(itemView.context, "Are you sure you want to delete the word \"${item.word}\"?", {
                        val pos = mDragListView.adapter.getPositionForItem(item)
                        mDragListView.adapter.removeItem(pos)
                        vm.delete(item)
                        vm.isSwipeStarted = false
                    }, {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false
                    })
                }
                mEdit.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MUnitWord
                        edit(item)
                    }
                    true
                }
                mDelete.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MUnitWord
                        delete(item)
                    }
                    true
                }
                mMore.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false

                        val item = itemView.tag as MUnitWord
                        // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                        AlertDialog.Builder(itemView.context)
                            .setTitle(item.wordnote)
                            .setItems(arrayOf("Delete", "Edit", "Retrieve Note", "Clear Note", "Copy Word", "Google Word", "Cancel")) { _, which ->
                                when (which) {
                                    0 -> delete(item)
                                    1 -> edit(item)
                                    2 -> {
                                        val index = itemList.indexOf(item)
                                        vm.viewModelScope.launch {
                                            vm.getNote(item)
                                            mDragListView.adapter.notifyItemChanged(index)
                                        }
                                    }
                                    3 -> {
                                        val index = itemList.indexOf(item)
                                        vm.viewModelScope.launch {
                                            vm.clearNote(item)
                                            mDragListView.adapter.notifyItemChanged(index)
                                        }
                                    }
                                    4 -> itemView.copyText(item.word)
                                    5 -> itemView.googleString(item.word)
                                    else -> {}
                                }
                            }.show()
                    }
                    true
                }
                mForward.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MUnitWord
                        navController.navigate(WordsTextbookFragmentDirections.actionWordsTextbookFragmentToWordsDictFragment(
                            vm.lstWords.map { it.word }.toTypedArray(), vm.lstWords.indexOf(item)
                        ))
                    }
                    true
                }
                if (vm.isEditMode)
                    mForward.visibility = View.GONE
            }

            override fun onItemClicked(view: View?) {
                if (vm.isSwipeStarted) {
                    mDragListView.resetSwipedViews(null)
                    vm.isSwipeStarted = false
                } else {
                    val item = view!!.tag as MUnitWord
                    if (vm.isEditMode)
                        edit(item)
                    else
                        speak(item.word)
                }
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }
}
