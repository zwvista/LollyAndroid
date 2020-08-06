package com.zwstudio.lolly.android.phrases

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
import com.zwstudio.lolly.data.PhrasesUnitViewModel
import com.zwstudio.lolly.domain.MSelectItem
import com.zwstudio.lolly.domain.MUnitPhrase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_phrases_unit_batch)
@OptionsMenu(R.menu.menu_save)
class PhrasesUnitBatchActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: PhrasesUnitViewModel

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
        vm.lstPhrases = (intent.getSerializableExtra("list") as Array<MUnitPhrase>).toList()
        chkUnit(); chkPart(); chkSeqNum()
        run {
            val lst = vm.vmSettings.lstUnits
            val adapter = object : ArrayAdapter<MSelectItem>(this, android.R.layout.simple_spinner_item, lst) {
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
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnUnit.adapter = adapter
            spnUnit.setSelection(vm.vmSettings.lstUnits.indexOfFirst { it.value == vm.vmSettings.usunitto })
        }

        run {
            val lst = vm.vmSettings.lstParts
            val adapter = object : ArrayAdapter<MSelectItem>(this, android.R.layout.simple_spinner_item, lst) {
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
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnPart.adapter = adapter
            spnPart.setSelection(vm.vmSettings.lstParts.indexOfFirst { it.value == vm.vmSettings.uspartto })
        }

        mDragListView.recyclerView.isVerticalScrollBarEnabled = true

        mRefreshLayout.setScrollingView(mDragListView.recyclerView)
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_color))
        mRefreshLayout.setOnRefreshListener { mRefreshLayout.postDelayed({ mRefreshLayout.isRefreshing = false }, 2000) }

        mDragListView.setLayoutManager(LinearLayoutManager(this))
        val listAdapter = PhrasesUnitBatchItemAdapter(vm)
        mDragListView.setAdapter(listAdapter, true)
        mDragListView.setCanDragHorizontally(false)
        mDragListView.setCustomDragItem(PhrasesUnitBatchDragItem(this, R.layout.list_item_phrases_unit_batch))
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
        for ((i, item) in vm.lstPhrases.withIndex()) {
            val v = mDragListView.recyclerView.findViewHolderForAdapterPosition(i) as PhrasesUnitBatchItemAdapter.ViewHolder
            if (v.mCheckmark.visibility == View.INVISIBLE) continue
            if (chkUnit.isChecked) item.unit = (spnUnit.selectedItem as MSelectItem).value
            if (chkPart.isChecked) item.part = (spnPart.selectedItem as MSelectItem).value
            if (chkSeqNum.isChecked) item.seqnum += etSeqNum.text.toString().toInt()
            compositeDisposable.add(vm.update(item).subscribe())
        }
        finish()
    }

    private class PhrasesUnitBatchDragItem internal constructor(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<TextView>(R.id.text3).text = clickedView.findViewById<TextView>(R.id.text3).text
            dragView.findViewById<View>(R.id.item_swipe).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background))
        }
    }

    private class PhrasesUnitBatchItemAdapter(val vm: PhrasesUnitViewModel) : DragItemAdapter<MUnitPhrase, PhrasesUnitBatchItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstPhrases
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_phrases_unit_batch, parent, false)
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
            var mCheckmark: ImageView

            init {
                mText1 = itemView.findViewById(R.id.text1)
                mText2 = itemView.findViewById(R.id.text2)
                mText3 = itemView.findViewById(R.id.text3)
                mCheckmark = itemView.findViewById(R.id.image_checkmark)
            }

            override fun onItemClicked(view: View?) {
                mCheckmark.visibility = if (mCheckmark.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            }

        }
    }
}
