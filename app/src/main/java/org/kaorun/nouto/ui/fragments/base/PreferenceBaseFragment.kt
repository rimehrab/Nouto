package org.kaorun.nouto.ui.fragments.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.listitem.ListItemLayout
import org.kaorun.nouto.R
import org.kaorun.nouto.ui.model.LayoutMode
import org.kaorun.nouto.ui.utils.MarginItemDecoration

abstract class PreferenceBaseFragment : PreferenceFragmentCompat() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = listView.adapter ?: return
        listView.removeItemDecorationAt(0)
        listView.addItemDecoration(MarginItemDecoration(
            resources.getDimensionPixelSize(R.dimen.recycler_view_outer_margin),
            resources.getDimensionPixelSize(R.dimen.recycler_view_segmented_list_margin),
            LayoutMode.LINEAR.spanCount,
            false
        ))
        listView.adapter = SegmentedPreferenceAdapter(adapter)
    }

    inner class SegmentedPreferenceAdapter(
        private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        init {
            adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                @SuppressLint("NotifyDataSetChanged")
                override fun onChanged() = notifyDataSetChanged()
                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) = notifyItemRangeChanged(positionStart, itemCount)
                override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) = notifyItemRangeChanged(positionStart, itemCount, payload)
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = notifyItemRangeInserted(positionStart, itemCount)
                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = notifyItemRangeRemoved(positionStart, itemCount)
                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) = notifyItemMoved(fromPosition, toPosition)
            })
        }

        override fun getItemCount() = adapter.itemCount
        override fun getItemViewType(position: Int) = adapter.getItemViewType(position)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            adapter.createViewHolder(parent, viewType)
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            adapter.bindViewHolder(holder, position)
            val view = holder.itemView as? ListItemLayout ?: return
            view.updateAppearance(preferencePosition(position), preferenceItemCount())
        }

        private fun isPreference(position: Int) = adapter.createViewHolder(
            listView,
            adapter.getItemViewType(position)
        ).itemView is ListItemLayout

        private fun preferenceItemCount() = (0 until adapter.itemCount).count {
            isPreference(it)
        }
        private fun preferencePosition(position: Int) = (0 until position).count {
            isPreference(it)
        }
    }
}