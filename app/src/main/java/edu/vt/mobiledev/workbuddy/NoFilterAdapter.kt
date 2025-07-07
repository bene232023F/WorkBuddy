package edu.vt.mobiledev.workbuddy

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes

// A simple ArrayAdapter that disables filtering so the dropdown always shows all items.
class NoFilterAdapter(
    ctx: Context,
    @LayoutRes itemLayout: Int,
    private val data: Array<String>
) : ArrayAdapter<String>(ctx, itemLayout, data) {

    // Override getFilter() to supply our own no-op Filter
    override fun getFilter(): Filter = object : Filter() {

        // Called on a background thread. Ignores any constraint and returns the full data set.
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return FilterResults().apply {
                values = data      // provide all items
                count  = data.size // with full count
            }
        }

        // Called on the UI thread. Since the data never actually changes, just notify.
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }
    }
}
