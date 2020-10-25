package hr.ferit.matijasokol.selectiontrackerexample.ui.notes

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import hr.ferit.matijasokol.selectiontrackerexample.R
import hr.ferit.matijasokol.selectiontrackerexample.models.Note
import hr.ferit.matijasokol.selectiontrackerexample.other.displayToast
import hr.ferit.matijasokol.selectiontrackerexample.other.gone
import hr.ferit.matijasokol.selectiontrackerexample.other.visible
import hr.ferit.matijasokol.selectiontrackerexample.ui.addNote.AddNoteDialog
import hr.ferit.matijasokol.selectiontrackerexample.ui.adapters.NotesAdapter
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class NotesActivity : AppCompatActivity() {

    val viewModel by viewModels<NotesViewModel>()
    private val notesAdapter by lazy { NotesAdapter { onItemClicked(it) } }
    private var menu: Menu? = null

    private fun onItemClicked(note: Note) {
        displayToast(getString(R.string.click_message, note.title))
    }

    private var tracker: SelectionTracker<Long>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        setRecycler()
        setSelectionTracker()
        setObservers()
        setListeners()

        tracker?.onRestoreInstanceState(savedInstanceState)
    }

    private fun setListeners() {
        fab.setOnClickListener {
            AddNoteDialog.newInstance().show(supportFragmentManager, "")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker?.onSaveInstanceState(outState)
    }

    private fun setRecycler() {
        recycler.apply {
            layoutManager = LinearLayoutManager(this@NotesActivity)
            adapter = notesAdapter
            setHasFixedSize(true)
        }
    }

    private fun setSelectionTracker() {
        tracker = SelectionTracker.Builder(
                "selection-1",
                recycler,
            NotesAdapter.NoteKeyProvider(notesAdapter, recycler),
            NotesAdapter.NoteItemLookup(recycler),
                StorageStrategy.createLongStorage())
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()

        tracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                tracker?.selection?.size()?.let {
                    viewModel.updateSelection(it)
                }
            }
        })

        notesAdapter.selectionTracker = tracker
    }

    private fun setObservers() {
        viewModel.notes.observe(this, { notes ->
            if (notes.isEmpty()) {
                notesAdapter.submitList(emptyList())
                tvNoNotes.visible()
            } else {
                tvNoNotes.gone()
                notesAdapter.submitList(notes)
            }
        })

        viewModel.selection.observe(this, { count ->
            handleSelection(count)
            handleMenuItemsVisibility()
        })

        viewModel.note.observe(this, { note ->
            viewModel.insertNote(note)
        })

        viewModel.deleteFinished.observe(this, {
            tracker?.clearSelection()
        })
    }

    private fun handleSelection(count: Int) {
        viewModel.selectionShown.value = count > 0
        supportActionBar?.apply {
            if (count > 0) {
                title = getString(R.string.notes_selected, count)
                setBackgroundDrawable(
                    ColorDrawable(ContextCompat.getColor(this@NotesActivity,
                        R.color.colorPrimaryDark
                    ))
                )
            } else {
                title = getString(R.string.app_name)
                setBackgroundDrawable(
                    ColorDrawable(ContextCompat.getColor(this@NotesActivity, R.color.colorPrimary))
                )
            }
        }
    }

    private fun handleMenuItemsVisibility() {
        val clearSelectionVisibility = viewModel.selectionShown.value ?: false
        menu?.findItem(R.id.clearSelection)?.isVisible = clearSelectionVisibility
        menu?.findItem(R.id.deleteItems)?.isVisible = clearSelectionVisibility
        menu?.findItem(R.id.searchButton)?.isVisible = !clearSelectionVisibility
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        this.menu = menu
        handleMenuItemsVisibility()
        menu?.let { setupSearchButton(it) }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupSearchButton(menu: Menu) {
        val searchItem = menu.findItem(R.id.searchButton)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchNotes(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchNotes(it) }
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clearSelection -> {
                tracker?.clearSelection()
                return true
            }
            R.id.deleteItems -> {
                deleteItems()
                return true
            }
         }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteItems() {
        val ids = tracker?.selection?.toList()
        ids?.let { viewModel.deleteNotes(it) }
    }

    override fun onBackPressed() {
        if (tracker?.hasSelection() == true) {
            tracker?.clearSelection()
        } else {
            super.onBackPressed()
        }
    }
}