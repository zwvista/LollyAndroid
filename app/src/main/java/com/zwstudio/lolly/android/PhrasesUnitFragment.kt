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
import com.zwstudio.lolly.data.PhrasesUnitViewModel
import com.zwstudio.lolly.domain.UnitPhrase
import org.androidannotations.annotations.*


@EFragment(R.layout.content_phrases_unit)
@OptionsMenu(R.menu.menu_add)
class PhrasesUnitFragment : DrawerListFragment() {

    @Bean
    lateinit var vm: PhrasesUnitViewModel

    lateinit var lst: MutableList<UnitPhrase>
    
    @ViewById(R.id.drag_list_view)
    lateinit var mDragListView: DragListView
    @ViewById(R.id.swipe_refresh_layout)
    lateinit var mRefreshLayout: LollySwipeRefreshLayout

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(R.string.phrases_unit)
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

                    val adapterItem = item!!.tag as UnitPhrase
                    // Swipe to delete on left or to edit on right
                    when (swipedDirection) {
                        ListSwipeItem.SwipeDirection.LEFT ->
                            yesNoDialog(context!!, "Are you sure you want to delete the phrase \"${adapterItem.phrase}\"?", {
                                val pos = mDragListView.adapter.getPositionForItem(adapterItem)
                                mDragListView.adapter.removeItem(pos)
                                vm.delete(adapterItem.id) {}
                            }, {
                                mDragListView.resetSwipedViews(null)
                            })
                        ListSwipeItem.SwipeDirection.RIGHT -> {
                            mDragListView.resetSwipedViews(null)
                            PhrasesUnitDetailActivity_.intent(context!!).extra("phrase", adapterItem).start()
                        }
                        else -> {}
                    }
                }
            })

            mDragListView.setLayoutManager(LinearLayoutManager(context!!))
            val listAdapter = PhrasesUnitItemAdapter(lst, R.layout.list_item_phrases_edit, R.id.image, false)
            mDragListView.setAdapter(listAdapter, true)
            mDragListView.setCanDragHorizontally(false)
            mDragListView.setCustomDragItem(PhrasesUnitDragItem(context!!, R.layout.list_item_phrases_edit))
            progressBar1.visibility = View.GONE
        }
    }

    @OptionsItem
    fun menuAdd() {
        val item = UnitPhrase()
        item.textbookid = vm.vm.ustextbookid
        // https://stackoverflow.com/questions/33640864/how-to-sort-based-on-compare-multiple-values-in-kotlin
        val maxItem = lst.maxWith(compareBy<UnitPhrase>({ it.unitpart }, { it.seqnum }))
        item.unit = maxItem?.unit ?: vm.vm.usunitto
        item.part = maxItem?.part ?: vm.vm.uspartto
        item.seqnum = (maxItem?.seqnum ?: 0) + 1
        PhrasesUnitDetailActivity_.intent(this).extra("phrase", item).start()
    }

    private class PhrasesUnitDragItem internal constructor(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<View>(R.id.item_layout).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background))
        }
    }

    private class PhrasesUnitItemAdapter(list: List<UnitPhrase>, private val mLayoutId: Int, private val mGrabHandleId: Int, private val mDragOnLongPress: Boolean) : DragItemAdapter<UnitPhrase, PhrasesUnitItemAdapter.ViewHolder>() {

        init {
            itemList = list
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(mLayoutId, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.mText1.text = mItemList[position].phrase
            holder.mText2.text = mItemList[position].translation
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
                val item = view!!.tag as UnitPhrase
                PhrasesUnitDetailActivity_.intent(view.context).extra("phrase", item).start()
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }

}
