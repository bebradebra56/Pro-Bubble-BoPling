package com.bubble.probubblebopling.ror

import android.R
import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout

class ProBubbleBoPlingGlobalLayoutUtil {

    private var chickenMChildOfContent: View? = null
    private var chickenUsableHeightPrevious = 0

    fun chickenAssistActivity(activity: Activity) {
        val content = activity.findViewById<FrameLayout>(R.id.content)
        chickenMChildOfContent = content.getChildAt(0)

        chickenMChildOfContent?.viewTreeObserver?.addOnGlobalLayoutListener {
            possiblyResizeChildOfContent(activity)
        }
    }

    private fun possiblyResizeChildOfContent(activity: Activity) {
        val chickenUsableHeightNow = chickenComputeUsableHeight()
        if (chickenUsableHeightNow != chickenUsableHeightPrevious) {
            val chickenUsableHeightSansKeyboard = chickenMChildOfContent?.rootView?.height ?: 0
            val chickenHeightDifference = chickenUsableHeightSansKeyboard - chickenUsableHeightNow

            if (chickenHeightDifference > (chickenUsableHeightSansKeyboard / 4)) {
                activity.window.setSoftInputMode(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.chickenInputMode)
            } else {
                activity.window.setSoftInputMode(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.chickenInputMode)
            }
//            mChildOfContent?.requestLayout()
            chickenUsableHeightPrevious = chickenUsableHeightNow
        }
    }

    private fun chickenComputeUsableHeight(): Int {
        val r = Rect()
        chickenMChildOfContent?.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top  // Visible height без status bar
    }
}