package hr.ferit.matijasokol.selectiontrackerexample.db

import androidx.lifecycle.LiveData
import androidx.room.*
import hr.ferit.matijasokol.selectiontrackerexample.models.Note
import hr.ferit.matijasokol.selectiontrackerexample.other.Constants.NOTES_TABLE_NAME

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note: List<Note>): List<Long>

    @Query("SELECT * FROM $NOTES_TABLE_NAME")
    fun getAll(): LiveData<List<Note>>

    @Query("SELECT * FROM $NOTES_TABLE_NAME WHERE title LIKE '%' || :query || '%'")
    suspend fun getSearchedNotes(query: String): List<Note>

    @Query("DELETE FROM $NOTES_TABLE_NAME WHERE id IN (:noteIdList)")
    suspend fun deleteNotes(noteIdList: List<Long>): Int
}