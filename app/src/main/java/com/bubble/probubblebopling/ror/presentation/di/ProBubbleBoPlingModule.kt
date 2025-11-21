package com.bubble.probubblebopling.ror.presentation.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val chickenModule = module {
    factory {
        _root_ide_package_.com.bubble.probubblebopling.ror.presentation.pushhandler.ProBubbleBoPlingPushHandler()
    }
    single {
        _root_ide_package_.com.bubble.probubblebopling.ror.data.repo.ChickenRepository()
    }
    single {
        _root_ide_package_.com.bubble.probubblebopling.ror.data.shar.ProBubbleBoPlingSharedPreference(get())
    }
    factory {
        _root_ide_package_.com.bubble.probubblebopling.ror.data.utils.ChickenPushToken()
    }
    factory {
        _root_ide_package_.com.bubble.probubblebopling.ror.data.utils.ChickenSystemService(get())
    }
    factory {
        _root_ide_package_.com.bubble.probubblebopling.ror.domain.usecases.ProBubbleBoPlingGetAllUseCase(
            get(), get(), get()
        )
    }
    factory {
        _root_ide_package_.com.bubble.probubblebopling.ror.presentation.ui.view.ProBubbleBoPlingViFun(get())
    }
    viewModel {
        _root_ide_package_.com.bubble.probubblebopling.ror.presentation.ui.load.ProBubbleBoPlingLoadViewModel(
            get(),
            get(),
            get()
        )
    }
}