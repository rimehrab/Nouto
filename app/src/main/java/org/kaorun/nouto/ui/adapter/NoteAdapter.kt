package org.kaorun.nouto.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.listitem.ListItemViewHolder
import org.kaorun.nouto.data.Note
import org.kaorun.nouto.databinding.ItemNoteBinding

class NoteAdapter(
    private val onItemClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback()) {
    class NoteViewHolder(
        private val binding: ItemNoteBinding,
        private val onItemClick: (Note) -> Unit,
        private val onDeleteClick: (Note) -> Unit
    ) : ListItemViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.noteTitle.text = HtmlCompat.fromHtml(
                (if (note.title.isNullOrBlank()) note.content else note.title)!!,
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
            binding.cardView.setOnClickListener { onItemClick(note) }
            binding.buttonDelete.setOnClickListener { onDeleteClick(note) }
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
        return NoteViewHolder(binding, onItemClick, onDeleteClick)
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
        holder.bind(position, itemCount)
    }
}