package edu.vt.mobiledev.workbuddy.ui.stats

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import edu.vt.mobiledev.workbuddy.data.TaskRepository
import java.util.Calendar

// ViewModel for StatsFragment (productivity statistics screen).
class StatsViewModel(
    repository: TaskRepository
) : ViewModel() {
    // Number of Pomodoros completed today
    val dailyPomodoros = repository
        .countPomodorosSince(startOfDayMillis())
        .asLiveData()

    // Number of Pomodoros completed in the last 7 days
    val weeklyPomodoros = repository
        .countPomodorosSince(startOfWeekMillis())
        .asLiveData()

    // Helper to get timestamp for start of current day
    private fun startOfDayMillis(): Long {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE,      0)
            set(Calendar.SECOND,      0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    // Helper to get timestamp for start of current week
    private fun startOfWeekMillis(): Long {
        val cal = Calendar.getInstance().apply {
            // first, roll back to midnight today
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE,      0)
            set(Calendar.SECOND,      0)
            set(Calendar.MILLISECOND, 0)
            // then roll to the first day of week
            set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        }
        return cal.timeInMillis
    }
}

// Factory to create a StatsViewModel with its required TaskRepository.
class StatsViewModelFactory(private val ctx: Context)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return StatsViewModel(TaskRepository.getInstance(ctx)) as T
    }
}