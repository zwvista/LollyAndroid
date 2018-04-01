package com.zwstudio.lolly.android

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragItemAdapter
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.swipe.ListSwipeHelper
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.zwstudio.lolly.data.SettingsViewModel
import com.zwstudio.lolly.data.WordsUnitViewModel
import com.zwstudio.lolly.domain.UnitWord
import org.androidannotations.annotations.*

@EFragment(R.layout.content_words_unit)
@OptionsMenu(R.menu.menu_add)
class WordsUnitFragment : DrawerListFragment() {

    @Bean
    lateinit var vm: WordsUnitViewModel
    
    lateinit var lst: MutableList<UnitWord>

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
            lst = it.lst!!.toMutableList()
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
                    for (i in 1..lst.size) {
                        val item = lst[i - 1]
                        if (item.seqnum == i) continue
                        item.seqnum = i
                        vm.updateSeqNum(item.id, i) {}
                    }
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

                    val adapterItem = item!!.tag as UnitWord
                    // Swipe to delete on left or to edit on right
                    when (swipedDirection) {
                        ListSwipeItem.SwipeDirection.LEFT ->
                            yesNoDialog(context!!, "Are you sure you want to delete the word \"${adapterItem.word}\"?", {
                                val pos = mDragListView.adapter.getPositionForItem(adapterItem)
                                mDragListView.adapter.removeItem(pos)
                                vm.delete(adapterItem.id) {}
                            }, {
                                mDragListView.resetSwipedViews(null)
                            })
                        ListSwipeItem.SwipeDirection.RIGHT -> {
                            mDragListView.resetSwipedViews(null)
                            WordsUnitDetailActivity_.intent(context!!).extra("word", adapterItem).start()
                        }
                        else -> {}
                    }
                }
            })

            mDragListView.setLayoutManager(LinearLayoutManager(context!!))
            val listAdapter = WordsUnitItemAdapter(lst, vm.vmSettings, R.layout.list_item_words_edit, R.id.image, false)
            mDragListView.setAdapter(listAdapter, true)
            mDragListView.setCanDragHorizontally(false)
            mDragListView.setCustomDragItem(WordsUnitDragItem(context!!, R.layout.list_item_words_edit))
            progressBar1.visibility = View.GONE
        }
    }

    @OptionsItem
    fun menuAdd() {
        val item = UnitWord()
        item.textbookid = vm.vmSettings.ustextbookid
        // https://stackoverflow.com/questions/33640864/how-to-sort-based-on-compare-multiple-values-in-kotlin
        val maxItem = lst.maxWith(compareBy<UnitWord>({ it.unitpart }, { it.seqnum }))
        item.unit = maxItem?.unit ?: vm.vmSettings.usunitto
        item.part = maxItem?.part ?: vm.vmSettings.uspartto
        item.seqnum = (maxItem?.seqnum ?: 0) + 1
        WordsUnitDetailActivity_.intent(this).extra("word", item).start()
    }

    private class WordsUnitDragItem internal constructor(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<View>(R.id.item_layout).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background))
        }
    }

    private class WordsUnitItemAdapter(list: List<UnitWord>, val vmSettings: SettingsViewModel, val mLayoutId: Int, val mGrabHandleId: Int, val mDragOnLongPress: Boolean) : DragItemAdapter<UnitWord, WordsUnitItemAdapter.ViewHolder>() {

        init {
            itemList = list
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(mLayoutId, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.mText1.text = mItemList[position].wordnote
            holder.mText2.text = mItemList[position].unitpartseqnum(vmSettings.lstParts)
            holder.itemView.tag = mItemList[position]
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        internal inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, mGrabHandleId, mDragOnLongPress) {
            var mText1: TextView
            var mText2: TextView

            init {
                mText1 = itemView.findViewById<TextView>(R.id.text1)
                mText2 = itemView.findViewById<TextView>(R.id.text2)
            }

            override fun onItemClicked(view: View?) {
                val item = view!!.tag as UnitWord
                WordDictActivity_.intent(view.context).extra("word", item.word).start()
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }

}
