package com.zwstudio.lolly.android.words

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.android.LollySwipeRefreshLayout
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.words.WordsUnitViewModel
import com.zwstudio.lolly.domain.misc.MSelectItem
import com.zwstudio.lolly.domain.wpp.MUnitWord
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_words_unit_batch)
@OptionsMenu(R.menu.menu_save)
class WordsUnitBatchActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: WordsUnitViewModel

    @ViewById
    lateinit var chkUnit: CheckBox
    @ViewById
    lateinit var spnUnit: Spinner
    @ViewById
    lateinit var chkPart: CheckBox
    @ViewById
    lateinit var spnPart: Spinner
    @ViewById
    lateinit var chkSeqNum: CheckBox
    @ViewById
    lateinit var etSeqNum: EditText
    @ViewById(R.id.drag_list_view)
    lateinit var mDragListView: DragListView
    @ViewById(R.id.swipe_refresh_layout)
    lateinit var mRefreshLayout: LollySwipeRefreshLayout

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        vm.lstWords = (intent.getSerializableExtra("list") as Array<MUnitWord>).toList()
        chkUnit(); chkPart(); chkSeqNum()
        run {
            val lst = vm.vmSettings.lstUnits
            val adapter = makeAdapter(this, android.R.layout.simple_spinner_item, lst) { v, position ->
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)!!.label
                v
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnUnit.adapter = adapter
            spnUnit.setSelection(vm.vmSettings.lstUnits.indexOfFirst { it.value == vm.vmSettings.usunitto })
        }

        run {
            val lst = vm.vmSettings.lstParts
            val adapter = makeAdapter(this, android.R.layout.simple_spinner_item, lst) { v, position ->
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)!!.label
                v
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnPart.adapter = adapter
            spnPart.setSelection(vm.vmSettings.lstParts.indexOfFirst { it.value == vm.vmSettings.uspartto })
        }

        mDragListView.recyclerView.isVerticalScrollBarEnabled = true

        mRefreshLayout.setScrollingView(mDragListView.recyclerView)
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_color))
        mRefreshLayout.setOnRefreshListener { mRefreshLayout.postDelayed({ mRefreshLayout.isRefreshing = false }, 2000) }

        mDragListView.setLayoutManager(LinearLayoutManager(this))
        val listAdapter = WordsUnitBatchItemAdapter(vm)
        mDragListView.setAdapter(listAdapter, true)
        mDragListView.setCanDragHorizontally(false)
        mDragListView.setCustomDragItem(WordsUnitBatchDragItem(this, R.layout.list_item_words_unit_batch))
    }

    @CheckedChange
    fun chkUnit() {
        spnUnit.isEnabled = chkUnit.isChecked
    }

    @CheckedChange
    fun chkPart() {
        spnPart.isEnabled = chkPart.isChecked
    }

    @CheckedChange
    fun chkSeqNum() {
        etSeqNum.isEnabled = chkSeqNum.isChecked
    }

    @OptionsItem
    fun menuSave() {
        if (!chkUnit.isChecked && !chkPart.isChecked && !chkSeqNum.isChecked) return
        for ((i, item) in vm.lstWords.withIndex()) {
            val v = mDragListView.recyclerView.findViewHolderForAdapterPosition(i) as WordsUnitBatchItemAdapter.ViewHolder
            if (v.mCheckmark.visibility == View.INVISIBLE) continue
            if (chkUnit.isChecked) item.unit = (spnUnit.selectedItem as MSelectItem).value
            if (chkPart.isChecked) item.part = (spnPart.selectedItem as MSelectItem).value
            if (chkSeqNum.isChecked) item.seqnum += etSeqNum.text.toString().toInt()
            compositeDisposable.add(vm.update(item).subscribe())
        }
        finish()
    }

    private class WordsUnitBatchDragItem internal constructor(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<View>(R.id.item_swipe).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background, null))
        }
    }

    private class WordsUnitBatchItemAdapter(val vm: WordsUnitViewModel) : DragItemAdapter<MUnitWord, WordsUnitBatchItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstWords
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_words_unit_batch, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.wordnote
            holder.mText2.text = item.unitpartseqnum
            holder.itemView.tag = item
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        internal inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView
            var mText2: TextView
            var mCheckmark: ImageView
            var mItemSwipe: View

            init {
                mText1 = itemView.findViewById(R.id.text1)
                mText2 = itemView.findViewById(R.id.text2)
                mCheckmark = itemView.findViewById(R.id.image_checkmark)
                mItemSwipe = itemView.findViewById(R.id.item_swipe)
            }

            override fun onItemClicked(view: View?) {
                mCheckmark.visibility = if (mCheckmark.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            }
        }
    }
}
