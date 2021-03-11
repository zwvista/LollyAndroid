package com.zwstudio.lolly.android

import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.woxthebox.draglistview.DragListView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById

@EFragment
class DrawerListFragment : Fragment() {

    @ViewById(R.id.drag_list_view)
    lateinit var mDragListView: DragListView
    @ViewById(R.id.swipe_refresh_layout)
    lateinit var mRefreshLayout: LollySwipeRefreshLayout
    @ViewById
    lateinit var progressBar1: ProgressBar
}
