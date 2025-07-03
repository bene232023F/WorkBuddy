package edu.vt.mobiledev.workbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.vt.mobiledev.workbuddy.R
import edu.vt.mobiledev.workbuddy.data.Task

// Adapter for the RecyclerView in TasksFragment. Binds each Task to an item_task.xml row.
class TasksAdapter(
    private val onToggleCompleted: (Task) -> Unit
) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view, onToggleCompleted)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TaskViewHolder(
        itemView: View,
        private val onToggle: (Task) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.cb_complete)
        private val titleView: TextView = itemView.findViewById(R.id.tv_task_title)

        fun bind(task: Task) {
            titleView.text = task.title
            checkBox.isChecked = task.isCompleted

            // When user toggles the box, inform the Fragment/VM
            checkBox.setOnClickListener {
                onToggle(task)
            }
        }
    }
}

// DiffUtil callback so ListAdapter can animate changes efficiently
private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(old: Task, new: Task) = old.id == new.id
    override fun areContentsTheSame(old: Task, new: Task) = old == new
}