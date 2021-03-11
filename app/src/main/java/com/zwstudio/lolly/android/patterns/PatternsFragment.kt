package com.zwstudio.lolly.android.patterns

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.android.DrawerListFragment
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ContentPatternsBinding
import com.zwstudio.lolly.android.yesNoDialog
import com.zwstudio.lolly.data.DrawerListViewModel
import com.zwstudio.lolly.data.misc.*
import com.zwstudio.lolly.data.patterns.PatternsViewModel
import com.zwstudio.lolly.domain.misc.MSelectItem
import com.zwstudio.lolly.domain.wpp.MPattern
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*
import java.util.*

private const val REQUEST_CODE = 1

@EFragment(R.layout.content_patterns)
@OptionsMenu(R.menu.menu_patterns)
class PatternsFragment : DrawerListFragment() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PatternsViewModel>() }
    override val vmDrawerList: DrawerListViewModel? get() = vm
    lateinit var binding: ContentPatternsBinding

    @OptionsMenuItem
    lateinit var menuNormalMode: MenuItem
    @OptionsMenuItem
    lateinit var menuEditMode: MenuItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ContentPatternsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@PatternsFragment
            model = vm
        }
        return binding.root
    }

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(R.string.patterns)
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

        binding.spnScopeFilter.adapter = makeCustomAdapter(requireContext(), SettingsViewModel.lstScopePatternFilters) { it.label }
        binding.spnScopeFilter.setSelection(0)

        setupListView()
        compositeDisposable.add(vm.getData().subscribe {
            refreshListView()
            progressBar1.visibility = View.GONE
        })
    }

    private fun refreshListView() {
        val listAdapter = PatternsItemAdapter(vm, mDragListView, tts, compositeDisposable)
        mDragListView.setAdapter(listAdapter, true)
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

    @OptionsItem
    fun menuAdd() {
        PatternsDetailActivity_.intent(this)
            .extra("pattern", vm.newPattern()).startForResult(REQUEST_CODE)
    }


    @OnActivityResult(REQUEST_CODE)
    fun onResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK)
            mDragListView.resetSwipedViews(null)
    }

    private class PatternsItemAdapter(val vm: PatternsViewModel, val mDragListView: DragListView, val tts: TextToSpeech, val compositeDisposable: CompositeDisposable) : DragItemAdapter<MPattern, PatternsItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstPatterns
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_patterns_edit, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = mItemList[position]
            holder.mText1.text = item.pattern
            holder.mText2.text = item.tags
            holder.itemView.tag = item
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image_hamburger, false) {
            var mText1: TextView
            var mText2: TextView
            var mEdit: TextView
            var mDelete: TextView
            var mMore: TextView
            var mForward: ImageView

            init {
                mText1 = itemView.findViewById(R.id.text1)
                mText2 = itemView.findViewById(R.id.text2)
                mEdit = itemView.findViewById(R.id.item_edit)
                mDelete = itemView.findViewById(R.id.item_delete)
                mMore = itemView.findViewById(R.id.item_more)
                mForward = itemView.findViewById(R.id.image_forward)
                initButtons()
            }

            fun edit(item: MPattern) {
                PatternsDetailActivity_.intent(itemView.context)
                    .extra("pattern", item).startForResult(REQUEST_CODE)
            }

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
                fun delete(item: MPattern) {
                    yesNoDialog(itemView.context, "Are you sure you want to delete the pattern \"${item.pattern}\"?", {
                        val pos = mDragListView.adapter.getPositionForItem(item)
                        mDragListView.adapter.removeItem(pos)
                        compositeDisposable.add(vm.delete(item.id).subscribe())
                        vm.isSwipeStarted = false
                    }, {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false
                    })
                }
                mEdit.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MPattern
                        edit(item)
                    }
                    true
                }
                mDelete.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MPattern
                        delete(item)
                    }
                    true
                }
                mMore.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false

                        val item = itemView.tag as MPattern
                        // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                        val builder = AlertDialog.Builder(itemView.context)
                            .setTitle(item.pattern)
                            .setItems(arrayOf("Delete", "Edit", "Browse Web Pages", "Edit Web Pages", "Copy Pattern", "Google Pattern", "Cancel")) { _, which ->
                                when (which) {
                                    0 -> delete(item)
                                    1 -> edit(item)
                                    2 -> {
                                        PatternsWebPagesBrowseActivity_.intent(itemView.context)
                                            .extra("pattern", item).startForResult(REQUEST_CODE)
                                    }
                                    3 -> {
                                        PatternsWebPagesListActivity_.intent(itemView.context)
                                            .extra("pattern", item).startForResult(REQUEST_CODE)
                                    }
                                    4 -> itemView.copyText(item.pattern)
                                    5 -> itemView.googleString(item.pattern)
                                    else -> {}
                                }
                            }
                        builder.show()
                    }
                    true
                }
                mForward.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as MPattern
                        PatternsWebPagesBrowseActivity_.intent(itemView.context)
                            .extra("pattern", item).startForResult(REQUEST_CODE)
                    }
                    true
                }
            }

            override fun onItemClicked(view: View?) {
                if (vm.isSwipeStarted) {
                    mDragListView.resetSwipedViews(null)
                    vm.isSwipeStarted = false
                } else {
                    val item = view!!.tag as MPattern
                    if (vm.isEditMode)
                        edit(item)
                    else
                        tts.speak(item.pattern, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }

}
