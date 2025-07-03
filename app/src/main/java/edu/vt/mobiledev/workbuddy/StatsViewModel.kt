package edu.vt.mobiledev.workbuddy.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import edu.vt.mobiledev.workbuddy.data.TaskRepository

// ViewModel for StatsFragment (productivity statistics screen).
class StatsViewModel(
    private val repository: TaskRepository = TaskRepository.getInstance()
) : ViewModel() {
    // Number of Pomodoros completed today
    val dailyPomodoros = repository.countPomodorosSince(startOfDayMillis()).asLiveData()

    // Number of Pomodoros completed in the last 7 days
    val weeklyPomodoros = repository.countPomodorosSince(startOfWeekMillis()).asLiveData()

    // Helper to get timestamp for start of current day
    private fun startOfDayMillis(): Long {
        // TODO: calculate midnight today in millis
        return System.currentTimeMillis()
    }

    // Helper to get timestamp for start of current week
    private fun startOfWeekMillis(): Long {
        // TODO: calculate start of week in millis
        return System.currentTimeMillis()
    }
}