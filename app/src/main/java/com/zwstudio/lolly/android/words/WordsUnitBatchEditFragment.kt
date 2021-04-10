package com.zwstudio.lolly.android.words

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.android.LollySwipeRefreshLayout
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentWordsUnitBatchEditBinding
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.words.WordsUnitBatchEditViewModel
import com.zwstudio.lolly.data.words.WordsUnitViewModel
import com.zwstudio.lolly.domain.misc.MSelectItem
import com.zwstudio.lolly.domain.wpp.MUnitWord

class WordsUnitBatchEditFragment : Fragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsUnitViewModel>() }
    val vmBatch by lazy { vita.with(VitaOwner.Single(this)).getViewModel<WordsUnitBatchEditViewModel>() }
    var binding by autoCleared<FragmentWordsUnitBatchEditBinding>()

    var mDragListView by autoCleared<DragListView>()
    var mRefreshLayout by autoCleared<LollySwipeRefreshLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vm.lstWords = (requireArguments().getSerializable("list") as Array<MUnitWord>).toList()
        binding = FragmentWordsUnitBatchEditBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vmBatch
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spnUnit.adapter = makeCustomAdapter(requireContext(), vmSettings.lstUnits) { it.label }
        binding.spnUnit.setSelection(vmSettings.lstUnits.indexOfFirst { it.value == vmSettings.usunitto })
        binding.spnPart.adapter = makeCustomAdapter(requireContext(), vmSettings.lstParts) { it.label }
        binding.spnPart.setSelection(vmSettings.lstParts.indexOfFirst { it.value == vmSettings.uspartto })

        fun chkUnit() {
            binding.spnUnit.isEnabled = binding.chkUnit.isChecked
        }
        fun chkPart() {
            binding.spnPart.isEnabled = binding.chkPart.isChecked
        }
        fun chkSeqNum() {
            binding.etSeqNum.isEnabled = binding.chkSeqNum.isChecked
        }
        binding.chkUnit.setOnCheckedChangeListener { _, _ -> chkUnit() }
        binding.chkPart.setOnCheckedChangeListener { _, _ -> chkPart() }
        binding.chkSeqNum.setOnCheckedChangeListener { _, _ -> chkSeqNum() }
        chkUnit(); chkPart(); chkSeqNum()

        mDragListView = view.findViewById(R.id.drag_list_view)
        mRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)

        mDragListView.recyclerView.isVerticalScrollBarEnabled = true

        mRefreshLayout.setScrollingView(mDragListView.recyclerView)
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.app_color))
        mRefreshLayout.setOnRefreshListener { mRefreshLayout.postDelayed({ mRefreshLayout.isRefreshing = false }, 2000) }

        mDragListView.setLayoutManager(LinearLayoutManager(requireContext()))
        val listAdapter = WordsUnitBatchItemAdapter(vm)
        mDragListView.setAdapter(listAdapter, true)
        mDragListView.setCanDragHorizontally(false)
        mDragListView.setCustomDragItem(WordsUnitBatchDragItem(requireContext(), R.layout.list_item_words_unit_batch_edit))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuSave -> {
                if (binding.chkUnit.isChecked || binding.chkPart.isChecked || binding.chkSeqNum.isChecked) {
                    for ((i, item) in vm.lstWords.withIndex()) {
                        val v = mDragListView.recyclerView.findViewHolderForAdapterPosition(i) as WordsUnitBatchItemAdapter.ViewHolder
                        if (v.mCheckmark.visibility == View.INVISIBLE) continue
                        if (binding.chkUnit.isChecked) item.unit = (binding.spnUnit.selectedItem as MSelectItem).value
                        if (binding.chkPart.isChecked) item.part = (binding.spnPart.selectedItem as MSelectItem).value
                        if (binding.chkSeqNum.isChecked) item.seqnum += binding.etSeqNum.text.toString().toInt()
                        vm.update(item)
                    }
                    setFragmentResult("WordsUnitBatchEditFragment", bundleOf("result" to "1"))
                    findNavController().navigateUp()
                }
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }

    private class WordsUnitBatchDragItem(context: Context, layoutId: Int) : DragItem(context, layoutId) {

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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_words_unit_batch_edit, parent, false)
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

        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView = itemView.findViewById(R.id.text1)
            var mText2: TextView = itemView.findViewById(R.id.text2)
            var mCheckmark: ImageView = itemView.findViewById(R.id.image_checkmark)

            override fun onItemClicked(view: View?) {
                mCheckmark.visibility = if (mCheckmark.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            }
        }
    }
}
