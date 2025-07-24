package com.zwstudio.lolly.ui.words

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.MainActivity
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.copyText
import com.zwstudio.lolly.common.googleString
import com.zwstudio.lolly.common.openPage
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentWordsUnitBinding
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.ui.common.LollyListFragment
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.ui.common.yesNoDialog
import com.zwstudio.lolly.viewmodels.LollyListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WordsUnitFragment : LollyListFragment(), MenuProvider {

    val vm by viewModel<WordsUnitViewModel>()
    override val vmList: LollyListViewModel get() = vm
    var binding by autoCleared<FragmentWordsUnitBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentWordsUnitBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.svTextFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                vm.textFilter = newText
                return false
            }
        })
        binding.spnScopeFilter.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstScopeWordFilters) { it.label }
        setupListView(WordsUnitDragItem(requireContext(), R.layout.list_item_words_unit_edit))

        setFragmentResultListener("WordsUnitDetailFragment") { _, bundle ->
            if (bundle.getBoolean("isAdd"))
                menuAdd()
        }

        combine(vm.lstWords_, vm.isEditMode_, ::Pair).onEach {
            val listAdapter = WordsUnitItemAdapter(vm, mDragListView)
            mDragListView.setAdapter(listAdapter, true)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override suspend fun onRefresh() = vm.getDataInTextbook()

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_words_unit, menu)
        setEditMode(menu.findItem(if (vm.isEditMode) R.id.menuEditMode else R.id.menuNormalMode), vm.isEditMode)
    }

    private fun setEditMode(menuItem: MenuItem, isEditMode: Boolean) {
        vm.isEditMode = isEditMode
        menuItem.isChecked = true
    }

    fun menuAdd() =
        findNavController().navigate(WordsUnitFragmentDirections.actionWordsUnitFragmentToWordsUnitDetailFragment(vm.newUnitWord()))

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        fun getNotes(ifEmpty: Boolean) =
            vm.getNotes(ifEmpty, oneComplete = {
                mDragListView.adapter.notifyItemChanged(it)
            }, allComplete = {})

        fun clearNotes(ifEmpty: Boolean) =
            vm.clearNotes(ifEmpty, oneComplete = {
                mDragListView.adapter.notifyItemChanged(it)
            }, allComplete = {})

        return when (menuItem.itemId) {
            R.id.menuNormalMode -> {
                setEditMode(menuItem, false)
                true
            }
            R.id.menuEditMode -> {
                setEditMode(menuItem, true)
                true
            }
            R.id.menuAdd -> {
                menuAdd()
                true
            }
            R.id.menuRetrieveNotesAll -> {
                getNotes(false)
                true
            }
            R.id.menuRetrieveNotesEmpty -> {
                getNotes(true)
                true
            }
            R.id.menuClearNotesAll -> {
                clearNotes(false)
                true
            }
            R.id.menuClearNotesEmpty -> {
                clearNotes(true)
                true
            }
            R.id.menuBatch -> {
                findNavController().navigate(WordsUnitFragmentDirections.actionWordsUnitFragmentToWordsUnitBatchEditFragment(vm.lstWords.toTypedArray()))
                true
            }
            else -> false
        }
    }

    private class WordsUnitDragItem(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<TextView>(R.id.text3).text = clickedView.findViewById<TextView>(R.id.text3).text
            dragView.findViewById<View>(R.id.item_swipe).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background, null))
            dragView.findViewById<ImageView>(R.id.image_forward).visibility = View.GONE
        }
    }

    private class WordsUnitItemAdapter(val vm: WordsUnitViewModel, val mDragListView: DragListView) : DragItemAdapter<MUnitWord, WordsUnitItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstWords
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_words_unit_edit, parent, false)
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

        @SuppressLint("ClickableViewAccessibility")
        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView = itemView.findViewById(R.id.text1)
            var mText2: TextView = itemView.findViewById(R.id.text2)
            var mText3: TextView = itemView.findViewById(R.id.text3)
            var mForward: ImageView = itemView.findViewById(R.id.image_forward)
            var mHamburger: ImageView = itemView.findViewById(R.id.image_hamburger)
            val navController get() = (itemView.context as MainActivity).getNavController()

            init {
                mForward.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MUnitWord
                        navController.navigate(WordsUnitFragmentDirections.actionWordsUnitFragmentToWordsDictFragment(
                            vm.lstWords.map { it.word }.toTypedArray(), vm.lstWords.indexOf(item)
                        ))
                    }
                    true
                }
                if (vm.isEditMode)
                    mForward.visibility = View.GONE
                if (!(vm.isEditMode && vmSettings.isSingleUnitPart && vm.noFilter))
                    mHamburger.visibility = View.GONE
            }

            fun edit(item: MUnitWord) =
                navController.navigate(WordsUnitFragmentDirections.actionWordsUnitFragmentToWordsUnitDetailFragment(item))

            override fun onItemClicked(view: View?) {
                val item = itemView.tag as MUnitWord
                if (vm.isEditMode)
                    edit(item)
                else
                    speak(item.word)
            }

            override fun onItemLongClicked(view: View?): Boolean {
                val item = itemView.tag as MUnitWord
                // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                AlertDialog.Builder(itemView.context)
                    .setTitle(item.wordnote)
                    .setItems(arrayOf(
                        itemView.context.getString(R.string.action_delete),
                        itemView.context.getString(R.string.action_edit),
                        itemView.context.getString(R.string.action_get_note),
                        itemView.context.getString(R.string.action_clear_note),
                        itemView.context.getString(R.string.action_copy_word),
                        itemView.context.getString(R.string.action_google_word),
                        itemView.context.getString(R.string.action_online_dict),
                        itemView.context.getString(R.string.action_cancel),
                    )) { _, which ->
                        when (which) {
                            0 ->
                                yesNoDialog(itemView.context, "Are you sure you want to delete the word \"${item.word}\"?", {
                                    val pos = mDragListView.adapter.getPositionForItem(item)
                                    mDragListView.adapter.removeItem(pos)
                                    vm.delete(item)
                                }, {})
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
                            4 -> copyText(itemView.context, item.word)
                            5 -> googleString(itemView.context, item.word)
                            6 -> {
                                val url = vmSettings.selectedDictReference.urlString(item.word, vmSettings.lstAutoCorrect)
                                openPage(itemView.context, url)
                            }
                            else -> {}
                        }
                    }.show()
                return true
            }
        }
    }
}
