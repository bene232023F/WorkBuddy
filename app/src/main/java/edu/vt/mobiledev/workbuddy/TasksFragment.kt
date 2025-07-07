package edu.vt.mobiledev.workbuddy.ui.tasks

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.vt.mobiledev.workbuddy.R
import edu.vt.mobiledev.workbuddy.TasksAdapter

/**
 * Fragment that displays the list of tasks.
 * Shows a RecyclerView of tasks, or an empty-state TextView when there are none.
 * Provides a FAB to add a new task via a BottomSheetDialog.
 * Supports swiping tasks left or right to delete them.
 */
class TasksFragment : Fragment(R.layout.fragment_tasks) {

    // Obtain a ViewModel scoped to this Fragment using the factory
    private val tasksVM: TasksViewModel by viewModels {
        TasksViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find views in the inflated layout
        val rv    = view.findViewById<RecyclerView>(R.id.rv_tasks)
        val empty = view.findViewById<TextView>(R.id.tv_empty)
        val fab   = view.findViewById<FloatingActionButton>(R.id.fab_add_task)

        // Create the RecyclerView adapter, passing in a callback to toggle completion
        val adapter = TasksAdapter { task ->
            tasksVM.toggleCompleted(task)
        }

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        // Observe the LiveData list of tasks
        tasksVM.tasks.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            // Show empty state if no tasks, otherwise show the list
            if (list.isEmpty()) {
                rv.visibility    = View.GONE
                empty.visibility = View.VISIBLE
            } else {
                rv.visibility    = View.VISIBLE
                empty.visibility = View.GONE
            }
        }

        // FAB click navigates to the dialog for adding a new task
        fab.setOnClickListener {
            findNavController().navigate(R.id.addTaskDialog)
        }

        // Set up swipe-to-delete on the RecyclerView
        val swipeToDelete = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            // No support for drag & drop
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            // Called when a swipe is detected
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                // Guard against invalid adapter positions
                if (pos != RecyclerView.NO_POSITION) {
                    val task = adapter.currentList[pos]
                    tasksVM.deleteTask(task)
                }
            }
        }
        // Attach the swipe callback to the RecyclerView
        ItemTouchHelper(swipeToDelete).attachToRecyclerView(rv)
    }
}