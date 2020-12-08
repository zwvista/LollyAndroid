package com.zwstudio.lolly.android.phrases

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.swipe.ListSwipeHelper
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.zwstudio.lolly.android.DrawerListFragment
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.yesNoDialog
import com.zwstudio.lolly.data.phrases.PhrasesUnitViewModel
import com.zwstudio.lolly.data.misc.copyText
import com.zwstudio.lolly.data.misc.googleString
import com.zwstudio.lolly.domain.misc.MSelectItem
import com.zwstudio.lolly.domain.wpp.MUnitPhrase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*
import java.util.*

private const val REQUEST_CODE = 1

@EFragment(R.layout.content_phrases_unit)
@OptionsMenu(R.menu.menu_phrases_unit)
class PhrasesUnitFragment : DrawerListFragment(), TextToSpeech.OnInitListener {

    @Bean
    lateinit var vm: PhrasesUnitViewModel
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
        activity?.title = resources.getString(R.string.phrases_unit)
        vm.compositeDisposable = compositeDisposable
        tts = TextToSpeech(context!!, this)

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
                        ListSwipeItem.SwipeDirection.LEFT -> vm.isSwipeStarted = true
                        ListSwipeItem.SwipeDirection.RIGHT -> vm.isSwipeStarted = true
                        else -> {}
                    }
                }
            })

            mDragListView.setLayoutManager(LinearLayoutManager(context!!))
            setMenuMode(false)
            mDragListView.setCanDragHorizontally(false)
            mDragListView.setCustomDragItem(PhrasesUnitDragItem(context!!, R.layout.list_item_phrases_unit_edit))
            progressBar1.visibility = View.GONE
        })
    }

    @OptionsItem
    fun menuNormalMode() = setMenuMode(false)
    @OptionsItem
    fun menuEditMode() = setMenuMode(true)
    private fun setMenuMode(isEditMode: Boolean) {
        (if (isEditMode) menuEditMode else menuNormalMode).isChecked = true
        val listAdapter = PhrasesUnitItemAdapter(vm, mDragListView, isEditMode, tts, compositeDisposable)
        mDragListView.setAdapter(listAdapter, true)
    }

    @OptionsItem
    fun menuAdd() {
        PhrasesUnitDetailActivity_.intent(this)
            .extra("phrase", vm.newUnitPhrase()).startForResult(REQUEST_CODE)
    }

    @OnActivityResult(REQUEST_CODE)
    fun onResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK)
            mDragListView.resetSwipedViews(null)
    }

    @OptionsItem
    fun menuBatch() {
        PhrasesUnitBatchActivity_.intent(this)
            .extra("list", vm.lstPhrases.toTypedArray()).start()
    }

    private class PhrasesUnitDragItem internal constructor(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<TextView>(R.id.text3).text = clickedView.findViewById<TextView>(R.id.text3).text
            dragView.findViewById<View>(R.id.item_swipe).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background))
        }
    }

    private class PhrasesUnitItemAdapter(val vm: PhrasesUnitViewModel, val mDragListView: DragListView, val isEditMode: Boolean, val tts: TextToSpeech, val compositeDisposable: CompositeDisposable) : DragItemAdapter<MUnitPhrase, PhrasesUnitItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstPhrases
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_phrases_unit_edit, parent, false)
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

        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView
            var mText2: TextView
            var mText3: TextView
            var mEdit: TextView
            var mDelete: TextView
            var mMore: TextView
            var mHamburger: ImageView

            init {
                mText1 = itemView.findViewById(R.id.text1)
                mText2 = itemView.findViewById(R.id.text2)
                mText3 = itemView.findViewById(R.id.text3)
                mEdit = itemView.findViewById(R.id.item_edit)
                mDelete = itemView.findViewById(R.id.item_delete)
                mMore = itemView.findViewById(R.id.item_more)
                mHamburger = itemView.findViewById(R.id.image_hamburger)
                initButtons()
            }

            fun edit(item: MUnitPhrase) {
                PhrasesUnitDetailActivity_.intent(itemView.context)
                    .extra("phrase", item).startForResult(REQUEST_CODE)
            }

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
                fun delete(item: MUnitPhrase) {
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
                if (!(isEditMode && vm.vmSettings.isSingleUnitPart))
                    mHamburger.visibility = View.GONE
            }

            override fun onItemClicked(view: View?) {
                if (vm.isSwipeStarted) {
                    mDragListView.resetSwipedViews(null)
                    vm.isSwipeStarted = false
                } else {
                    val item = view!!.tag as MUnitPhrase
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
