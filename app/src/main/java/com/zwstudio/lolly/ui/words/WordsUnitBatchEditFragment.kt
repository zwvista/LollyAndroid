package com.zwstudio.lolly.ui.words

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentWordsUnitBatchEditBinding
import com.zwstudio.lolly.models.misc.MSelectItem
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.ui.common.LollySwipeRefreshLayout
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.viewmodels.words.WordsUnitBatchEditViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WordsUnitBatchEditFragment : Fragment(), MenuProvider {

    val vm by lazy { requireParentFragment().getViewModel<WordsUnitViewModel>() }
    val vmBatchEdit by viewModel<WordsUnitBatchEditViewModel>()
    var binding by autoCleared<FragmentWordsUnitBatchEditBinding>()
    val args: WordsUnitBatchEditFragmentArgs by navArgs()

    var mDragListView by autoCleared<DragListView>()
    var mRefreshLayout by autoCleared<LollySwipeRefreshLayout>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        vm.lstWords = args.list.toList()
        binding = FragmentWordsUnitBatchEditBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vmBatchEdit
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmBatchEdit.saveEnabled.onEach {
            requireActivity().invalidateOptionsMenu()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.spnUnit.adapter = makeCustomAdapter(requireContext(), vmSettings.lstUnits) { it.label }
        binding.spnPart.adapter = makeCustomAdapter(requireContext(), vmSettings.lstParts) { it.label }

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

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_save, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
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
                    setFragmentResult("WordsUnitBatchEditFragment", bundleOf())
                    findNavController().navigateUp()
                }
                true
            }
            else -> false
        }

    private class WordsUnitBatchDragItem(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<View>(R.id.item_swipe).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background, null))
        }
    }

    private class WordsUnitBatchItemAdapter(vm: WordsUnitViewModel) : DragItemAdapter<MUnitWord, WordsUnitBatchItemAdapter.ViewHolder>() {

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
