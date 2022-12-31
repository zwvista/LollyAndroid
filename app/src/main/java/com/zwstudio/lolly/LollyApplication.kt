package com.zwstudio.lolly.views

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.androidisland.vita.startVita
import com.zwstudio.lolly.common.onCreateApp

class LollyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startVita()
        onCreateApp(this)
    }
}

fun yesNoDialog(context: Context, message: String, yesAction: () -> Unit, noAction: () -> Unit) {
    val dialogClickListener = DialogInterface.OnClickListener { _, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE ->
                //Yes button clicked
                yesAction()
            DialogInterface.BUTTON_NEGATIVE -> {
                //No button clicked
                noAction()
            }
        }
    }

    AlertDialog.Builder(context)
        .setMessage(message).setPositiveButton("Yes", dialogClickListener)
        .setNegativeButton("No", dialogClickListener).show()
}

class LollySwipeRefreshLayout : SwipeRefreshLayout {
    private var mScrollingView: View? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun canChildScrollUp(): Boolean {
        return mScrollingView != null && mScrollingView!!.canScrollVertically(-1)
    }

    fun setScrollingView(scrollingView: View) {
        mScrollingView = scrollingView
    }
}
