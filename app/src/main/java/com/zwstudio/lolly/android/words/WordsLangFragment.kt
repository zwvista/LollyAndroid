package com.zwstudio.lolly.android.words

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.swipe.ListSwipeHelper
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.zwstudio.lolly.android.DrawerListFragment
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ContentWordsLangBinding
import com.zwstudio.lolly.android.yesNoDialog
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.copyText
import com.zwstudio.lolly.data.misc.googleString
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.words.WordsLangViewModel
import com.zwstudio.lolly.domain.misc.MSelectItem
import com.zwstudio.lolly.domain.wpp.MLangWord
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import org.androidannotations.annotations.*
import java.util.*

private const val REQUEST_CODE = 1

@EFragment(R.layout.content_words_lang)
@OptionsMenu(R.menu.menu_words_lang)
class WordsLangFragment : DrawerListFragment(), TextToSpeech.OnInitListener {

    val vm: WordsLangViewModel by viewModels()
    lateinit var binding: ContentWordsLangBinding
    lateinit var tts: TextToSpeech

    @OptionsMenuItem
    lateinit var menuNormalMode: MenuItem
    @OptionsMenuItem
    lateinit var menuEditMode: MenuItem

    @ViewById
    lateinit var svTextFilter: SearchView
    @ViewById
    lateinit var spnScopeFilter: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ContentWordsLangBinding.inflate(inflater, container, false).apply {
            model = vm
            lifecycleOwner = this@WordsLangFragment
        }
        return binding.root
    }

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(R.string.words_lang)
        tts = TextToSpeech(requireContext(), this)

        svTextFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

        val lst = SettingsViewModel.lstScopeWordFilters
        val adapter = makeAdapter(requireContext(), android.R.layout.simple_spinner_item, lst) { v, position ->
            val tv = v.findViewById<TextView>(android.R.id.text1)
            tv.text = getItem(position)!!.label
            v
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        spnScopeFilter.adapter = adapter
        spnScopeFilter.setSelection(0)

        mDragListView.recyclerView.isVerticalScrollBarEnabled = true

        mRefreshLayout.setScrollingView(mDragListView.recyclerView)
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.app_color))
        mRefreshLayout.setOnRefreshListener { mRefreshLayout.postDelayed({ mRefreshLayout.isRefreshing = false }, 2000) }

        mDragListView.setSwipeListener(object : ListSwipeHelper.OnSwipeListenerAdapter() {
            override fun onItemSwipeStarted(item: ListSwipeItem?) {
                mRefreshLayout.isEnabled = false
            }

            override fun onItemSwipeEnded(item: ListSwipeItem?, swipedDirection: ListSwipeItem.SwipeDirection?) {
                mRefreshLayout.isEnabled = true
                when (swipedDirection) {
                    ListSwipeItem.SwipeDirection.LEFT -> vm.isSwipeStarted = true
                    ListSwipeItem.SwipeDirection.RIGHT -> vm.isSwipeStarted = true
                    else -> {}
                }
            }
        })

        mDragListView.setLayoutManager(LinearLayoutManager(requireContext()))

        vm.viewModelScope.launch {
            vm.getData()
            refreshListView()
            progressBar1.visibility = View.GONE
        }
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) return
        val locale = Locale.getAvailableLocales().find {
            "${it.language}_${it.country}" == vm.vmSettings.selectedVoice?.voicelang
        }
        if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return
        tts.language = locale
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun refreshListView() {
        val listAdapter = WordsLangItemAdapter(vm, mDragListView, tts)
        mDragListView.setAdapter(listAdapter, true)
    }

    @ItemSelect
    fun spnScopeFilterItemSelected(selected: Boolean, selectedItem: MSelectItem) {
        vm.scopeFilter = selectedItem.label
        vm.applyFilters()
        refreshListView()
    }

    @OptionsItem
    fun menuNormalMode() = setMenuMode(false)
    @OptionsItem
    fun menuEditMode() = setMenuMode(true)
    private fun setMenuMode(isEditMode: Boolean) {
        (if (isEditMode) menuEditMode else menuNormalMode).isChecked = true
        refreshListView()
    }

    @OptionsItem
    fun menuAdd() {
        WordsLangDetailActivity_.intent(this)
            .extra("word", vm.newLangWord()).startForResult(1)
    }

    @OnActivityResult(REQUEST_CODE)
    fun onResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK)
            mDragListView.resetSwipedViews(null)
    }

    private class WordsLangItemAdapter(val vm: WordsLangViewModel, val mDragListView: DragListView, val tts: TextToSpeech) : DragItemAdapter<MLangWord, WordsLangItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstWords
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_words_lang_edit, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.word
            holder.mText2.text = item.note
            holder.itemView.tag = item
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView
            var mText2: TextView
            var mEdit: TextView
            var mDelete: TextView
            var mMore: TextView
            var mForward: ImageView

            init {
                mText1 = itemView.findViewById(R.id.text1)
                mText2 = itemView.findViewById(R.id.text2)
                mEdit = itemView.findViewById(R.id.item_edit)
                mDelete = itemView.findViewById(R.id.item_delete)
                mMore = itemView.findViewById(R.id.item_more)
                mForward = itemView.findViewById(R.id.image_forward)
                initButtons()
            }

            fun edit(item: MLangWord) {
                WordsLangDetailActivity_.intent(itemView.context)
                    .extra("word", item).startForResult(REQUEST_CODE)
            }

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
                fun delete(item: MLangWord) {
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
                        val item = itemView.tag as MLangWord
                        edit(item)
                    }
                    true
                }
                mDelete.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MLangWord
                        delete(item)
                    }
                    true
                }
                mMore.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false

                        val item = itemView.tag as MLangWord
                        // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                        val builder = AlertDialog.Builder(itemView.context)
                            .setTitle(item.wordnote)
                            .setItems(arrayOf("Delete", "Edit", "Retrieve Note", "Copy Word", "Google Word", "Cancel")) { _, which ->
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
                                    else -> {}
                                }
                            }
                        builder.show()
                    }
                    true
                }
                mForward.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MLangWord
                        WordsDictActivity_.intent(itemView.context)
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
                    val item = view!!.tag as MLangWord
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
