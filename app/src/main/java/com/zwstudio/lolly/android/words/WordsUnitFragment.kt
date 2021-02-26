package com.zwstudio.lolly.android.words

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.swipe.ListSwipeHelper
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.zwstudio.lolly.android.DrawerListFragment
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ContentWordsUnitBinding
import com.zwstudio.lolly.android.yesNoDialog
import com.zwstudio.lolly.data.misc.*
import com.zwstudio.lolly.data.words.WordsUnitViewModel
import com.zwstudio.lolly.domain.misc.MSelectItem
import com.zwstudio.lolly.domain.wpp.MUnitWord
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*
import java.util.*


private const val REQUEST_CODE = 1

@EFragment(R.layout.content_words_unit)
@OptionsMenu(R.menu.menu_words_unit)
class WordsUnitFragment : DrawerListFragment(), TextToSpeech.OnInitListener {

    @Bean
    lateinit var vm: WordsUnitViewModel
    lateinit var tts: TextToSpeech

    @OptionsMenuItem
    lateinit var menuNormalMode: MenuItem
    @OptionsMenuItem
    lateinit var menuEditMode: MenuItem

    @ViewById
    lateinit var svTextFilter: SearchView
    @ViewById
    lateinit var spnScopeFilter: Spinner

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(R.string.words_unit)
        vm.compositeDisposable = compositeDisposable
        tts = TextToSpeech(context!!, this)

        svTextFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                vm.applyFilters()
                refreshListView()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                vm.textFilter.value = newText
                if (newText.isEmpty())
                    refreshListView()
                return false
            }
        })

        val lst = SettingsViewModel.lstScopeWordFilters
        val adapter = makeAdapter(context!!, android.R.layout.simple_spinner_item, lst) { v, position ->
            val tv = v.findViewById<TextView>(android.R.id.text1)
            tv.text = getItem(position)!!.label
            v
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        spnScopeFilter.adapter = adapter
        spnScopeFilter.setSelection(0)

        DataBindingUtil.inflate<ContentWordsUnitBinding>(LayoutInflater.from(activity!!), R.layout.content_words_unit,
                activity!!.findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@WordsUnitFragment
            model = vm
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
        compositeDisposable.add(vm.getDataInTextbook().subscribe {
            mDragListView.recyclerView.isVerticalScrollBarEnabled = true
            mDragListView.setDragListListener(object : DragListView.DragListListenerAdapter() {
                override fun onItemDragStarted(position: Int) {
                    mRefreshLayout.isEnabled = false
                    Toast.makeText(mDragListView.context, "Start - position: $position", Toast.LENGTH_SHORT).show()
                }
                override fun onItemDragEnded(fromPosition: Int, toPosition: Int) {
                    mRefreshLayout.isEnabled = true
                    Toast.makeText(mDragListView.context, "End - position: $toPosition", Toast.LENGTH_SHORT).show()
                    vm.reindex {}
                }
            })

            mRefreshLayout.setScrollingView(mDragListView.recyclerView)
            mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.app_color))
            mRefreshLayout.setOnRefreshListener { mRefreshLayout.postDelayed({ mRefreshLayout.isRefreshing = false }, 2000) }

            mDragListView.setSwipeListener(object : ListSwipeHelper.OnSwipeListenerAdapter() {
                override fun onItemSwipeStarted(item: ListSwipeItem?) {
                    mRefreshLayout.isEnabled = false
                }
                override fun onItemSwipeEnded(item: ListSwipeItem?, swipedDirection: ListSwipeItem.SwipeDirection?) {
                    mRefreshLayout.isEnabled = true
                    when (swipedDirection) {
                        ListSwipeItem.SwipeDirection.LEFT -> vm.isSwipeStarted.value = true
                        ListSwipeItem.SwipeDirection.RIGHT -> vm.isSwipeStarted.value = true
                        else -> {}
                    }
                }
            })

            mDragListView.setLayoutManager(LinearLayoutManager(context!!))
            refreshListView()
            mDragListView.setCanDragHorizontally(false)
            mDragListView.setCustomDragItem(WordsUnitDragItem(context!!, R.layout.list_item_words_unit_edit))
            progressBar1.visibility = View.GONE
        })
    }

    private fun refreshListView() {
        val listAdapter = WordsUnitItemAdapter(vm, mDragListView, tts, compositeDisposable)
        mDragListView.setAdapter(listAdapter, true)
    }

    @ItemSelect
    fun spnScopeFilterItemSelected(selected: Boolean, selectedItem: MSelectItem) {
        vm.scopeFilter.value = selectedItem.label
        vm.applyFilters()
        refreshListView()
    }

    @OptionsItem
    fun menuNormalMode() = setMenuMode(false)
    @OptionsItem
    fun menuEditMode() = setMenuMode(true)
    private fun setMenuMode(isEditMode: Boolean) {
        vm.isEditMode.value = isEditMode
        (if (isEditMode) menuEditMode else menuNormalMode).isChecked = true
        refreshListView()
    }

    @OptionsItem
    fun menuAdd() {
        WordsUnitDetailActivity_.intent(this)
            .extra("word", vm.newUnitWord()).startForResult(REQUEST_CODE)
    }

    @OnActivityResult(REQUEST_CODE)
    fun onResult(resultCode: Int) {
        if (resultCode == RESULT_OK)
            menuAdd()
    }

    @OptionsItem
    fun menuNotesAll() = getNotes(false)
    @OptionsItem
    fun menuNotesEmpty() = getNotes(true)
    private fun getNotes(ifEmpty: Boolean) {
        val handler = Handler(Looper.getMainLooper())
        progressBar1.visibility = View.VISIBLE
        vm.getNotes(ifEmpty, oneComplete = {}, allComplete = {
            handler.post {
                mDragListView.adapter.notifyDataSetChanged()
                progressBar1.visibility = View.GONE
            }
        })
    }

    @OptionsItem
    fun menuBatch() {
        WordsUnitBatchActivity_.intent(this)
            .extra("list", vm.lstWords.value!!.toTypedArray()).start()
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

    private class WordsUnitItemAdapter(val vm: WordsUnitViewModel, val mDragListView: DragListView, val tts: TextToSpeech, val compositeDisposable: CompositeDisposable) : DragItemAdapter<MUnitWord, WordsUnitItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstWords.value!!
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
            var mText1: TextView
            var mText2: TextView
            var mText3: TextView
            var mEdit: TextView
            var mDelete: TextView
            var mMore: TextView
            var mForward: ImageView
            var mHamburger: ImageView

            init {
                mText1 = itemView.findViewById(R.id.text1)
                mText2 = itemView.findViewById(R.id.text2)
                mText3 = itemView.findViewById(R.id.text3)
                mEdit = itemView.findViewById(R.id.item_edit)
                mDelete = itemView.findViewById(R.id.item_delete)
                mMore = itemView.findViewById(R.id.item_more)
                mForward = itemView.findViewById(R.id.image_forward)
                mHamburger = itemView.findViewById(R.id.image_hamburger)
                initButtons()
            }

            fun edit(item: MUnitWord) {
                WordsUnitDetailActivity_.intent(itemView.context)
                        .extra("word", item).startForResult(REQUEST_CODE)
            }

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
                fun delete(item: MUnitWord) {
                    yesNoDialog(itemView.context, "Are you sure you want to delete the word \"${item.word}\"?", {
                        val pos = mDragListView.adapter.getPositionForItem(item)
                        mDragListView.adapter.removeItem(pos)
                        compositeDisposable.add(vm.delete(item).subscribe())
                        vm.isSwipeStarted.value = false
                    }, {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted.value = false
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
                        vm.isSwipeStarted.value = false

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
                                        compositeDisposable.add(vm.getNote(index).subscribe {
                                            mDragListView.adapter.notifyItemChanged(index)
                                        })
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
                        WordsDictActivity_.intent(itemView.context)
                                .extra("list", vm.lstWords.value!!.map { it.word }.toTypedArray())
                                .extra("index", vm.lstWords.value!!.indexOf(item)).start()
                    }
                    true
                }
                if (vm.isEditMode.value!!)
                    mForward.visibility = View.GONE
                if (!(vm.isEditMode.value!! && vm.vmSettings.isSingleUnitPart))
                    mHamburger.visibility = View.GONE
            }

            override fun onItemClicked(view: View?) {
                if (vm.isSwipeStarted.value!!) {
                    mDragListView.resetSwipedViews(null)
                    vm.isSwipeStarted.value = false
                } else {
                    val item = view!!.tag as MUnitWord
                    if (vm.isEditMode.value!!)
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
