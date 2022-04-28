package org.ireader.bookDetails.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.ireader.common_models.entities.Book
import org.ireader.core_api.source.Source
import javax.inject.Inject

open class DetailStateImpl @Inject constructor() : DetailState {
    override var source by mutableStateOf<Source?>(null)
    override var book by mutableStateOf<Book?>(null)
    override var inLibraryLoading by mutableStateOf<Boolean>(false)
    override var detailIsLoading by mutableStateOf<Boolean>(false)
    override var expandedSummary by mutableStateOf(false)
}

interface DetailState {
    var source: Source?
    var book: Book?
    var inLibraryLoading: Boolean
    var detailIsLoading: Boolean
    var expandedSummary: Boolean
}
