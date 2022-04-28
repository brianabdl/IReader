package org.ireader.downloader

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.ireader.common_models.entities.SavedDownload
import org.ireader.core_ui.viewmodel.BaseViewModel
import org.ireader.domain.services.downloaderService.DownloadServiceStateImpl
import org.ireader.domain.services.downloaderService.DownloaderService
import org.ireader.domain.use_cases.download.DownloadUseCases
import org.ireader.domain.use_cases.local.LocalGetChapterUseCase
import org.ireader.domain.use_cases.local.LocalInsertUseCases
import org.ireader.domain.use_cases.services.ServiceUseCases
import javax.inject.Inject

@HiltViewModel
class DownloaderViewModel @Inject constructor(
    private val getBookUseCases: org.ireader.domain.use_cases.local.LocalGetBookUseCases,
    private val getChapterUseCase: LocalGetChapterUseCase,
    private val insertUseCases: LocalInsertUseCases,
    private val downloadUseCases: DownloadUseCases,
    private val serviceUseCases: ServiceUseCases,
    private val downloadState: DownloadStateImpl,
    val downloadServiceStateImpl: DownloadServiceStateImpl
) : BaseViewModel(), DownloadState by downloadState {

    init {
        subscribeDownloads()
    }

    fun insertSavedDownload(download: SavedDownload) {
        viewModelScope.launch(Dispatchers.IO) {
            downloadUseCases.insertDownload(
                download = download
            )
        }
    }

    private var getBooksJob: Job? = null
    private fun subscribeDownloads() {
        getBooksJob?.cancel()
        getBooksJob = viewModelScope.launch {
            downloadUseCases.subscribeDownloadsUseCase().distinctUntilChanged().collect {
                downloads = it.filter { it.chapterId != 0L }
            }
        }
    }

    fun startDownloadService(chapterIds: List<Long>) {
        serviceUseCases.startDownloadServicesUseCase(
            chapterIds = chapterIds.toLongArray()
        )
    }

    fun stopDownloads() {
        serviceUseCases.stopServicesUseCase(
            DownloaderService.DOWNLOADER_SERVICE_NAME
        )
    }

    fun toggleExpandMenu(enable: Boolean = true) {
        isMenuExpanded = enable
    }

    fun deleteAllDownloads() {
        viewModelScope.launch(Dispatchers.IO) {
            downloadUseCases.deleteAllSavedDownload()
        }
    }
    fun deleteSelectedDownloads(list: List<SavedDownload>) {
        viewModelScope.launch(Dispatchers.IO) {
            downloadUseCases.deleteSavedDownloads(list)
        }
    }
}
