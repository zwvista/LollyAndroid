package com.zwstudio.lolly.android

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.swipe.ListSwipeHelper
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.data.DrawerListViewModel
import java.util.*

open class DrawerListFragment : Fragment(), TextToSpeech.OnInitListener {

    var mDragListView by autoCleared<DragListView>()
    var mRefreshLayout by autoCleared<LollySwipeRefreshLayout>()
    var progressBar1 by autoCleared<ProgressBar>()
    open val vmDrawerList: DrawerListViewModel? get() = null
    lateinit var tts: TextToSpeech

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) return
        val locale = Locale.getAvailableLocales().find {
            "${it.language}_${it.country}" == vmSettings.selectedVoice?.voicelang
        }
        if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return
        tts.language = locale
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tts.shutdown()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tts = TextToSpeech(requireContext(), this)
        mDragListView = view.findViewById(R.id.drag_list_view)
        mRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        progressBar1 = view.findViewById(R.id.progressBar1)
    }

    fun setupListView(dragItem: DragItem? = null) {
        mDragListView.recyclerView.isVerticalScrollBarEnabled = true

        mRefreshLayout.setScrollingView(mDragListView.recyclerView)
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.app_color))
        mRefreshLayout.setOnRefreshListener { mRefreshLayout.postDelayed({ mRefreshLayout.isRefreshing = false }, 2000) }

        mDragListView.setSwipeListener(object : ListSwipeHelper.OnSwipeListenerAdapter() {
            override fun onItemSwipeStarted(item: ListSwipeItem?) {
                mRefreshLayout.isEnabled = false
            }
            override fun onItemSwipeEnded(item: ListSwipeItem?, swipedDirection: ListSwipeItem.SwipeDirection?) {
                mRefreshLayout.isEnabled = true
                when (swipedDirection) {
                    ListSwipeItem.SwipeDirection.LEFT -> vmDrawerList?.isSwipeStarted = true
                    ListSwipeItem.SwipeDirection.RIGHT -> vmDrawerList?.isSwipeStarted = true
                    else -> {}
                }
            }
        })

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
                    vmDrawerList?.reindex {}
                }
            })
            mDragListView.setCanDragHorizontally(false)
            mDragListView.setCustomDragItem(dragItem)
        }
    }
}
