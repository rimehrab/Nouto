package org.kaorun.nouto.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.listitem.ListItemViewHolder
import org.kaorun.nouto.databinding.ItemSearchBinding

class SearchAdapter(
    private val onItemClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : ListAdapter<String, SearchAdapter.SearchViewHolder>(SearchDiffCallback()) {

    inner class SearchViewHolder(
        private val binding: ItemSearchBinding,
    ) : ListItemViewHolder(binding.root) {
        fun bind(query: String) {
            binding.listItemText.text = query
            binding.root.setOnClickListener { onItemClick(query) }
            binding.buttonDelete.setOnClickListener { onDeleteClick(query) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(position, itemCount)
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<String>?) {
        super.submitList(list) {
            notifyItemRangeChanged(0, itemCount)
        }
    }
}