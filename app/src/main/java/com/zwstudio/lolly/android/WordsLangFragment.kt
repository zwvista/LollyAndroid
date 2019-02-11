package com.zwstudio.lolly.android

import android.annotation.SuppressLint
import android.content.*
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.swipe.ListSwipeHelper
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.zwstudio.lolly.data.WordsLangViewModel
import com.zwstudio.lolly.domain.LangWord
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.*
import java.net.URLEncoder

@EFragment(R.layout.content_words_lang)
@OptionsMenu(R.menu.menu_add)
class WordsLangFragment : DrawerListFragment() {

    @Bean
    lateinit var vm: WordsLangViewModel

    @ViewById(R.id.drag_list_view)
    lateinit var mDragListView: DragListView
    @ViewById(R.id.swipe_refresh_layout)
    lateinit var mRefreshLayout: LollySwipeRefreshLayout

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(R.string.words_lang)
    }

    override fun onResume() {
        super.onResume()
        compositeDisposable.add(vm.getData().subscribe {
            mDragListView.recyclerView.isVerticalScrollBarEnabled = true

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
            val listAdapter = WordsLangItemAdapter(vm, mDragListView, R.layout.list_item_words_lang_edit, compositeDisposable)
            mDragListView.setAdapter(listAdapter, true)
            progressBar1.visibility = View.GONE
        })
    }

    @OptionsItem
    fun menuAdd() {
        WordsLangDetailActivity_.intent(this)
            .extra("list", vm.lstWords.toTypedArray()).extra("word", vm.newLangWord())
            .startForResult(1)
    }

    private class WordsLangItemAdapter(val vm: WordsLangViewModel, val mDragListView: DragListView, val mLayoutId: Int, val compositeDisposable: CompositeDisposable) : DragItemAdapter<LangWord, WordsLangItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstWords
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(mLayoutId, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.mText1.text = mItemList[position].wordnote
            holder.itemView.tag = mItemList[position]
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        internal inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.image, false) {
            var mText1: TextView
            var mEdit: TextView
            var mDelete: TextView
            var mMore: TextView

            init {
                mText1 = itemView.findViewById(R.id.text1)
                mEdit = itemView.findViewById(R.id.item_edit)
                mDelete = itemView.findViewById(R.id.item_delete)
                mMore = itemView.findViewById(R.id.item_more)
                initButtons()
            }

            @SuppressLint("ClickableViewAccessibility")
            private fun initButtons() {
                fun edit(item: LangWord) {
                    WordsLangDetailActivity_.intent(itemView.context)
                        .extra("list", vm.lstWords.toTypedArray()).extra("word", item).start()
                }
                fun delete(item: LangWord) {
                    yesNoDialog(itemView.context, "Are you sure you want to delete the word \"${item.word}\"?", {
                        val pos = mDragListView.adapter.getPositionForItem(item)
                        mDragListView.adapter.removeItem(pos)
                        compositeDisposable.add(vm.delete(item.id).subscribe())
                        vm.isSwipeStarted = false
                    }, {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false
                    })
                }
                fun copy(item: LangWord) {
                    // https://stackoverflow.com/questions/19177231/android-copy-paste-from-clipboard-manager
                    val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("", item.word)
                    clipboard.primaryClip = clip
                }
                fun google(item: LangWord) {
                    // https://stackoverflow.com/questions/12013416/is-there-any-way-in-android-to-force-open-a-link-to-open-in-chrome
                    val urlString = "https://www.google.com/search?q=" + URLEncoder.encode(item.word, "UTF-8")
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.`package` = "com.android.chrome"
                    try {
                        itemView.context.startActivity(intent)
                    } catch (ex: ActivityNotFoundException) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.`package` = null
                        itemView.context.startActivity(intent)
                    }
                }

                mEdit.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as LangWord
                        edit(item)
                    }
                    true
                }
                mDelete.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as LangWord
                        delete(item)
                    }
                    true
                }
                mMore.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false

                        val item = itemView.tag as LangWord
                        // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                        val builder = AlertDialog.Builder(itemView.context)
                            .setTitle(item.wordnote)
                            .setItems(arrayOf("Delete", "Edit", "Retrieve Note", "Copy Word", "Google Word", "Cancel")) { _, which ->
                                when (which) {
                                    0 -> delete(item)
                                    1 -> edit(item)
                                    2 -> {
                                        val index = itemList.indexOf(item)
                                        compositeDisposable.add(vm.getNote(index).subscribe {
                                            mDragListView.adapter.notifyItemChanged(index)
                                        })
                                    }
                                    3 -> copy(item)
                                    4 -> google(item)
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
                    val item = view!!.tag as LangWord
                    WordsDictActivity_.intent(view.context)
                        .extra("list", vm.lstWords.map { it.word } .toTypedArray())
                        .extra("index", vm.lstWords.indexOf(item)).start()
                }
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }

}
