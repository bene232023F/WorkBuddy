package edu.vt.mobiledev.workbuddy.ui.tasks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import edu.vt.mobiledev.workbuddy.data.Task
import edu.vt.mobiledev.workbuddy.data.TaskRepository
import kotlinx.coroutines.launch

// ViewModel for TasksFragment (task management screen).
class TasksViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    // LiveData list of all tasks
    val tasks = repository.getAllTasks().asLiveData()

    // Toggle completion status
    fun toggleCompleted(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    // Delete a task from the repository
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}

// Factory to create a TasksViewModel and supply it with the required TaskRepository
class TasksViewModelFactory(private val ctx: Context)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TasksViewModel(
            // Obtain the singleton repository instance using the application context
            TaskRepository.getInstance(ctx)
        ) as T
    }
}
