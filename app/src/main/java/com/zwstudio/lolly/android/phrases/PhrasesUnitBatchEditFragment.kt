package com.zwstudio.lolly.android.phrases

import android.content.Context
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.android.LollySwipeRefreshLayout
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentPhrasesUnitBatchEditBinding
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.phrases.PhrasesUnitBatchEditViewModel
import com.zwstudio.lolly.data.phrases.PhrasesUnitViewModel
import com.zwstudio.lolly.domain.misc.MSelectItem
import com.zwstudio.lolly.domain.wpp.MUnitPhrase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.CheckedChange
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity(R.layout.fragment_phrases_unit_batch_edit)
class PhrasesUnitBatchEditFragment : AppCompatActivity() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PhrasesUnitViewModel>() }
    val vmBatch by lazy { vita.with(VitaOwner.Single(this)).getViewModel<PhrasesUnitBatchEditViewModel>() }
    lateinit var binding: FragmentPhrasesUnitBatchEditBinding

    @ViewById(R.id.drag_list_view)
    lateinit var mDragListView: DragListView
    @ViewById(R.id.swipe_refresh_layout)
    lateinit var mRefreshLayout: LollySwipeRefreshLayout

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        vm.lstPhrases = (intent.getSerializableExtra("list") as Array<MUnitPhrase>).toList()
        binding = DataBindingUtil.inflate<FragmentPhrasesUnitBatchEditBinding>(LayoutInflater.from(this), R.layout.fragment_phrases_unit_batch_edit,
            findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@PhrasesUnitBatchEditFragment
            model = vmBatch
        }
        chkUnit(); chkPart(); chkSeqNum()
        run {
            val lst = vm.vmSettings.lstUnits
            val adapter = makeAdapter(this, android.R.layout.simple_spinner_item, lst) { v, position ->
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)!!.label
                v
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            binding.spnUnit.adapter = adapter
            binding.spnUnit.setSelection(vm.vmSettings.lstUnits.indexOfFirst { it.value == vm.vmSettings.usunitto })
        }

        run {
            val lst = vm.vmSettings.lstParts
            val adapter = makeAdapter(this, android.R.layout.simple_spinner_item, lst) { v, position ->
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)!!.label
                v
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            binding.spnPart.adapter = adapter
            binding.spnPart.setSelection(vm.vmSettings.lstParts.indexOfFirst { it.value == vm.vmSettings.uspartto })
        }

        mDragListView.recyclerView.isVerticalScrollBarEnabled = true

        mRefreshLayout.setScrollingView(mDragListView.recyclerView)
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_color))
        mRefreshLayout.setOnRefreshListener { mRefreshLayout.postDelayed({ mRefreshLayout.isRefreshing = false }, 2000) }

        mDragListView.setLayoutManager(LinearLayoutManager(this))
        val listAdapter = PhrasesUnitBatchItemAdapter(vm)
        mDragListView.setAdapter(listAdapter, true)
        mDragListView.setCanDragHorizontally(false)
        mDragListView.setCustomDragItem(PhrasesUnitBatchDragItem(this, R.layout.list_item_phrases_unit_batch_edit))
    }

    @CheckedChange
    fun chkUnit() {
        binding.spnUnit.isEnabled = binding.chkUnit.isChecked
    }

    @CheckedChange
    fun chkPart() {
        binding.spnPart.isEnabled = binding.chkPart.isChecked
    }

    @CheckedChange
    fun chkSeqNum() {
        binding.etSeqNum.isEnabled = binding.chkSeqNum.isChecked
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuSave -> {
                if (binding.chkUnit.isChecked || binding.chkPart.isChecked || binding.chkSeqNum.isChecked) {
                    for ((i, item) in vm.lstPhrases.withIndex()) {
                        val v = mDragListView.recyclerView.findViewHolderForAdapterPosition(i) as PhrasesUnitBatchItemAdapter.ViewHolder
                        if (v.mCheckmark.visibility == View.INVISIBLE) continue
                        if (binding.chkUnit.isChecked) item.unit = (binding.spnUnit.selectedItem as MSelectItem).value
                        if (binding.chkPart.isChecked) item.part = (binding.spnPart.selectedItem as MSelectItem).value
                        if (binding.chkSeqNum.isChecked) item.seqnum += binding.etSeqNum.text.toString().toInt()
                        vm.update(item)
                    }
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }

    private class PhrasesUnitBatchDragItem(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<TextView>(R.id.text3).text = clickedView.findViewById<TextView>(R.id.text3).text
            dragView.findViewById<View>(R.id.item_swipe).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background, null))
        }
    }

    private class PhrasesUnitBatchItemAdapter(val vm: PhrasesUnitViewModel) : DragItemAdapter<MUnitPhrase, PhrasesUnitBatchItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstPhrases
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_phrases_unit_batch_edit, parent, false)
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
            var mText1: TextView = itemView.findViewById(R.id.text1)
            var mText2: TextView = itemView.findViewById(R.id.text2)
            var mText3: TextView = itemView.findViewById(R.id.text3)
            var mCheckmark: ImageView = itemView.findViewById(R.id.image_checkmark)

            override fun onItemClicked(view: View?) {
                mCheckmark.visibility = if (mCheckmark.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            }
        }
    }
}
