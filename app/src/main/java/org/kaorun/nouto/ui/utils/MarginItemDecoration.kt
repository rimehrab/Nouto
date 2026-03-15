package org.kaorun.nouto.ui.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class MarginItemDecoration(
    private val outerSpaceSize: Int,
    private val innerSpaceSize: Int,
    private val spanCount: Int,
    private val isTopMargin: Boolean = true
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val layoutParams = view.layoutParams as? StaggeredGridLayoutManager.LayoutParams
        val column = layoutParams?.spanIndex ?: (position % spanCount)
        val itemCount = parent.adapter?.itemCount ?: 0

        outRect.top = if (position < spanCount) {
            if (isTopMargin) outerSpaceSize else 0
        } else innerSpaceSize
        outRect.left = if (column == 0) outerSpaceSize else innerSpaceSize / 2
        outRect.right = if (column == spanCount - 1) outerSpaceSize else innerSpaceSize / 2
        outRect.bottom = if (position >= itemCount - spanCount) outerSpaceSize else 0
    }
}