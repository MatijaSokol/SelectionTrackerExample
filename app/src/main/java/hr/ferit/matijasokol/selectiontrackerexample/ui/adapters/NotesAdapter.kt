package hr.ferit.matijasokol.selectiontrackerexample.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import hr.ferit.matijasokol.selectiontrackerexample.R
import hr.ferit.matijasokol.selectiontrackerexample.models.Note
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.note_item.view.*

class NotesAdapter(
    private val onItemClicked: (Note) -> Unit
) : ListAdapter<Note, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    var selectionTracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {

            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NoteViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NoteViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    override fun getItemId(position: Int): Long = getItem(position).id

    inner class NoteViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        init {
            itemView.setOnClickListener {
                if (selectionTracker?.hasSelection() == false) {
                    onItemClicked.invoke(getItem(adapterPosition))
                }
            }
        }

        fun bind(item: Note) {
            with(itemView) {
                tvTitle.text = item.title
                tvDescription.text = item.description
            }

            val backgroundResId = if (selectionTracker?.isSelected(getItemId(adapterPosition)) == true) {
                R.drawable.note_item_selected_background
            } else {
                R.drawable.note_item_background
            }

            itemView.background = ContextCompat.getDrawable(itemView.context, backgroundResId)
        }

        fun getNoteDetails(): ItemDetailsLookup.ItemDetails<Long> = object : ItemDetailsLookup.ItemDetails<Long>() {

            override fun getPosition(): Int = adapterPosition

            override fun getSelectionKey(): Long? = itemId
        }
    }

    class NoteKeyProvider(
        private val adapter: NotesAdapter,
        private val recycler: RecyclerView
    ) : ItemKeyProvider<Long>(SCOPE_MAPPED) {

        override fun getKey(position: Int): Long? {
            return adapter.getItemId(position)
        }

        override fun getPosition(@NonNull key: Long): Int {
            val viewHolder = recycler.findViewHolderForItemId(key)
            return viewHolder?.layoutPosition ?: RecyclerView.NO_POSITION
        }
    }

    class NoteItemLookup(private val recycler: RecyclerView) : ItemDetailsLookup<Long>() {

        override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
            val view = recycler.findChildViewUnder(event.x, event.y)
            if (view != null) {
                return (recycler.getChildViewHolder(view) as NoteViewHolder).getNoteDetails()
            }

            return null
        }
    }
}