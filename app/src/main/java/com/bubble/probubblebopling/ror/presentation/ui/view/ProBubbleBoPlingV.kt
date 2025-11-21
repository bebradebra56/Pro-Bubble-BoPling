package com.bubble.probubblebopling.ror.presentation.ui.view

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import org.koin.android.ext.android.inject

class ProBubbleBoPlingV : androidx.fragment.app.Fragment(){

    private lateinit var chickenPhoto: Uri
    private var chickenFilePathFromChrome: ValueCallback<Array<Uri>>? = null

    private val chickenTakeFile: ActivityResultLauncher<PickVisualMediaRequest> = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        chickenFilePathFromChrome?.onReceiveValue(arrayOf(it ?: Uri.EMPTY))
        chickenFilePathFromChrome = null
    }

    private val chickenTakePhoto: ActivityResultLauncher<Uri> = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            chickenFilePathFromChrome?.onReceiveValue(arrayOf(chickenPhoto))
            chickenFilePathFromChrome = null
        } else {
            chickenFilePathFromChrome?.onReceiveValue(null)
            chickenFilePathFromChrome = null
        }
    }

    private val proBubbleBoPlingDataStore by activityViewModels<ProBubbleBoPlingDataStore>()


    private val chickenViFun by inject<ProBubbleBoPlingViFun>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Fragment onCreate")
        CookieManager.getInstance().setAcceptCookie(true)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (proBubbleBoPlingDataStore.proBubbleBoPlingView.canGoBack()) {
                        proBubbleBoPlingDataStore.proBubbleBoPlingView.goBack()
                        Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "WebView can go back")
                    } else if (proBubbleBoPlingDataStore.proBubbleBoPlingViList.size > 1) {
                        Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "WebView can`t go back")
                        proBubbleBoPlingDataStore.proBubbleBoPlingViList.removeAt(proBubbleBoPlingDataStore.proBubbleBoPlingViList.lastIndex)
                        Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "WebView list size ${proBubbleBoPlingDataStore.proBubbleBoPlingViList.size}")
                        proBubbleBoPlingDataStore.proBubbleBoPlingView.destroy()
                        val previousWebView = proBubbleBoPlingDataStore.proBubbleBoPlingViList.last()
                        attachWebViewToContainer(previousWebView)
                        proBubbleBoPlingDataStore.proBubbleBoPlingView = previousWebView
                    }
                }

            })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (proBubbleBoPlingDataStore.chickenIsFirstCreate) {
            proBubbleBoPlingDataStore.chickenIsFirstCreate = false
            proBubbleBoPlingDataStore.chickenContainerView = FrameLayout(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                id = View.generateViewId()
            }
            return proBubbleBoPlingDataStore.chickenContainerView
        } else {
            return proBubbleBoPlingDataStore.chickenContainerView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "onViewCreated")
        if (proBubbleBoPlingDataStore.proBubbleBoPlingViList.isEmpty()) {
            proBubbleBoPlingDataStore.proBubbleBoPlingView = ProBubbleBoPlingVi(requireContext(), object :
                ProBubbleBoPlingCallBack {
                override fun chickenHandleCreateWebWindowRequest(proBubbleBoPlingVi: ProBubbleBoPlingVi) {
                    proBubbleBoPlingDataStore.proBubbleBoPlingViList.add(proBubbleBoPlingVi)
                    Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "WebView list size = ${proBubbleBoPlingDataStore.proBubbleBoPlingViList.size}")
                    Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "CreateWebWindowRequest")
                    proBubbleBoPlingDataStore.proBubbleBoPlingView = proBubbleBoPlingVi
                    proBubbleBoPlingVi.setFileChooserHandler { callback ->
                        handleFileChooser(callback)
                    }
                    attachWebViewToContainer(proBubbleBoPlingVi)
                }

                override fun chickenOnPermissionRequest(chickenRequest: PermissionRequest?) {
                    chickenRequest?.grant(chickenRequest.resources)
                }

                override fun chickenOnFirstPageFinished() {
                    proBubbleBoPlingDataStore.chickenSetIsFirstFinishPage()
                }

            }, chickenWindow = requireActivity().window).apply {
                setFileChooserHandler { callback ->
                    handleFileChooser(callback)
                }
            }
            proBubbleBoPlingDataStore.proBubbleBoPlingView.chickenFLoad(arguments?.getString(
                _root_ide_package_.com.bubble.probubblebopling.ror.presentation.ui.load.ProBubbleBoPlingLoadFragment.Companion.CHICKEN_D) ?: "")
//            ejvview.fLoad("www.google.com")
            proBubbleBoPlingDataStore.proBubbleBoPlingViList.add(proBubbleBoPlingDataStore.proBubbleBoPlingView)
            attachWebViewToContainer(proBubbleBoPlingDataStore.proBubbleBoPlingView)
        } else {
            proBubbleBoPlingDataStore.proBubbleBoPlingViList.forEach { webView ->
                webView.setFileChooserHandler { callback ->
                    handleFileChooser(callback)
                }
            }
            proBubbleBoPlingDataStore.proBubbleBoPlingView = proBubbleBoPlingDataStore.proBubbleBoPlingViList.last()

            attachWebViewToContainer(proBubbleBoPlingDataStore.proBubbleBoPlingView)
        }
        Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "WebView list size = ${proBubbleBoPlingDataStore.proBubbleBoPlingViList.size}")
    }

    private fun handleFileChooser(callback: ValueCallback<Array<Uri>>?) {
        Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "handleFileChooser called, callback: ${callback != null}")

        chickenFilePathFromChrome = callback

        val listItems: Array<out String> = arrayOf("Select from file", "To make a photo")
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                0 -> {
                    Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Launching file picker")
                    chickenTakeFile.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
                1 -> {
                    Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Launching camera")
                    chickenPhoto = chickenViFun.chickenSavePhoto()
                    chickenTakePhoto.launch(chickenPhoto)
                }
            }
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Choose a method")
            .setItems(listItems, listener)
            .setCancelable(true)
            .setOnCancelListener {
                Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "File chooser canceled")
                callback?.onReceiveValue(null)
                chickenFilePathFromChrome = null
            }
            .create()
            .show()
    }

    private fun attachWebViewToContainer(w: ProBubbleBoPlingVi) {
        proBubbleBoPlingDataStore.chickenContainerView.post {
            // Убираем предыдущую WebView, если есть
            (w.parent as? ViewGroup)?.removeView(w)
            proBubbleBoPlingDataStore.chickenContainerView.removeAllViews()
            proBubbleBoPlingDataStore.chickenContainerView.addView(w)
        }
    }


}