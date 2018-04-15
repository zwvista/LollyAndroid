package com.zwstudio.lolly.android

import android.app.Activity.RESULT_OK
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
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.swipe.ListSwipeHelper
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.zwstudio.lolly.data.WordsUnitViewModel
import com.zwstudio.lolly.data.extractTextFrom
import com.zwstudio.lolly.domain.UnitWord
import org.androidannotations.annotations.*
import java.net.URLEncoder


@EFragment(R.layout.content_words_unit)
@OptionsMenu(R.menu.menu_add)
class WordsUnitFragment : DrawerListFragment() {

    @Bean
    lateinit var vm: WordsUnitViewModel

    @ViewById(R.id.drag_list_view)
    lateinit var mDragListView: DragListView
    @ViewById(R.id.swipe_refresh_layout)
    lateinit var mRefreshLayout: LollySwipeRefreshLayout

    @AfterViews
    fun afterViews() {
        activity?.title = "Words in Unit"
    }

    override fun onResume() {
        super.onResume()
        vm.getData {
            mDragListView.recyclerView.isVerticalScrollBarEnabled = true
            mDragListView.setDragListListener(object : DragListView.DragListListenerAdapter() {
                override fun onItemDragStarted(position: Int) {
                    mRefreshLayout.isEnabled = false
                    Toast.makeText(mDragListView.context, "Start - position: $position", Toast.LENGTH_SHORT).show()
                }

                override fun onItemDragEnded(fromPosition: Int, toPosition: Int) {
                    mRefreshLayout.isEnabled = true
                    if (fromPosition == toPosition) return
                    Toast.makeText(mDragListView.context, "End - position: $toPosition", Toast.LENGTH_SHORT).show()
                    vm.reindex {}
                }
            })

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
            val listAdapter = WordsUnitItemAdapter(vm, mDragListView, R.layout.list_item_words_edit, R.id.image, false)
            mDragListView.setAdapter(listAdapter, true)
            mDragListView.setCanDragHorizontally(false)
            mDragListView.setCustomDragItem(WordsUnitDragItem(context!!, R.layout.list_item_words_edit))
            progressBar1.visibility = View.GONE
        }
    }

    @OptionsItem
    fun menuAdd() {
        WordsUnitDetailActivity_.intent(this)
                .extra("list", vm.lstWords.toTypedArray()).extra("word", vm.newUnitWord())
                .startForResult(1);
    }
    @OnActivityResult(1)
    fun onResult(resultCode: Int) {
        if (resultCode == RESULT_OK) menuAdd()
    }

    private class WordsUnitDragItem internal constructor(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<View>(R.id.item_layout).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background))
        }
    }

    private class WordsUnitItemAdapter(val vm: WordsUnitViewModel, val mDragListView: DragListView, val mLayoutId: Int, val mGrabHandleId: Int, val mDragOnLongPress: Boolean) : DragItemAdapter<UnitWord, WordsUnitItemAdapter.ViewHolder>() {

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
            holder.mText2.text = mItemList[position].unitpartseqnum(vm.vmSettings.lstParts)
            holder.itemView.tag = mItemList[position]
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        internal inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, mGrabHandleId, mDragOnLongPress) {
            var mText1: TextView
            var mText2: TextView
            var mEdit: TextView
            var mDelete: TextView
            var mMore: TextView

            init {
                mText1 = itemView.findViewById(R.id.text1)
                mText2 = itemView.findViewById(R.id.text2)

                fun edit(item: UnitWord) {
                    WordsUnitDetailActivity_.intent(itemView.context)
                            .extra("list", vm.lstWords.toTypedArray()).extra("word", item).start()
                }
                fun delete(item: UnitWord) {
                    yesNoDialog(itemView.context, "Are you sure you want to delete the word \"${item.word}\"?", {
                        val pos = mDragListView.adapter.getPositionForItem(item)
                        mDragListView.adapter.removeItem(pos)
                        vm.delete(item.id) {}
                        vm.isSwipeStarted = false
                    }, {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false
                    })
                }
                fun getNote(item: UnitWord, onNext: (String) -> Unit) {
                    val noteSite = vm.vmSettings.selectedNoteSite
                    val url = noteSite.url!!.replace("{0}", URLEncoder.encode(item.word, "UTF-8"))
                    vm.getHtml(url) {
                        val result = extractTextFrom(it, noteSite.transformMac!!, noteSite.template!!) { _,_ -> "" }
                        onNext(result)
                    }
                }
                fun copy(item: UnitWord) {
                    // https://stackoverflow.com/questions/19177231/android-copy-paste-from-clipboard-manager
                    val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("", item.word)
                    clipboard.primaryClip = clip
                }
                fun google(item: UnitWord) {
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

                mEdit = itemView.findViewById(R.id.item_edit)
                mEdit.setOnTouchListener { v, event ->
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as UnitWord
                        edit(item)
                    }
                    true
                }
                mDelete = itemView.findViewById(R.id.item_delete)
                mDelete.setOnTouchListener { v, event ->
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as UnitWord
                        delete(item)
                    }
                    true
                }
                mMore = itemView.findViewById(R.id.item_more)
                mMore.setOnTouchListener { v, event ->
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mDragListView.resetSwipedViews(null)
                        vm.isSwipeStarted = false

                        val item = itemView.tag as UnitWord
                        // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
                        val builder = AlertDialog.Builder(itemView.context)
                            .setTitle(item.wordnote)
                            .setItems(arrayOf("Delete", "Edit", "Retrieve Note", "Copy Word", "Google Word", "Cancel")) { dialog, which ->
                                when (which) {
                                    0 -> delete(item)
                                    1 -> edit(item)
                                    2 -> getNote(item) {
                                        vm.updateNote(item.id, it) {
                                            item.note = it
                                            mDragListView.adapter.notifyItemChanged(itemList.indexOf(item))
                                        }
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
                    val item = view!!.tag as UnitWord
                    WordsDictActivity_.intent(view.context).extra("word", item.word).start()
                }
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }

}
