package com.zwstudio.lolly.ui.words

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.MainActivity
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.copyText
import com.zwstudio.lolly.common.getPreferredRangeFromArray
import com.zwstudio.lolly.common.googleString
import com.zwstudio.lolly.common.openPage
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentWordsTextbookBinding
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.ui.common.LollyListFragment
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.ui.common.yesNoDialog
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WordsTextbookFragment : LollyListFragment() {

    val vm by viewModel<WordsUnitViewModel>()
    override val vmDrawerList: DrawerListViewModel get() = vm
    var binding by autoCleared<FragmentWordsTextbookBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                vm.textFilter = newText
                return false
            }
        })
        binding.spnTextbookFilter.adapter = makeCustomAdapter(requireContext(), vmSettings.lstTextbookFilters) { it.label }
        binding.spnScopeFilter.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstScopeWordFilters) { it.label }
        setupListView()

        vm.lstWords_.onEach {
            val listAdapter = WordsTextbookItemAdapter(vm, mDragListView)
            mDragListView.setAdapter(listAdapter, true)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vm.isBusy_.onEach {
            progressBar1.visibility = if (it) View.VISIBLE else View.GONE
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            vm.getDataInLang()
        }
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

        @SuppressLint("ClickableViewAccessibility")
        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView = itemView.findViewById(R.id.text1)
            var mText2: TextView = itemView.findViewById(R.id.text2)
            var mText3: TextView = itemView.findViewById(R.id.text3)
            var mForward: ImageView = itemView.findViewById(R.id.image_forward)
            val navController get() = (itemView.context as MainActivity).getNavController()

            init {
                mForward.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MUnitWord
                        val index = vm.lstWords.indexOf(item)
                        val (start, end) = getPreferredRangeFromArray(index, vm.lstWords.size, 50)
                        navController.navigate(WordsTextbookFragmentDirections.actionWordsTextbookFragmentToWordsDictFragment(
                            vm.lstWords.subList(start, end).map { it.word }.toTypedArray(), index
                        ))
                    }
                    true
                }
            }

            fun edit(item: MUnitWord) =
                navController.navigate(WordsTextbookFragmentDirections.actionWordsTextbookFragmentToWordsTextbookDetailFragment(item))

            override fun onItemClicked(view: View?) {
                val item = itemView.tag as MUnitWord
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
