package com.zwstudio.lolly.android.words

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.android.DrawerListFragment
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentWordsTextbookBinding
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.android.yesNoDialog
import com.zwstudio.lolly.data.DrawerListViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.copyText
import com.zwstudio.lolly.data.misc.googleString
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.words.WordsUnitViewModel
import com.zwstudio.lolly.domain.misc.MSelectItem
import com.zwstudio.lolly.domain.wpp.MUnitWord
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ItemSelect

class WordsTextbookFragment : DrawerListFragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsUnitViewModel>() }
    override val vmDrawerList: DrawerListViewModel? get() = vm
    var binding by autoCleared<FragmentWordsTextbookBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWordsTextbookBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    @AfterViews
    override fun afterViews() {
        super.afterViews()
        activity?.title = resources.getString(R.string.words_textbook)

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
        binding.spnTextbookFilter.setSelection(0)
        binding.spnScopeFilter.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstScopeWordFilters) { it.label }
        binding.spnScopeFilter.setSelection(0)

        setupListView()
        compositeDisposable.add(vm.getDataInLang().subscribe {
            refreshListView()
            progressBar1.visibility = View.GONE
        })
    }

    private fun refreshListView() {
        val listAdapter = WordsTextbookItemAdapter(vm, mDragListView, tts, compositeDisposable)
        mDragListView.setAdapter(listAdapter, true)
    }

    @ItemSelect
    fun spnTextbookFilterItemSelected(selected: Boolean, selectedItem: MSelectItem) {
        vm.textbookFilter = selectedItem.value
        vm.applyFilters()
        refreshListView()
    }

    @ItemSelect
    fun spnScopeFilterItemSelected(selected: Boolean, selectedItem: MSelectItem) {
        vm.scopeFilter = selectedItem.label
        vm.applyFilters()
        refreshListView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_words_textbook, menu)
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
            else -> super.onOptionsItemSelected(item)
        }

    private class WordsTextbookItemAdapter(val vm: WordsUnitViewModel, val mDragListView: DragListView, val tts: TextToSpeech, val compositeDisposable: CompositeDisposable) : DragItemAdapter<MUnitWord, WordsTextbookItemAdapter.ViewHolder>() {

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

            init {
                initButtons()
            }

            fun edit(item: MUnitWord) {
                WordsTextbookDetailFragment_.intent(itemView.context).extra("word", item).start()
            }

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
                fun delete(item: MUnitWord) {
                    yesNoDialog(itemView.context, "Are you sure you want to delete the word \"${item.word}\"?", {
                        val pos = mDragListView.adapter.getPositionForItem(item)
                        mDragListView.adapter.removeItem(pos)
                        compositeDisposable.add(vm.delete(item).subscribe())
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
                        val builder = AlertDialog.Builder(itemView.context)
                            .setTitle(item.wordnote)
                            .setItems(arrayOf("Delete", "Edit", "Retrieve Note", "Copy Word", "Google Word", "Cancel")) { _, which ->
                                when (which) {
                                    0 -> delete(item)
                                    1 -> edit(item)
                                    2 -> {
                                        val index = itemList.indexOf(item)
                                        compositeDisposable.add(vm.getNote(index).subscribe {
                                            mDragListView.adapter.notifyItemChanged(index)
                                        })
                                    }
                                    3 -> itemView.copyText(item.word)
                                    4 -> itemView.googleString(item.word)
                                    else -> {}
                                }
                            }
                        builder.show()
                    }
                    true
                }
                mForward.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MUnitWord
                        WordsDictFragment_.intent(itemView.context)
                                .extra("list", vm.lstWords.map { it.word }.toTypedArray())
                                .extra("index", vm.lstWords.indexOf(item)).start()
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
                        tts.speak(item.word, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }

}
