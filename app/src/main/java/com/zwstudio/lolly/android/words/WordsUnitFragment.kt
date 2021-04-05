package com.zwstudio.lolly.android.words

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.android.*
import com.zwstudio.lolly.android.databinding.FragmentWordsUnitBinding
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.data.DrawerListViewModel
import com.zwstudio.lolly.data.misc.*
import com.zwstudio.lolly.data.words.WordsUnitViewModel
import com.zwstudio.lolly.domain.wpp.MUnitWord
import io.reactivex.rxjava3.disposables.CompositeDisposable

class WordsUnitFragment : DrawerListFragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsUnitViewModel>() }
    override val vmDrawerList: DrawerListViewModel? get() = vm
    var binding by autoCleared<FragmentWordsUnitBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWordsUnitBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        vm.compositeDisposable = compositeDisposable
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.spnScopeFilter.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstScopeWordFilters) { it.label }
        vm.scopeFilterIndex.distinctUntilChanged().observe(viewLifecycleOwner) {
            vm.applyFilters()
            refreshListView()
        }

        setFragmentResultListener("WordsUnitDetailFragment") { requestKey, bundle ->
            menuAdd()
        }

        setupListView(WordsUnitDragItem(requireContext(), R.layout.list_item_words_unit_edit))
        compositeDisposable.add(vm.getDataInTextbook().subscribe {
            refreshListView()
            progressBar1.visibility = View.GONE
        })
    }

    private fun refreshListView() {
        val listAdapter = WordsUnitItemAdapter(vm, mDragListView, compositeDisposable)
        mDragListView.setAdapter(listAdapter, true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_words_unit, menu)
        setEditMode(menu.findItem(if (vm.isEditMode) R.id.menuEditMode else R.id.menuNormalMode), vm.isEditMode)
    }

    fun setEditMode(item: MenuItem, isEditMode: Boolean) {
        vm.isEditMode = isEditMode
        item.isChecked = true
        refreshListView()
    }

    fun menuAdd() =
        findNavController().navigate(R.id.action_wordsUnitFragment_to_wordsUnitDetailFragment,
            bundleOf("word" to vm.newUnitWord()))

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        fun getNotes(ifEmpty: Boolean) {
            val handler = Handler(Looper.getMainLooper())
            progressBar1.visibility = View.VISIBLE
            vm.getNotes(ifEmpty, oneComplete = {}, allComplete = {
                handler.post {
                    mDragListView.adapter.notifyDataSetChanged()
                    progressBar1.visibility = View.GONE
                }
            })
        }
        fun clearNotes(ifEmpty: Boolean) {
            val handler = Handler(Looper.getMainLooper())
            progressBar1.visibility = View.VISIBLE
            vm.clearNotes(ifEmpty, oneComplete = {}, allComplete = {
                handler.post {
                    mDragListView.adapter.notifyDataSetChanged()
                    progressBar1.visibility = View.GONE
                }
            })
        }
        return when (item.itemId) {
            R.id.menuNormalMode -> {
                setEditMode(item,false)
                true
            }
            R.id.menuEditMode -> {
                setEditMode(item,true)
                true
            }
            R.id.menuAdd -> {
                menuAdd()
                true
            }
            R.id.menuRetrieveNotesAll -> {
                getNotes(false)
                true
            }
            R.id.menuRetrieveNotesEmpty -> {
                getNotes(true)
                true
            }
            R.id.menuClearNotesAll -> {
                clearNotes(false)
                true
            }
            R.id.menuClearNotesEmpty -> {
                clearNotes(true)
                true
            }
            R.id.menuBatch -> {
                findNavController().navigate(R.id.action_wordsUnitFragment_to_wordsUnitBatchEditFragment,
                    bundleOf("list" to vm.lstWords.toTypedArray()))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private class WordsUnitDragItem(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<TextView>(R.id.text3).text = clickedView.findViewById<TextView>(R.id.text3).text
            dragView.findViewById<View>(R.id.item_swipe).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background, null))
            dragView.findViewById<ImageView>(R.id.image_forward).visibility = View.GONE
        }
    }

    private class WordsUnitItemAdapter(val vm: WordsUnitViewModel, val mDragListView: DragListView, val compositeDisposable: CompositeDisposable) : DragItemAdapter<MUnitWord, WordsUnitItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstWords
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_words_unit_edit, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.word
            holder.mText2.text = item.unitpartseqnum
            holder.mText3.text = item.note
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
            var mForward: ImageView = itemView.findViewById(R.id.image_forward)
            var mHamburger: ImageView = itemView.findViewById(R.id.image_hamburger)
            val navController get() = (itemView.context as MainActivity).getNavController()

            init {
                initButtons()
            }

            fun edit(item: MUnitWord) =
                navController.navigate(R.id.action_wordsUnitFragment_to_wordsUnitDetailFragment,
                    bundleOf("word" to item))

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
                fun delete(item: MUnitWord) {
                    yesNoDialog(itemView.context, "Are you sure you want to delete the word \"${item.word}\"?", {
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
                        val item = itemView.tag as MUnitWord
                        edit(item)
                    }
                    true
                }
                mDelete.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MUnitWord
                        delete(item)
                    }
                    true
                }
                mMore.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false

                        val item = itemView.tag as MUnitWord
                        // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                        val builder = AlertDialog.Builder(itemView.context)
                            .setTitle(item.wordnote)
                            .setItems(arrayOf("Delete", "Edit", "Retrieve Note", "Copy Word", "Google Word", "Online Dictionary", "Cancel")) { _, which ->
                                when (which) {
                                    0 -> delete(item)
                                    1 -> edit(item)
                                    2 -> {
                                        val index = itemList.indexOf(item)
                                        compositeDisposable.add(vm.getNote(index).subscribe {
                                            mDragListView.adapter.notifyItemChanged(index)
                                        })
                                    }
                                    3 -> itemView.copyText(item.word)
                                    4 -> itemView.googleString(item.word)
                                    5 -> {
                                        val itemDict = vmSettings.lstDictsReference.find { it.dictname == vmSettings.selectedDictReference.dictname }!!
                                        val url = itemDict.urlString(item.word, vmSettings.lstAutoCorrect)
                                        itemView.openPage(url)
                                    }
                                    else -> {}
                                }
                            }
                        builder.show()
                    }
                    true
                }
                mForward.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MUnitWord
                        navController.navigate(R.id.action_wordsUnitFragment_to_wordsDictFragment,
                            bundleOf("list" to vm.lstWords.map { it.word }.toTypedArray(),
                                "index" to vm.lstWords.indexOf(item)))
                    }
                    true
                }
                if (vm.isEditMode)
                    mForward.visibility = View.GONE
                if (!(vm.isEditMode && vmSettings.isSingleUnitPart && vm.noFilter))
                    mHamburger.visibility = View.GONE
            }

            override fun onItemClicked(view: View?) {
                if (vm.isSwipeStarted) {
                    mDragListView.resetSwipedViews(null)
                    vm.isSwipeStarted = false
                } else {
                    val item = view!!.tag as MUnitWord
                    if (vm.isEditMode)
                        edit(item)
                    else
                        speak(item.word)
                }
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }
}
