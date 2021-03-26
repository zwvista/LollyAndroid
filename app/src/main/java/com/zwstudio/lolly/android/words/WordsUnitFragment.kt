package com.zwstudio.lolly.android.words

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.viewModelScope
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.android.DrawerListFragment
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ContentWordsUnitBinding
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.android.yesNoDialog
import com.zwstudio.lolly.data.DrawerListViewModel
import com.zwstudio.lolly.data.misc.*
import com.zwstudio.lolly.data.words.WordsUnitViewModel
import com.zwstudio.lolly.domain.misc.MSelectItem
import com.zwstudio.lolly.domain.wpp.MUnitWord
import kotlinx.coroutines.launch
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ItemSelect
import org.androidannotations.annotations.OnActivityResult


private const val REQUEST_CODE = 1

@EFragment(R.layout.content_words_unit)
class WordsUnitFragment : DrawerListFragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsUnitViewModel>() }
    override val vmDrawerList: DrawerListViewModel? get() = vm
    var binding by autoCleared<ContentWordsUnitBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ContentWordsUnitBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    @AfterViews
    override fun afterViews() {
        super.afterViews()
        activity?.title = resources.getString(R.string.words_unit)

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

        binding.spnScopeFilter.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstScopeWordFilters) { it.label }
        binding.spnScopeFilter.setSelection(0)

        setupListView(WordsUnitDragItem(requireContext(), R.layout.list_item_words_unit_edit))
        vm.viewModelScope.launch {
            vm.getDataInTextbook()
            refreshListView()
            progressBar1.visibility = View.GONE
        }
    }

    private fun refreshListView() {
        val listAdapter = WordsUnitItemAdapter(vm, mDragListView, tts)
        mDragListView.setAdapter(listAdapter, true)
    }

    @ItemSelect
    fun spnScopeFilterItemSelected(selected: Boolean, selectedItem: MSelectItem) {
        vm.scopeFilter = selectedItem.label
        vm.applyFilters()
        refreshListView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_words_unit, menu)
        setEditMode(menu.findItem(if (vm.isEditMode) R.id.menuEditMode else R.id.menuNormalMode), vm.isEditMode)
    }

    fun setEditMode(item: MenuItem, isEditMode: Boolean) {
        vm.isEditMode = isEditMode
        item.isChecked = true
        refreshListView()
    }

    fun menuAdd() {
        WordsUnitDetailFragment_.intent(this)
            .extra("word", vm.newUnitWord()).startForResult(REQUEST_CODE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        fun getNotes(ifEmpty: Boolean) {
            val handler = Handler(Looper.getMainLooper())
            progressBar1.visibility = View.VISIBLE
            vm.getNotes(ifEmpty, oneComplete = {}, allComplete = {
                handler.post {
                    mDragListView.adapter.notifyDataSetChanged()
                    progressBar1.visibility = View.GONE
                }
            })
        }
        fun clearNotes(ifEmpty: Boolean) {
            val handler = Handler(Looper.getMainLooper())
            progressBar1.visibility = View.VISIBLE
            vm.clearNotes(ifEmpty, oneComplete = {}, allComplete = {
                handler.post {
                    mDragListView.adapter.notifyDataSetChanged()
                    progressBar1.visibility = View.GONE
                }
            })
        }
        return when (item.itemId) {
            R.id.menuNormalMode -> {
                setEditMode(item,false)
                true
            }
            R.id.menuEditMode -> {
                setEditMode(item,true)
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
                WordsUnitBatchEditFragment_.intent(this)
                    .extra("list", vm.lstWords.toTypedArray()).start()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @OnActivityResult(REQUEST_CODE)
    fun onResult(resultCode: Int) {
        if (resultCode == RESULT_OK)
            menuAdd()
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

    private class WordsUnitItemAdapter(val vm: WordsUnitViewModel, val mDragListView: DragListView, val tts: TextToSpeech) : DragItemAdapter<MUnitWord, WordsUnitItemAdapter.ViewHolder>() {

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

        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView = itemView.findViewById(R.id.text1)
            var mText2: TextView = itemView.findViewById(R.id.text2)
            var mText3: TextView = itemView.findViewById(R.id.text3)
            var mEdit: TextView = itemView.findViewById(R.id.item_edit)
            var mDelete: TextView = itemView.findViewById(R.id.item_delete)
            var mMore: TextView = itemView.findViewById(R.id.item_more)
            var mForward: ImageView = itemView.findViewById(R.id.image_forward)
            var mHamburger: ImageView = itemView.findViewById(R.id.image_hamburger)

            init {
                initButtons()
            }

            fun edit(item: MUnitWord) {
                WordsUnitDetailFragment_.intent(itemView.context)
                        .extra("word", item).startForResult(REQUEST_CODE)
            }

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
                        val builder = AlertDialog.Builder(itemView.context)
                            .setTitle(item.wordnote)
                            .setItems(arrayOf("Delete", "Edit", "Retrieve Note", "Copy Word", "Google Word", "Online Dictionary", "Cancel")) { _, which ->
                                when (which) {
                                    0 -> delete(item)
                                    1 -> edit(item)
                                    2 -> {
                                        val index = itemList.indexOf(item)
                                        vm.getNote(index)
                                        mDragListView.adapter.notifyItemChanged(index)
                                    }
                                    3 -> itemView.copyText(item.word)
                                    4 -> itemView.googleString(item.word)
                                    5 -> {
                                        val itemDict = vm.vmSettings.lstDictsReference.find { it.dictname == vm.vmSettings.selectedDictReference.dictname }!!
                                        val url = itemDict.urlString(item.word, vm.vmSettings.lstAutoCorrect)
                                        itemView.openPage(url)
                                    }
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
                if (!(vm.isEditMode && vm.vmSettings.isSingleUnitPart))
                    mHamburger.visibility = View.GONE
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
