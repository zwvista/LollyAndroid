package com.zwstudio.lolly.android.phrases

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.swipe.ListSwipeHelper
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.zwstudio.lolly.android.DrawerListFragment
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ContentPhrasesTextbookBinding
import com.zwstudio.lolly.android.databinding.ContentPhrasesUnitBinding
import com.zwstudio.lolly.android.yesNoDialog
import com.zwstudio.lolly.data.DrawerListViewModel
import com.zwstudio.lolly.data.misc.*
import com.zwstudio.lolly.data.phrases.PhrasesUnitViewModel
import com.zwstudio.lolly.data.words.WordsUnitViewModel
import com.zwstudio.lolly.domain.misc.MSelectItem
import com.zwstudio.lolly.domain.wpp.MUnitPhrase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import org.androidannotations.annotations.*
import java.util.*


@EFragment(R.layout.content_phrases_textbook)
@OptionsMenu(R.menu.menu_phrases_textbook)
class PhrasesTextbookFragment : DrawerListFragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PhrasesUnitViewModel>() }
    override val vmDrawerList: DrawerListViewModel? get() = vm
    lateinit var binding: ContentPhrasesTextbookBinding

    @OptionsMenuItem
    lateinit var menuNormalMode: MenuItem
    @OptionsMenuItem
    lateinit var menuEditMode: MenuItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ContentPhrasesTextbookBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@PhrasesTextbookFragment
            model = vm
        }
        return binding.root
    }

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(R.string.phrases_textbook)
        tts = TextToSpeech(requireContext(), this)

        binding.svTextFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                vm.applyFilters()
                refreshListView()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                vm.textFilter = newText
                if (newText.isEmpty())
                    refreshListView()
                return false
            }
        })

        binding.spnTextbookFilter.adapter = makeCustomAdapter(requireContext(), vm.vmSettings.lstTextbookFilters) { it.label }
        binding.spnTextbookFilter.setSelection(0)
        binding.spnScopeFilter.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstScopePhraseFilters) { it.label }
        binding.spnScopeFilter.setSelection(0)

        setupListView()
        vm.viewModelScope.launch {
            vm.getDataInLang()
            refreshListView()
            progressBar1.visibility = View.GONE
        }
    }

    private fun refreshListView() {
        val listAdapter = PhrasesTextbookItemAdapter(vm, mDragListView, tts)
        mDragListView.setAdapter(listAdapter, true)
    }

    @ItemSelect
    fun spnTextbookFilterItemSelected(selected: Boolean, selectedItem: MSelectItem) {
        vm.textbookFilter = selectedItem.value
        vm.applyFilters()
        refreshListView()
    }

    @ItemSelect
    fun spnScopeFilterItemSelected(selected: Boolean, selectedItem: MSelectItem) {
        vm.scopeFilter = selectedItem.label
        vm.applyFilters()
        refreshListView()
    }

    @OptionsItem
    fun menuNormalMode() = setMenuMode(false)
    @OptionsItem
    fun menuEditMode() = setMenuMode(true)
    private fun setMenuMode(isEditMode: Boolean) {
        vm.isEditMode = isEditMode
        (if (isEditMode) menuEditMode else menuNormalMode).isChecked = true
        refreshListView()
    }

    private class PhrasesTextbookItemAdapter(val vm: PhrasesUnitViewModel, val mDragListView: DragListView, val tts: TextToSpeech) : DragItemAdapter<MUnitPhrase, PhrasesTextbookItemAdapter.ViewHolder>() {

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
            var mEdit: TextView = itemView.findViewById(R.id.item_edit)
            var mDelete: TextView = itemView.findViewById(R.id.item_delete)
            var mMore: TextView = itemView.findViewById(R.id.item_more)

            init {
                initButtons()
            }

            fun edit(item: MUnitPhrase) {
                PhrasesTextbookDetailActivity_.intent(itemView.context).extra("phrase", item).start()
            }

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
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
            }

            override fun onItemClicked(view: View?) {
                if (vm.isSwipeStarted) {
                    mDragListView.resetSwipedViews(null)
                    vm.isSwipeStarted = false
                } else {
                    val item = view!!.tag as MUnitPhrase
                    if (vm.isEditMode)
                        edit(item)
                    else
                        tts.speak(item.phrase, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }

}
