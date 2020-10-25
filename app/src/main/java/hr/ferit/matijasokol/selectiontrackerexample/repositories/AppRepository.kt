package hr.ferit.matijasokol.selectiontrackerexample.repositories

import hr.ferit.matijasokol.selectiontrackerexample.models.Note
import hr.ferit.matijasokol.selectiontrackerexample.db.NoteDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val noteDao: NoteDao
) {

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun insertNotes(notes: List<Note>) = noteDao.insertNotes(notes)

    fun getAllNotes() = noteDao.getAll()

    suspend fun getSearchedNotes(query: String) = noteDao.getSearchedNotes(query)

    suspend fun deleteNotes(ids: List<Long>) = noteDao.deleteNotes(ids)
}