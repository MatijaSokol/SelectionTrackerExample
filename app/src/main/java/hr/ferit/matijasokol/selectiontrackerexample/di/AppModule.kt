package hr.ferit.matijasokol.selectiontrackerexample.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import hr.ferit.matijasokol.selectiontrackerexample.db.AppDatabase
import hr.ferit.matijasokol.selectiontrackerexample.other.Constants.APP_DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, AppDatabase::class.java, APP_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideNoteDao(appDatabase: AppDatabase) = appDatabase.getNoteDao()
}