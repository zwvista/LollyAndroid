package com.zwstudio.lolly.android.phrases

import android.annotation.SuppressLint
import android.app.Activity
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.swipe.ListSwipeHelper
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.zwstudio.lolly.android.DrawerListFragment
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.yesNoDialog
import com.zwstudio.lolly.data.phrases.PhrasesLangViewModel
import com.zwstudio.lolly.data.misc.copyText
import com.zwstudio.lolly.data.misc.googleString
import com.zwstudio.lolly.domain.misc.MSelectItem
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*
import java.util.*

private const val REQUEST_CODE = 1

@EFragment(R.layout.content_phrases_lang)
@OptionsMenu(R.menu.menu_phrases_lang)
class PhrasesLangFragment : DrawerListFragment(), TextToSpeech.OnInitListener {

    @Bean
    lateinit var vm: PhrasesLangViewModel
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
        activity?.title = resources.getString(R.string.phrases_lang)
        tts = TextToSpeech(context!!, this)
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) return
        val locale = Locale.getAvailableLocales().find {
            "${it.language}_${it.country}" == vm.vmSettings.selectedVoice?.voicelang
        }
        if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return
        tts.language = locale

        val lst = vm.vmSettings.lstScopePhraseFilters
        val adapter = object : ArrayAdapter<MSelectItem>(context!!, android.R.layout.simple_spinner_item, lst) {
            fun convert(v: View, position: Int): View {
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)!!.label
                return v
            }
            override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getView(position, convertView, parent), position)
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getDropDownView(position, convertView, parent), position)
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        spnScopeFilter.adapter = adapter
        spnScopeFilter.setSelection(0)
    }

    override fun onResume() {
        super.onResume()
        compositeDisposable.add(vm.getData().subscribe {
            mDragListView.recyclerView.isVerticalScrollBarEnabled = true

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
                        ListSwipeItem.SwipeDirection.LEFT -> vm.isSwipeStarted = true
                        ListSwipeItem.SwipeDirection.RIGHT -> vm.isSwipeStarted = true
                        else -> {}
                    }
                }
            })

            mDragListView.setLayoutManager(LinearLayoutManager(context!!))
            setMenuMode(false)
            progressBar1.visibility = View.GONE
        })
    }

    @OptionsItem
    fun menuNormalMode() = setMenuMode(false)
    @OptionsItem
    fun menuEditMode() = setMenuMode(true)
    private fun setMenuMode(isEditMode: Boolean) {
        (if (isEditMode) menuEditMode else menuNormalMode).isChecked = true
        val listAdapter = PhrasesLangItemAdapter(vm, mDragListView, isEditMode, tts, compositeDisposable)
        mDragListView.setAdapter(listAdapter, true)
    }

    @OptionsItem
    fun menuAdd() {
        PhrasesLangDetailActivity_.intent(this)
            .extra("phrase", vm.newLangPhrase()).startForResult(REQUEST_CODE)
    }


    @OnActivityResult(REQUEST_CODE)
    fun onResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK)
            mDragListView.resetSwipedViews(null)
    }

    private class PhrasesLangItemAdapter(val vm: PhrasesLangViewModel, val mDragListView: DragListView, val isEditMode: Boolean, val tts: TextToSpeech, val compositeDisposable: CompositeDisposable) : DragItemAdapter<MLangPhrase, PhrasesLangItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstPhrases
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_phrases_lang_edit, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.phrase
            holder.mText2.text = item.translation
            holder.itemView.tag = item
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        internal inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView
            var mText2: TextView
            var mEdit: TextView
            var mDelete: TextView
            var mMore: TextView

            init {
                mText1 = itemView.findViewById(R.id.text1)
                mText2 = itemView.findViewById(R.id.text2)
                mEdit = itemView.findViewById(R.id.item_edit)
                mDelete = itemView.findViewById(R.id.item_delete)
                mMore = itemView.findViewById(R.id.item_more)
                initButtons()
            }

            fun edit(item: MLangPhrase) {
                PhrasesLangDetailActivity_.intent(itemView.context)
                    .extra("phrase", item).startForResult(REQUEST_CODE)
            }

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
                fun delete(item: MLangPhrase) {
                    yesNoDialog(itemView.context, "Are you sure you want to delete the phrase \"${item.phrase}\"?", {
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
                        val item = itemView.tag as MLangPhrase
                        edit(item)
                    }
                    true
                }
                mDelete.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MLangPhrase
                        delete(item)
                    }
                    true
                }
                mMore.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false

                        val item = itemView.tag as MLangPhrase
                        // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                        val builder = AlertDialog.Builder(itemView.context)
                            .setTitle(item.phrase)
                            .setItems(arrayOf("Delete", "Edit", "Copy Phrase", "Google Phrase", "Cancel")) { _, which ->
                                when (which) {
                                    0 -> delete(item)
                                    1 -> edit(item)
                                    2 -> itemView.copyText(item.phrase)
                                    3 -> itemView.googleString(item.phrase)
                                    else -> {}
                                }
                            }
                        builder.show()
                    }
                    true
                }
            }

            override fun onItemClicked(view: View?) {
                if (vm.isSwipeStarted) {
                    mDragListView.resetSwipedViews(null)
                    vm.isSwipeStarted = false
                } else {
                    val item = view!!.tag as MLangPhrase
                    if (isEditMode)
                        edit(item)
                    else
                        tts.speak(item.phrase, TextToSpeech.QUEUE_FLUSH, null)
                }
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }

}
