package org.kaorun.nouto.ui.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.listitem.ListItemViewHolder
import org.kaorun.nouto.data.Note
import org.kaorun.nouto.databinding.ItemNoteBinding
import com.google.android.material.listitem.ListItemCardView.SwipeCallback
import com.google.android.material.listitem.RevealableListItem
import com.google.android.material.listitem.SwipeableListItem
import com.google.android.material.listitem.SwipeableListItem.STATE_SWIPE_PRIMARY_ACTION

class NoteAdapter(
    private val onItemClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback()) {
    inner class NoteViewHolder(
        private val binding: ItemNoteBinding
    ) : ListItemViewHolder(binding.root) {
        private lateinit var note: Note

        init {
            binding.cardView.addSwipeCallback(object : SwipeCallback() {
                override fun onSwipe(p0: Int) {}
                override fun <T> onSwipeStateChanged(
                    newState: Int,
                    activeRevealableListItem: T,
                    gravity: Int
                ) where T : View, T : RevealableListItem {
                    if (newState == STATE_SWIPE_PRIMARY_ACTION &&
                        bindingAdapterPosition != RecyclerView.NO_POSITION) onDeleteClick(note)
                }
            })
            binding.cardView.setOnClickListener { onItemClick(note) }
            binding.buttonDelete.setOnClickListener { onDeleteClick(note) }
        }

        fun bind(currentNote: Note) {
            note = currentNote
            binding.noteTitle.text = HtmlCompat.fromHtml(
                (if (note.title.isNullOrBlank()) note.content else note.title)!!,
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        }

        fun closeSwipe() {
            val swipeable = binding.root
            binding.cardView.animate().cancel()
            swipeable.setSwipeState(SwipeableListItem.STATE_CLOSED, Gravity.START)
            swipeable.setSwipeState(SwipeableListItem.STATE_CLOSED, Gravity.END)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
        holder.bind(position, itemCount)
    }

    override fun onViewRecycled(holder: NoteViewHolder) {
        super.onViewRecycled(holder)
        holder.closeSwipe()
    }
}