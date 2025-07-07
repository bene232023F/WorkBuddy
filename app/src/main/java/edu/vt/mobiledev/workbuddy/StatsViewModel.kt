package edu.vt.mobiledev.workbuddy.ui.stats

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import edu.vt.mobiledev.workbuddy.data.TaskRepository
import java.util.Calendar

/**
 * ViewModel for the StatsFragment, responsible for exposing productivity metrics:
 *  dailyPomodoros: Pomodoro sessions completed since today's start.
 *  weeklyPomodoros: Pomodoro sessions completed since the start of the current week.
 *
 * @param repository Provides access to task and session data.
 */
class StatsViewModel(
    repository: TaskRepository
) : ViewModel() {

    /** LiveData emitting the count of Pomodoro sessions completed today. */
    val dailyPomodoros = repository
        .countPomodorosSince(startOfDayMillis())
        .asLiveData()

    /** LiveData emitting the count of Pomodoro sessions completed in the last 7 days. */
    val weeklyPomodoros = repository
        .countPomodorosSince(startOfWeekMillis())
        .asLiveData()

    /**
     * Calculate the timestamp (ms) for today's midnight.
     * Used to filter sessions occurring from the start of today.
     */
    private fun startOfDayMillis(): Long {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE,      0)
            set(Calendar.SECOND,      0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    /**
     * Calculate the timestamp (ms) for the first day of the current week at midnight.
     * Used to filter sessions occurring from the start of the week.
     */
    private fun startOfWeekMillis(): Long {
        val cal = Calendar.getInstance().apply {
            // Roll back to midnight today first
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE,      0)
            set(Calendar.SECOND,      0)
            set(Calendar.MILLISECOND, 0)
            // Then set to the first day of the week
            set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        }
        return cal.timeInMillis
    }
}

/**
 * Factory class to create StatsViewModel instances, injecting the required TaskRepository.
 *
 * @param ctx Context needed to obtain the singleton TaskRepository.
 */
class StatsViewModelFactory(private val ctx: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        // Instantiate the StatsViewModel with the app-wide TaskRepository
        return StatsViewModel(TaskRepository.getInstance(ctx)) as T
    }
}
