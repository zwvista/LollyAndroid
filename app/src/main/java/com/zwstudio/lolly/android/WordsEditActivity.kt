package com.zwstudio.lolly.android

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
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
import com.zwstudio.lolly.domain.UnitWord
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity(R.layout.activity_words_edit)
class WordsEditActivity : AppCompatActivity() {

    lateinit var lst: List<UnitWord>

    @ViewById(R.id.drag_list_view)
    lateinit var mDragListView: DragListView
    @ViewById(R.id.swipe_refresh_layout)
    lateinit var mRefreshLayout: MySwipeRefreshLayout


    @AfterViews
    fun afterViews() {
        lst = (intent.getSerializableExtra("lst") as Array<UnitWord>).toList()
        mDragListView.recyclerView.isVerticalScrollBarEnabled = true
        mDragListView.setDragListListener(object : DragListView.DragListListenerAdapter() {
            override fun onItemDragStarted(position: Int) {
                mRefreshLayout.isEnabled = false
                Toast.makeText(mDragListView.context, "Start - position: $position", Toast.LENGTH_SHORT).show()
            }

            override fun onItemDragEnded(fromPosition: Int, toPosition: Int) {
                mRefreshLayout.isEnabled = true
                if (fromPosition != toPosition) {
                    Toast.makeText(mDragListView.context, "End - position: $toPosition", Toast.LENGTH_SHORT).show()
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
                    val adapterItem = item!!.tag as UnitWord
                    val pos = mDragListView.adapter.getPositionForItem(adapterItem)
                    mDragListView.adapter.removeItem(pos)
                }
            }
        })

        setupListRecyclerView()
    }


    private fun setupListRecyclerView() {
        mDragListView.setLayoutManager(LinearLayoutManager(this))
        val listAdapter = ItemAdapter(lst, R.layout.list_item, R.id.image, false)
        mDragListView.setAdapter(listAdapter, true)
        mDragListView.setCanDragHorizontally(false)
        mDragListView.setCustomDragItem(MyDragItem(this, R.layout.list_item))
    }

    private class MyDragItem internal constructor(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            val text = (clickedView.findViewById<View>(R.id.text) as TextView).text
            (dragView.findViewById<View>(R.id.text) as TextView).text = text
            dragView.findViewById<View>(R.id.item_layout).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background))
        }
    }
}

internal class ItemAdapter(list: List<UnitWord>, private val mLayoutId: Int, private val mGrabHandleId: Int, private val mDragOnLongPress: Boolean) : DragItemAdapter<UnitWord, ItemAdapter.ViewHolder>() {

    init {
        itemList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(mLayoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val text = mItemList[position].word
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
            Toast.makeText(view!!.context, "Item clicked", Toast.LENGTH_SHORT).show()
        }

        override fun onItemLongClicked(view: View?): Boolean {
            Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
            return true
        }
    }
}
