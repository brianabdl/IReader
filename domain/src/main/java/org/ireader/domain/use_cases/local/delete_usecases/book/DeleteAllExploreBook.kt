package org.ireader.domain.use_cases.local.delete_usecases.book

import org.ireader.common_data.repository.LocalBookRepository
import org.ireader.common_models.entities.Book
import javax.inject.Inject

/**
 * Delete All Books That are paged in Explore Screen
 */
class DeleteAllExploreBook @Inject constructor(private val localBookRepository: org.ireader.common_data.repository.LocalBookRepository) {
    suspend operator fun invoke() {
        return localBookRepository.deleteAllExploreBook()
    }
}

class DeleteBooks @Inject constructor(private val localBookRepository: org.ireader.common_data.repository.LocalBookRepository) {
    suspend operator fun invoke(books: List<Book>) {
        localBookRepository.deleteBooks(books)
    }
}

class DeleteBookAndChapterByBookIds @Inject constructor(private val localBookRepository: org.ireader.common_data.repository.LocalBookRepository) {
    suspend operator fun invoke(bookIds: List<Long>) {
        localBookRepository.deleteBookAndChapterByBookIds(bookIds)
    }
}
