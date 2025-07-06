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

class TasksFragment : Fragment(R.layout.fragment_tasks) {
    private val tasksVM: TasksViewModel by viewModels {
        TasksViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<RecyclerView>(R.id.rv_tasks)
        val empty = view.findViewById<TextView>(R.id.tv_empty)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add_task)

        val adapter = TasksAdapter { task ->
            tasksVM.toggleCompleted(task)
        }
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        // Observe tasks list
        tasksVM.tasks.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            if (list.isEmpty()) {
                rv.visibility = View.GONE
                empty.visibility = View.VISIBLE
            } else {
                rv.visibility = View.VISIBLE
                empty.visibility = View.GONE
            }
        }

        fab.setOnClickListener {
            findNavController().navigate(R.id.addTaskDialog)
        }

        // Create the swipe callback
        val swipeToDelete = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Guard against invalid positions
                val pos = viewHolder.adapterPosition
                if (pos == RecyclerView.NO_POSITION) return

                val task = adapter.currentList[pos]
                tasksVM.deleteTask(task)
            }
        }

        // Attach it to the RecyclerView
        ItemTouchHelper(swipeToDelete).attachToRecyclerView(rv)

        // Observe tasks and submitList as before
        tasksVM.tasks.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }
}