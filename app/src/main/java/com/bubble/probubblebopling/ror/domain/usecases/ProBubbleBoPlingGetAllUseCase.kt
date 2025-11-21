package com.bubble.probubblebopling.ror.domain.usecases

import android.util.Log

class ProBubbleBoPlingGetAllUseCase(
    private val chickenRepository: com.bubble.probubblebopling.ror.data.repo.ChickenRepository,
    private val chickenSystemService: com.bubble.probubblebopling.ror.data.utils.ChickenSystemService,
    private val chickenPushToken: com.bubble.probubblebopling.ror.data.utils.ChickenPushToken,
) {
    suspend operator fun invoke(conversion: MutableMap<String, Any>?) : com.bubble.probubblebopling.ror.domain.model.ProBubbleBoPlingEntity?{
        val params = _root_ide_package_.com.bubble.probubblebopling.ror.domain.model.ChickenParam(
            chickenLocale = chickenSystemService.chickenGetLocale(),
            chickenPushToken = chickenPushToken.chickenGetToken(),
            chickenAfId = chickenSystemService.chickenGetAppsflyerId()
        )
//        chickenSystemService.bubblePasswrodGetGaid()
        Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Params for request: $params")
        return chickenRepository.chickenGetClient(params, conversion)
    }



}