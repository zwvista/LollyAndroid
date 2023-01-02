package com.zwstudio.lolly.views.words

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

// https://stackoverflow.com/questions/12578895/how-to-detect-a-swipe-gesture-on-webview
class OnSwipeWebviewTouchListener(ctx: Context, touchListener: TouchListener) : View.OnTouchListener {
    private val gestureDetector = GestureDetector(ctx, GestureListener(touchListener))
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener constructor(private val touchListener: TouchListener) : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return false // THIS does the trick
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (abs(diffX) > abs(diffY)) {
                    // You can customize these settings, so 30 is an example
                    if (abs(diffX) > 30 && abs(velocityX) > 30) {
                        if (diffX > 0) {
                            touchListener.onSwipeRight()
                        } else {
                            touchListener.onSwipeLeft()
                        }
                        result = true
                    }
                } else {
                    result = false
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }

    }

}

interface TouchListener {
    fun onSwipeLeft() {
        Log.d("Swipe", "Swipe left")
    }

    fun onSwipeRight() {
        Log.d("Swipe", "Swipe right")
    }
}