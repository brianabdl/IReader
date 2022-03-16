package org.ireader.infinity.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.ireader.core.prefs.AndroidPreferenceStore
import org.ireader.data.local.AppDatabase
import org.ireader.data.local.MIGRATION_11_12
import org.ireader.data.local.MIGRATION_8_9
import org.ireader.data.local.dao.DownloadDao
import org.ireader.data.local.dao.LibraryBookDao
import org.ireader.data.local.dao.LibraryChapterDao
import org.ireader.data.local.dao.RemoteKeysDao
import org.ireader.data.repository.DownloadRepositoryImpl
import org.ireader.domain.feature_services.io.LibraryCovers
import org.ireader.domain.repository.DownloadRepository
import tachiyomi.core.prefs.PreferenceStore
import java.io.File
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {


    @Provides
    @Singleton
    fun provideBookDatabase(
        @ApplicationContext appContext: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .addMigrations(
                MIGRATION_8_9,
                MIGRATION_11_12
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesBookDao(db: AppDatabase): LibraryBookDao {
        return db.libraryBookDao
    }


    @Provides
    @Singleton
    fun provideChapterDao(db: AppDatabase): LibraryChapterDao {
        return db.libraryChapterDao
    }

    @Provides
    @Singleton
    fun provideRemoteKeyDao(db: AppDatabase): RemoteKeysDao {
        return db.remoteKeysDao
    }


    @Singleton
    @Provides
    fun provideDownloadRepository(
        downloadDao: DownloadDao,
    ): DownloadRepository {
        return DownloadRepositoryImpl(downloadDao)
    }

    @Singleton
    @Provides
    fun provideDownloadDao(
        database: AppDatabase,
    ): DownloadDao {
        return database.downloadDao
    }


    @Provides
    @Singleton
    fun provideLibraryCovers(
        @ApplicationContext context: Context,
    ): LibraryCovers {
        return LibraryCovers(
            FileSystem.SYSTEM,
            File(context.filesDir, "library_covers").toOkioPath()
        )
    }


    @Provides
    @Singleton
    fun providePreferencesStore(@ApplicationContext context: Context): PreferenceStore {
        return AndroidPreferenceStore(context = context, "ui")
    }

}
