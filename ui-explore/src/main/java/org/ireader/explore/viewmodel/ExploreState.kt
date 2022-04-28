package org.ireader.explore.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.ireader.common_extensions.UiText
import org.ireader.common_models.LayoutType
import org.ireader.common_models.entities.BookItem
import org.ireader.core_api.source.CatalogSource
import org.ireader.core_api.source.model.Filter
import org.ireader.core_api.source.model.Listing
import javax.inject.Inject

interface ExploreState {
    var isLoading: Boolean
    var error: org.ireader.common_extensions.UiText?
    val layout: LayoutType
    val isSearchModeEnable: Boolean
    var searchQuery: String?
    val source: CatalogSource?
    val isFilterEnable: Boolean
    var topMenuEnable: Boolean

    // var listing: Listing?
    var modifiedFilter: List<Filter<*>>

    var page: Int
    var stateItems: List<BookItem>
    var endReached: Boolean

    var stateFilters: List<Filter<*>>?
    var stateListing: Listing?
}

open class ExploreStateImpl @Inject constructor() : ExploreState {
    override var isLoading by mutableStateOf<Boolean>(false)
    override var error by mutableStateOf<org.ireader.common_extensions.UiText?>(null)
    override var layout by mutableStateOf<LayoutType>(LayoutType.GridLayout)
    override var isSearchModeEnable by mutableStateOf<Boolean>(false)
    override var searchQuery by mutableStateOf<String?>(null)
    override var source by mutableStateOf<CatalogSource?>(null)
    override var isFilterEnable by mutableStateOf<Boolean>(false)
    override var topMenuEnable: Boolean by mutableStateOf<Boolean>(false)
    override var modifiedFilter by mutableStateOf(emptyList<Filter<*>>())
    override var page by mutableStateOf<Int>(1)
    override var stateItems by mutableStateOf<List<BookItem>>(emptyList())
    override var endReached by mutableStateOf(false)
    override var stateFilters by mutableStateOf<List<Filter<*>>?>(null)
    override var stateListing by mutableStateOf<Listing?>(null)
}
