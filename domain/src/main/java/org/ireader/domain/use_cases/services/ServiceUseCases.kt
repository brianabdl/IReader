package org.ireader.domain.use_cases.services

import javax.inject.Inject

data class ServiceUseCases @Inject constructor(
    val startDownloadServicesUseCase: StartDownloadServicesUseCase,
    val startLibraryUpdateServicesUseCase: StartLibraryUpdateServicesUseCase,
    val startTTSServicesUseCase: StartTTSServicesUseCase,
    val stopServicesUseCase: StopServiceUseCase,
)
