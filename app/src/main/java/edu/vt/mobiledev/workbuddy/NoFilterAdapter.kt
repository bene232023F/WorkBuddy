package edu.vt.mobiledev.workbuddy

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes

class NoFilterAdapter(
    ctx: Context,
    @LayoutRes itemLayout: Int,
    private val data: Array<String>
) : ArrayAdapter<String>(ctx, itemLayout, data) {

    // Always return our custom Filter instead of the default one
    override fun getFilter(): Filter = object : Filter() {

        // Runs on a background thread; ignore the constraint and return the full array
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return FilterResults().apply {
                values = data      // supply all items
                count  = data.size // report full size
            }
        }

        // Runs on the UI thread; notify the adapter that data didn’t actually change
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }
    }
}

