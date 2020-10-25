package hr.ferit.matijasokol.selectiontrackerexample.db

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.ferit.matijasokol.selectiontrackerexample.models.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao
}