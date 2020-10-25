package hr.ferit.matijasokol.selectiontrackerexample.ui.notes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import hr.ferit.matijasokol.selectiontrackerexample.models.Note
import hr.ferit.matijasokol.selectiontrackerexample.repositories.AppRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class NotesViewModel @ViewModelInject constructor(
    private val repository: AppRepository
) : ViewModel() {

    val note = MutableLiveData<Note>()

    private val _notes = MediatorLiveData<List<Note>>()
    val notes: LiveData<List<Note>>
        get() = _notes

    private val allNotes = repository.getAllNotes()
    private val searchedNotes = MutableLiveData<List<Note>>()

    private val _selection = MutableLiveData<Int>()
    val selection: LiveData<Int>
        get() = _selection

    val selectionShown = MutableLiveData<Boolean>()

    private val _deleteFinished = MutableLiveData<Unit>()
    val deleteFinished: LiveData<Unit>
        get() = _deleteFinished

    init {
        _notes.addSource(allNotes) { _notes.value = it }
        _notes.addSource(searchedNotes) { _notes.value = it }
    }

    fun insertNote(note: Note) = viewModelScope.launch(IO) {
        repository.insertNote(note)
    }

    fun insertNotes(notes: List<Note>) = viewModelScope.launch(IO) {
        repository.insertNotes(notes)
    }

    fun searchNotes(query: String) = viewModelScope.launch(IO) {
        val notes = repository.getSearchedNotes(query)
        searchedNotes.postValue(notes)
    }

    fun updateSelection(count: Int) {
        _selection.value = count
    }

    fun deleteNotes(ids: List<Long>) = viewModelScope.launch(IO) {
        val rows = repository.deleteNotes(ids)
        if (rows > 0) _deleteFinished.postValue(Unit)
    }
}