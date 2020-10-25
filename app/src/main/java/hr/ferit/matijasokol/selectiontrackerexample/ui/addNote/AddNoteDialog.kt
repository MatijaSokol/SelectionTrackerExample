package hr.ferit.matijasokol.selectiontrackerexample.ui.addNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import hr.ferit.matijasokol.selectiontrackerexample.R
import hr.ferit.matijasokol.selectiontrackerexample.models.Note
import hr.ferit.matijasokol.selectiontrackerexample.ui.notes.NotesActivity
import kotlinx.android.synthetic.main.dialog_add_note.*

class AddNoteDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    private fun setListeners() {
        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val note = Note(title, description)

                val noteActivity = requireActivity() as NotesActivity
                val viewModel = noteActivity.viewModel
                viewModel.note.value = note

                dismiss()
            } else {
                Toast.makeText(requireContext(), getString(R.string.invalid_input_message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance() = AddNoteDialog()
    }
}