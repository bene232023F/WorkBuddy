package edu.vt.mobiledev.workbuddy.ui.tasks

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

        var adapter = TasksAdapter { task ->
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
            // show add-task dialog
        }
    }
}