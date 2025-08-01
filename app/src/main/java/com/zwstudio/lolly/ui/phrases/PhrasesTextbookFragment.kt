package com.zwstudio.lolly.ui.phrases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.MainActivity
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.copyText
import com.zwstudio.lolly.common.googleString
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.databinding.FragmentPhrasesTextbookBinding
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import com.zwstudio.lolly.ui.common.LollyListFragment
import com.zwstudio.lolly.ui.common.autoCleared
import com.zwstudio.lolly.ui.common.makeCustomAdapter
import com.zwstudio.lolly.ui.common.yesNoDialog
import com.zwstudio.lolly.viewmodels.LollyListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhrasesTextbookFragment : LollyListFragment() {

    val vm by viewModel<PhrasesUnitViewModel>()
    override val vmList: LollyListViewModel get() = vm
    var binding by autoCleared<FragmentPhrasesTextbookBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPhrasesTextbookBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.svTextFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                vm.textFilter = newText
                return false
            }
        })
        binding.spnTextbookFilter.adapter = makeCustomAdapter(requireContext(), vmSettings.lstTextbookFilters) { it.label }
        binding.spnScopeFilter.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstScopePhraseFilters) { it.label }
        setupListView()

        vm.lstPhrases_.onEach {
            val listAdapter = PhrasesTextbookItemAdapter(vm, mDragListView)
            mDragListView.setAdapter(listAdapter, true)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override suspend fun onRefresh() = vm.getDataInLang()

    private class PhrasesTextbookItemAdapter(val vm: PhrasesUnitViewModel, val mDragListView: DragListView) : DragItemAdapter<MUnitPhrase, PhrasesTextbookItemAdapter.ViewHolder>() {

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

        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView = itemView.findViewById(R.id.text1)
            var mText2: TextView = itemView.findViewById(R.id.text2)
            var mText3: TextView = itemView.findViewById(R.id.text3)
            val navController get() = (itemView.context as MainActivity).getNavController()

            override fun onItemClicked(view: View?) {
                val item = itemView.tag as MUnitPhrase
                speak(item.phrase)
            }

            override fun onItemLongClicked(view: View?): Boolean {
                val item = itemView.tag as MUnitPhrase
                // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                AlertDialog.Builder(itemView.context)
                    .setTitle(item.phrase)
                    .setItems(arrayOf(
                        itemView.context.getString(R.string.action_delete),
                        itemView.context.getString(R.string.action_edit),
                        itemView.context.getString(R.string.action_copy_phrase),
                        itemView.context.getString(R.string.action_google_phrase),
                        itemView.context.getString(R.string.action_cancel),
                    )) { _, which ->
                        when (which) {
                            0 ->
                                yesNoDialog(itemView.context, "Are you sure you want to delete the phrase \"${item.phrase}\"?", {
                                    val pos = mDragListView.adapter.getPositionForItem(item)
                                    mDragListView.adapter.removeItem(pos)
                                    vm.delete(item)
                                }, {})

                            1 -> navController.navigate(PhrasesTextbookFragmentDirections.actionPhrasesTextbookFragmentToPhrasesTextbookDetailFragment(item))
                            2 -> copyText(itemView.context, item.phrase)
                            3 -> googleString(itemView.context, item.phrase)
                            else -> {}
                        }
                    }.show()
                return true
            }
        }
    }
}
