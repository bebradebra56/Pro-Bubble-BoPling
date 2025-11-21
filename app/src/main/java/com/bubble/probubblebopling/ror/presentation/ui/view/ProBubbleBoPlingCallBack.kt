package com.bubble.probubblebopling.ror.presentation.ui.view


import android.webkit.PermissionRequest

interface ProBubbleBoPlingCallBack {
    fun chickenHandleCreateWebWindowRequest(proBubbleBoPlingVi: ProBubbleBoPlingVi)

    fun chickenOnPermissionRequest(todoSphereRequest: PermissionRequest?)

    fun chickenOnFirstPageFinished()
}