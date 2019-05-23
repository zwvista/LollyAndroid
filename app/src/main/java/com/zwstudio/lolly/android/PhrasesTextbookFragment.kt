package com.zwstudio.lolly.android

import android.annotation.SuppressLint
import android.speech.tts.TextToSpeech
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.swipe.ListSwipeHelper
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.zwstudio.lolly.data.PhrasesUnitViewModel
import com.zwstudio.lolly.data.copyText
import com.zwstudio.lolly.data.googleString
import com.zwstudio.lolly.domain.MUnitPhrase
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment
import java.util.*


@EFragment(R.layout.content_phrases_textbook)
class PhrasesTextbookFragment : DrawerListFragment(), TextToSpeech.OnInitListener {

    @Bean
    lateinit var vm: PhrasesUnitViewModel
    lateinit var tts: TextToSpeech

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(R.string.phrases_textbook)
        vm.compositeDisposable = compositeDisposable
        tts = TextToSpeech(context!!, this);
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) return
        val locale = Locale.getAvailableLocales().find {
            "${it.language}_${it.country}" == vm.vmSettings.selectedVoice?.voicelang
        }
        if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return
        tts.language = locale
    }

    override fun onResume() {
        super.onResume()
        compositeDisposable.add(vm.getDataInLang().subscribe {
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
            val listAdapter = PhrasesTextbookItemAdapter(vm, mDragListView, tts, compositeDisposable)
            mDragListView.setAdapter(listAdapter, true)
            progressBar1.visibility = View.GONE
        })
    }

    private class PhrasesTextbookItemAdapter(val vm: PhrasesUnitViewModel, val mDragListView: DragListView, val tts: TextToSpeech, val compositeDisposable: CompositeDisposable) : DragItemAdapter<MUnitPhrase, PhrasesTextbookItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstPhrases
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_phrases_textbook_edit, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.phrase
            holder.mText2.text = item.unitpartseqnum
            holder.mText3.text = item.translation
            holder.itemView.tag = item
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        internal inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView
            var mText2: TextView
            var mText3: TextView
            var mEdit: TextView
            var mDelete: TextView
            var mMore: TextView
            var mSpeak: ImageView

            init {
                mText1 = itemView.findViewById(R.id.text1)
                mText2 = itemView.findViewById(R.id.text2)
                mText3 = itemView.findViewById(R.id.text3)
                mEdit = itemView.findViewById(R.id.item_edit)
                mDelete = itemView.findViewById(R.id.item_delete)
                mMore = itemView.findViewById(R.id.item_more)
                mSpeak = itemView.findViewById(R.id.image_speak)
                initButtons()
            }

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
                fun edit(item: MUnitPhrase) {
                    PhrasesTextbookDetailActivity_.intent(itemView.context).extra("phrase", item).start()
                }
                fun delete(item: MUnitPhrase) {
                    yesNoDialog(itemView.context, "Are you sure you want to delete the phrase \"${item.phrase}\"?", {
                        val pos = mDragListView.adapter.getPositionForItem(item)
                        mDragListView.adapter.removeItem(pos)
//                        compositeDisposable.add(vm.delete(item.id).subscribe())
                        vm.isSwipeStarted = false
                    }, {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false
                    })
                }
                mEdit.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MUnitPhrase
                        edit(item)
                    }
                    true
                }
                mDelete.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MUnitPhrase
                        delete(item)
                    }
                    true
                }
                mMore.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false

                        val item = itemView.tag as MUnitPhrase
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
                mSpeak.setOnTouchListener { _, event ->
                    val item = itemView.tag as MUnitPhrase
                    if (event.action == MotionEvent.ACTION_DOWN)
                        tts.speak(item.phrase, TextToSpeech.QUEUE_FLUSH, null)
                    true
                }
            }

            override fun onItemClicked(view: View?) {
                if (vm.isSwipeStarted) {
                    mDragListView.resetSwipedViews(null)
                    vm.isSwipeStarted = false
                } else {
                    val item = view!!.tag as MUnitPhrase
                    PhrasesTextbookDetailActivity_.intent(view.context)
                            .extra("phrase", item).start()
                }
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }

}
