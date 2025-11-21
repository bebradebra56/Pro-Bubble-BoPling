package com.bubble.probubblebopling

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.bubble.probubblebopling.ror.chickenSetupSystemBars
import org.koin.android.ext.android.inject

class ProBubbleBoPlingActivity : androidx.appcompat.app.AppCompatActivity() {

    private val proBubbleBoPlingPushHandler by inject<com.bubble.probubblebopling.ror.presentation.pushhandler.ProBubbleBoPlingPushHandler>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chickenSetupSystemBars()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_chicken)
        val chickenRootView = findViewById<View>(android.R.id.content)
        _root_ide_package_.com.bubble.probubblebopling.ror.ProBubbleBoPlingGlobalLayoutUtil().chickenAssistActivity(this)
        ViewCompat.setOnApplyWindowInsetsListener(chickenRootView) { chickenView, chickenInsets ->
            val chickenSystemBars = chickenInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val chickenDisplayCutout = chickenInsets.getInsets(WindowInsetsCompat.Type.displayCutout())
            val chickenIme = chickenInsets.getInsets(WindowInsetsCompat.Type.ime())


            val chickenTopPadding = maxOf(chickenSystemBars.top, chickenDisplayCutout.top)
            val chickenLeftPadding = maxOf(chickenSystemBars.left, chickenDisplayCutout.left)
            val chickenRightPadding = maxOf(chickenSystemBars.right, chickenDisplayCutout.right)
            window.setSoftInputMode(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.chickenInputMode)

            if (window.attributes.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) {
                Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "ADJUST PUN")
                val chickenBottomInset = maxOf(chickenSystemBars.bottom, chickenDisplayCutout.bottom)

                chickenView.setPadding(chickenLeftPadding, chickenTopPadding, chickenRightPadding, 0)

                chickenView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = chickenBottomInset
                }
            } else {
                Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "ADJUST RESIZE")

                val chickenBottomInset = maxOf(chickenSystemBars.bottom, chickenDisplayCutout.bottom, chickenIme.bottom)

                chickenView.setPadding(chickenLeftPadding, chickenTopPadding, chickenRightPadding, 0)

                chickenView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = chickenBottomInset
                }
            }



            WindowInsetsCompat.CONSUMED
        }
        Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Activity onCreate()")
        proBubbleBoPlingPushHandler.chickenHandlePush(intent.extras)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            chickenSetupSystemBars()
        }
    }

    override fun onResume() {
        super.onResume()
        chickenSetupSystemBars()
    }
}