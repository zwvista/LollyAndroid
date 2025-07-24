package com.zwstudio.lolly.ui.common

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragListView
import com.zwstudio.lolly.R
import com.zwstudio.lolly.viewmodels.LollyListViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

abstract class LollyListFragment : Fragment() {

    var mDragListView by autoCleared<DragListView>()
    var mRefreshLayout by autoCleared<LollySwipeRefreshLayout>()
    var progressBar1 by autoCleared<ProgressBar>()
    abstract val vmList: LollyListViewModel
    abstract suspend fun onRefresh()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDragListView = view.findViewById(R.id.drag_list_view)
        mRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        progressBar1 = view.findViewById(R.id.progressBar1)
    }

    fun setupListView(dragItem: DragItem? = null) {
        mDragListView.recyclerView.isVerticalScrollBarEnabled = true

        mRefreshLayout.setScrollingView(mDragListView.recyclerView)
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(),
            R.color.app_color
        ))
        mRefreshLayout.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                onRefresh();
                mRefreshLayout.isRefreshing = false
            }
        }

        mDragListView.setLayoutManager(LinearLayoutManager(requireContext()))
        if (dragItem == null)
            mDragListView.isDragEnabled = false
        else {
            mDragListView.setDragListListener(object : DragListView.DragListListenerAdapter() {
                override fun onItemDragStarted(position: Int) {
                    mRefreshLayout.isEnabled = false
                    Toast.makeText(mDragListView.context, "Start - position: $position", Toast.LENGTH_SHORT).show()
                }
                override fun onItemDragEnded(fromPosition: Int, toPosition: Int) {
                    mRefreshLayout.isEnabled = true
                    Toast.makeText(mDragListView.context, "End - position: $toPosition", Toast.LENGTH_SHORT).show()
                    vmList.reindex {}
                }
            })
            mDragListView.setCanDragHorizontally(false)
            mDragListView.setCustomDragItem(dragItem)
        }

        vmList.isBusy_.onEach {
            progressBar1.visibility = if (it) View.VISIBLE else View.GONE
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            onRefresh()
        }
    }
}
