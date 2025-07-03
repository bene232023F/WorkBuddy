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

    // Add a new task
    fun addTask(title: String) {
        viewModelScope.launch {
            repository.insertTask(Task(title = title))
        }
    }

    // Toggle completion status
    fun toggleCompleted(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    // Increment pomodoro count for a task
    fun incrementPomodoro(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(pomodoroCount = task.pomodoroCount + 1))
        }
    }
}

class TasksViewModelFactory(private val ctx: Context)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TasksViewModel(
            TaskRepository.getInstance(ctx)
        ) as T
    }
}
