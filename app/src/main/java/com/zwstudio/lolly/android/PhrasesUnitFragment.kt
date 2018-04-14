package com.zwstudio.lolly.android

import android.content.Context
import android.support.v4.content.ContextCompat
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
import com.zwstudio.lolly.data.PhrasesUnitViewModel
import com.zwstudio.lolly.domain.UnitPhrase
import org.androidannotations.annotations.*


@EFragment(R.layout.content_phrases_unit)
@OptionsMenu(R.menu.menu_add)
class PhrasesUnitFragment : DrawerListFragment() {

    @Bean
    lateinit var vm: PhrasesUnitViewModel

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
            val listAdapter = PhrasesUnitItemAdapter(vm, mDragListView, R.layout.list_item_phrases_edit, R.id.image, false)
            mDragListView.setAdapter(listAdapter, true)
            mDragListView.setCanDragHorizontally(false)
            mDragListView.setCustomDragItem(PhrasesUnitDragItem(context!!, R.layout.list_item_phrases_edit))
            progressBar1.visibility = View.GONE
        }
    }

    @OptionsItem
    fun menuAdd() {
        PhrasesUnitDetailActivity_.intent(this)
                .extra("list", vm.lstPhrases.toTypedArray()).extra("phrase", vm.newUnitPhrase()).start()
    }

    private class PhrasesUnitDragItem internal constructor(context: Context, layoutId: Int) : DragItem(context, layoutId) {

        override fun onBindDragView(clickedView: View, dragView: View) {
            dragView.findViewById<TextView>(R.id.text1).text = clickedView.findViewById<TextView>(R.id.text1).text
            dragView.findViewById<TextView>(R.id.text2).text = clickedView.findViewById<TextView>(R.id.text2).text
            dragView.findViewById<TextView>(R.id.text3).text = clickedView.findViewById<TextView>(R.id.text3).text
            dragView.findViewById<View>(R.id.item_layout).setBackgroundColor(dragView.resources.getColor(R.color.list_item_background))
        }
    }

    private class PhrasesUnitItemAdapter(val vm: PhrasesUnitViewModel, val mDragListView: DragListView, val mLayoutId: Int, val mGrabHandleId: Int, val mDragOnLongPress: Boolean) : DragItemAdapter<UnitPhrase, PhrasesUnitItemAdapter.ViewHolder>() {

        init {
            itemList = vm.lstPhrases
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(mLayoutId, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.mText1.text = mItemList[position].phrase
            holder.mText2.text = mItemList[position].unitpartseqnum(vm.vmSettings.lstParts)
            holder.mText3.text = mItemList[position].translation
            holder.itemView.tag = mItemList[position]
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].id.toLong()
        }

        internal inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, mGrabHandleId, mDragOnLongPress) {
            var mText1: TextView
            var mText2: TextView
            var mText3: TextView
            var mEdit: TextView
            var mDelete: TextView

            init {
                mText1 = itemView.findViewById(R.id.text1)
                mText2 = itemView.findViewById(R.id.text2)
                mText3 = itemView.findViewById(R.id.text3)
                mEdit = itemView.findViewById(R.id.item_edit)
                mEdit.setOnTouchListener { v, event ->
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as UnitPhrase
                        PhrasesUnitDetailActivity_.intent(itemView.context)
                                .extra("list", vm.lstPhrases.toTypedArray()).extra("phrase", item).start()
                    }
                    true
                }
                mDelete = itemView.findViewById(R.id.item_delete)
                mDelete.setOnTouchListener { v, event ->
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        val item = itemView.tag as UnitPhrase
                        yesNoDialog(itemView.context, "Are you sure you want to delete the phrase \"${item.phrase}\"?", {
                            val pos = mDragListView.adapter.getPositionForItem(item)
                            mDragListView.adapter.removeItem(pos)
                            vm.delete(item.id) {}
                            vm.isSwipeStarted = false
                        }, {
                            mDragListView.resetSwipedViews(null)
                            vm.isSwipeStarted = false
                        })
                    }
                    true
                }
            }

            override fun onItemClicked(view: View?) {
                if (vm.isSwipeStarted) {
                    mDragListView.resetSwipedViews(null)
                    vm.isSwipeStarted = false
                } else {
                    val item = view!!.tag as UnitPhrase
                    PhrasesUnitDetailActivity_.intent(view.context)
                            .extra("list", vm.lstPhrases.toTypedArray()).extra("phrase", item).start()
                }
            }

            override fun onItemLongClicked(view: View?): Boolean {
                Toast.makeText(view!!.context, "Item long clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }

}
