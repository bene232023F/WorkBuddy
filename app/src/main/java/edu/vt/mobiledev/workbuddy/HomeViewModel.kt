package edu.vt.mobiledev.workbuddy.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.vt.mobiledev.workbuddy.data.Task
import edu.vt.mobiledev.workbuddy.data.TaskRepository

// ViewModel for HomeFragment (Pomodoro timer screen).
class HomeViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    // Timer text (e.g. "25:00")
    private val _timerText = MutableLiveData("25:00")
    val timerText: LiveData<String> = _timerText

    // Currently selected Task for Pomodoro
    private val _currentTask = MutableLiveData<Task?>(null)
    val currentTask: LiveData<Task?> = _currentTask

    // TODO: Implement timer start logic
    fun startTimerForTask(task: Task) {
        _currentTask.value = task
        // TODO: kick off countdown, update _timerText
    }

    // TODO: Pause the countdown
    fun pauseTimer() {
        // TODO
    }

    // TODO: Reset timer to default durations
    fun resetTimer() {
        _timerText.value = "25:00"
        _currentTask.value = null
    }
}

class HomeViewModelFactory(private val ctx: Context)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(
            TaskRepository.getInstance(ctx)
        ) as T
    }
}