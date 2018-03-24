package com.zwstudio.lolly.android

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
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
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity(R.layout.activity_phrases_unit_edit)
class PhrasesUnitEditActivity : AppCompatActivity() {

    lateinit var lst: MutableList<UnitPhrase>

    @ViewById(R.id.drag_list_view)
    lateinit var mDragListView: DragListView
    @ViewById(R.id.swipe_refresh_layout)
    lateinit var mRefreshLayout: PhrasesUnitEditSwipeRefreshLayout
    @Bean
    lateinit var vm: PhrasesUnitViewModel

    @AfterViews
    fun afterViews() {
        lst = (intent.getSerializableExtra("lst") as Array<UnitPhrase>).toMutableList()
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
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_color))
        mRefreshLayout.setOnRefreshListener { mRefreshLayout.postDelayed({ mRefreshLayout.isRefreshing = false }, 2000) }

        mDragListView.setSwipeListener(object : ListSwipeHelper.OnSwipeListenerAdapter() {
            override fun onItemSwipeStarted(item: ListSwipeItem?) {
                mRefreshLayout.isEnabled = false
            }

            override fun onItemSwipeEnded(item: ListSwipeItem?, swipedDirection: ListSwipeItem.SwipeDirection?) {
                mRefreshLayout.isEnabled = true

                // Swipe to delete on left
                if (swipedDirection == ListSwipeItem.SwipeDirection.LEFT) {
                    val adapterItem = item!!.tag as UnitPhrase
                    val pos = mDragListView.adapter.getPositionForItem(adapterItem)
                    mDragListView.adapter.removeItem(pos)
                    vm.delete(adapterItem.id) {}
                }
            }
        })

        setupListRecyclerView()
    }

    private fun setupListRecyclerView() {
        mDragListView.setLayoutManager(LinearLayoutManager(this))
        val listAdapter = PhrasesUnitEditItemAdapter(lst, R.layout.list_item_phrases_edit, R.id.image, false)
        mDragListView.setAdapter(listAdapter, true)
        mDragListView.setCanDragHorizontally(false)
        mDragListView.setCustomDragItem(PhrasesUnitEditDragItem(this, R.layout.list_item_phrases_edit))
    }

    private class PhrasesUnitEditDragItem internal constructor(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            val text = (clickedView.findViewById<View>(R.id.text) as TextView).text
            (dragView.findViewById<View>(R.id.text) as TextView).text = text
            dragView.findViewById<View>(R.id.item_layout).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background))
        }
    }

    private class PhrasesUnitEditItemAdapter(list: List<UnitPhrase>, private val mLayoutId: Int, private val mGrabHandleId: Int, private val mDragOnLongPress: Boolean) : DragItemAdapter<UnitPhrase, PhrasesUnitEditItemAdapter.ViewHolder>() {

        init {
            itemList = list
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(mLayoutId, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val text = mItemList[position].phrase
            holder.mText.text = text
            holder.itemView.tag = mItemList[position]
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        internal inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, mGrabHandleId, mDragOnLongPress) {
            var mText: TextView

            init {
                mText = itemView.findViewById<View>(R.id.text) as TextView
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

class PhrasesUnitEditSwipeRefreshLayout : SwipeRefreshLayout {
    private var mScrollingView: View? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun canChildScrollUp(): Boolean {
        return mScrollingView != null && ViewCompat.canScrollVertically(mScrollingView!!, -1)
    }

    fun setScrollingView(scrollingView: View) {
        mScrollingView = scrollingView
    }
}

