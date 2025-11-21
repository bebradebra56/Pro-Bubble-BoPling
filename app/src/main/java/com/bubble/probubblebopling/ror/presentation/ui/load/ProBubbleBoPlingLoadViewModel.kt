package com.bubble.probubblebopling.ror.presentation.ui.load

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProBubbleBoPlingLoadViewModel(
    private val proBubbleBoPlingGetAllUseCase: com.bubble.probubblebopling.ror.domain.usecases.ProBubbleBoPlingGetAllUseCase,
    private val chickenSharedPreference: com.bubble.probubblebopling.ror.data.shar.ProBubbleBoPlingSharedPreference,
    private val chickenSystemService: com.bubble.probubblebopling.ror.data.utils.ChickenSystemService
) : ViewModel() {

    private val _chickenHomeScreenState: MutableStateFlow<ChickenHomeScreenState> =
        MutableStateFlow(ChickenHomeScreenState.ChickenLoading)
    val chickenHomeScreenState = _chickenHomeScreenState.asStateFlow()

    private var chickenGetApps = false


    init {
        viewModelScope.launch {
            when (chickenSharedPreference.chickenAppState) {
                0 -> {
                    if (chickenSystemService.chickenIsOnline()) {
                        _root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.chickenConversionFlow.collect {
                            when(it) {
                                _root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ChickenAppsFlyerState.ChickenDefault -> {}
                                _root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ChickenAppsFlyerState.ChickenError -> {
                                    chickenSharedPreference.chickenAppState = 2
                                    _chickenHomeScreenState.value =
                                        ChickenHomeScreenState.ChickenError
                                    chickenGetApps = true
                                }
                                is com.bubble.probubblebopling.ror.presentation.app.ChickenAppsFlyerState.ChickenSuccess -> {
                                    if (!chickenGetApps) {
                                        chickenGetData(it.chickenData)
                                        chickenGetApps = true
                                    }
                                }
                            }
                        }
                    } else {
                        _chickenHomeScreenState.value =
                            ChickenHomeScreenState.ChickenNotInternet
                    }
                }
                1 -> {
                    if (chickenSystemService.chickenIsOnline()) {
                        if (_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_FB_LI != null) {
                            _chickenHomeScreenState.value =
                                ChickenHomeScreenState.ChickenSuccess(
                                    _root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_FB_LI.toString()
                                )
                        } else if (System.currentTimeMillis() / 1000 > chickenSharedPreference.chickenExpired) {
                            Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Current time more then expired, repeat request")
                            _root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.chickenConversionFlow.collect {
                                when(it) {
                                    _root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ChickenAppsFlyerState.ChickenDefault -> {}
                                    _root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ChickenAppsFlyerState.ChickenError -> {
                                        _chickenHomeScreenState.value =
                                            ChickenHomeScreenState.ChickenSuccess(
                                                chickenSharedPreference.chickenSavedUrl
                                            )
                                        chickenGetApps = true
                                    }
                                    is com.bubble.probubblebopling.ror.presentation.app.ChickenAppsFlyerState.ChickenSuccess -> {
                                        if (!chickenGetApps) {
                                            chickenGetData(it.chickenData)
                                            chickenGetApps = true
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Current time less then expired, use saved url")
                            _chickenHomeScreenState.value =
                                ChickenHomeScreenState.ChickenSuccess(
                                    chickenSharedPreference.chickenSavedUrl
                                )
                        }
                    } else {
                        _chickenHomeScreenState.value =
                            ChickenHomeScreenState.ChickenNotInternet
                    }
                }
                2 -> {
                    _chickenHomeScreenState.value =
                        ChickenHomeScreenState.ChickenError
                }
            }
        }
    }


    private suspend fun chickenGetData(conversation: MutableMap<String, Any>?) {
        val chickenData = proBubbleBoPlingGetAllUseCase.invoke(conversation)
        if (chickenSharedPreference.chickenAppState == 0) {
            if (chickenData == null) {
                chickenSharedPreference.chickenAppState = 2
                _chickenHomeScreenState.value =
                    ChickenHomeScreenState.ChickenError
            } else {
                chickenSharedPreference.chickenAppState = 1
                chickenSharedPreference.apply {
                    chickenExpired = chickenData.chickenExpires
                    chickenSavedUrl = chickenData.chickenUrl
                }
                _chickenHomeScreenState.value =
                    ChickenHomeScreenState.ChickenSuccess(chickenData.chickenUrl)
            }
        } else  {
            if (chickenData == null) {
                _chickenHomeScreenState.value =
                    ChickenHomeScreenState.ChickenSuccess(chickenSharedPreference.chickenSavedUrl)
            } else {
                chickenSharedPreference.apply {
                    chickenExpired = chickenData.chickenExpires
                    chickenSavedUrl = chickenData.chickenUrl
                }
                _chickenHomeScreenState.value =
                    ChickenHomeScreenState.ChickenSuccess(chickenData.chickenUrl)
            }
        }
    }


    sealed class ChickenHomeScreenState {
        data object ChickenLoading : ChickenHomeScreenState()
        data object ChickenError : ChickenHomeScreenState()
        data class ChickenSuccess(val data: String) : ChickenHomeScreenState()
        data object ChickenNotInternet: ChickenHomeScreenState()
    }
}