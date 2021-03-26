package com.zwstudio.lolly.android

import android.speech.tts.TextToSpeech
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.swipe.ListSwipeHelper
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.zwstudio.lolly.data.DrawerListViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById
import java.util.*

@EFragment
class DrawerListFragment : Fragment(), TextToSpeech.OnInitListener {

    @ViewById(R.id.drag_list_view)
    lateinit var mDragListView: DragListView
    @ViewById(R.id.swipe_refresh_layout)
    lateinit var mRefreshLayout: LollySwipeRefreshLayout
    @ViewById
    lateinit var progressBar1: ProgressBar
    val vmDrawerList: DrawerListViewModel? get() = null
    lateinit var tts: TextToSpeech

    val compositeDisposable = CompositeDisposable()

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) return
        val locale = Locale.getAvailableLocales().find {
            "${it.language}_${it.country}" == vmDrawerList?.vmSettings?.selectedVoice?.voicelang
        }
        if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return
        tts.language = locale
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tts.shutdown()
    }

    fun afterViews() {
        tts = TextToSpeech(requireContext(), this)
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
