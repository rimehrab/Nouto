package org.kaorun.nouto.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.listitem.ListItemLayout
import org.kaorun.nouto.R
import org.kaorun.nouto.ui.model.LayoutMode
import org.kaorun.nouto.ui.utils.MarginItemDecoration

abstract class SettingsBaseFragment : PreferenceFragmentCompat() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = listView.adapter ?: return
        listView.removeItemDecorationAt(0)
        listView.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.recycler_view_outer_margin
                ),
                resources.getDimensionPixelSize(
                    R.dimen.recycler_view_segmented_list_margin
                ),
                LayoutMode.LINEAR.spanCount,
                false
            )
        )

        listView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun getItemCount() = adapter.itemCount

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                adapter.createViewHolder(parent, viewType)

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                adapter.bindViewHolder(holder, position)
                (holder.itemView as? ListItemLayout)?.updateAppearance(position, itemCount)
            }
        }
    }
}