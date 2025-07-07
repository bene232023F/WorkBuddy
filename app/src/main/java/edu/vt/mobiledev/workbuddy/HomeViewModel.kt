package edu.vt.mobiledev.workbuddy.ui.home

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import edu.vt.mobiledev.workbuddy.data.Task
import edu.vt.mobiledev.workbuddy.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * ViewModel for the Pomodoro timer screen.
 *
 * Exposes the next unfinished Task and its title.
 * Manages the countdown timer state (length, remaining time, formatted text).
 * Records completed sessions back into the repository.
 */
class HomeViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    // LiveData list of all tasks from the repository
    private val allTasks: LiveData<List<Task>> =
        repository.getAllTasks().asLiveData()

    // The first unfinished task, or null if none exist
    val nextTask: LiveData<Task?> = allTasks.map { list ->
        list.firstOrNull { !it.isCompleted }
    }

    // Only the title of the next task, or “—” if no task is available
    val nextTaskName: LiveData<String> = nextTask.map { task ->
        task?.title ?: "—"
    }

    // User-selected Pomodoro length in minutes (default = 25)
    private val _pomodoroLengthMin = MutableLiveData(25L)
    val pomodoroLengthMin: LiveData<Long> = _pomodoroLengthMin

    // Milliseconds remaining in the current countdown
    private val _msRemaining = MutableLiveData(_pomodoroLengthMin.value!! * 60_000L)
    val msRemaining: LiveData<Long> = _msRemaining

    // Formatted timer text "MM:SS" for display
    val timerText: LiveData<String> = _msRemaining.map { ms ->
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
        String.format("%02d:%02d", minutes, seconds)
    }

    // The task currently being timed (nullable)
    private val _currentTask = MutableLiveData<Task?>(null)

    // Title of the current task for UI
    private val _currentTaskName = MutableLiveData<String>("—")
    val currentTaskName: LiveData<String> = _currentTaskName

    private var timer: CountDownTimer? = null
    private var running = false

    init {
        // Reset the countdown whenever the Pomodoro length changes
        _pomodoroLengthMin.observeForever { mins ->
            _msRemaining.value = mins * 60_000L
        }
    }

    /**
     * Update the Pomodoro interval length (in minutes).
     * Will enforce a minimum of 1 minute.
     */
    fun setPomodoroLength(minutes: Long) {
        _pomodoroLengthMin.value = minutes.coerceAtLeast(1L)
    }

    // Convert minutes to milliseconds for internal calculations
    private fun lengthMs(mins: Long) = mins * 60_000L

    /**
     * Start the countdown for a given Task.
     * If a timer is already running, this call is ignored.
     * When the timer finishes:
     *  Remaining time is set to zero and running flag cleared.
     *  The task’s pomodoroCount is incremented.
     *  A new PomodoroSession record is written to the repository.
     */
    fun startTimerForTask(task: Task) {
        if (running) return

        _currentTask.value = task
        _currentTaskName.value = task.title

        val startFrom = _msRemaining.value
            ?: lengthMs(_pomodoroLengthMin.value ?: 25L)

        timer = object : CountDownTimer(startFrom, 1_000L) {
            override fun onTick(millisUntilFinished: Long) {
                _msRemaining.value = millisUntilFinished
            }

            override fun onFinish() {
                _msRemaining.value = 0L
                running = false

                // Record completion on IO thread
                viewModelScope.launch(Dispatchers.IO) {
                    repository.updateTask(task.copy(pomodoroCount = task.pomodoroCount + 1))
                    repository.recordSession(task.id)
                }
            }
        }.start()

        running = true
    }

    /** Pause the current countdown (if running). */
    fun pauseTimer() {
        timer?.cancel()
        running = false
    }

    /**
     * Reset the timer back to the full interval length
     * and clear the current task selection.
     */
    fun resetTimer() {
        timer?.cancel()
        running = false
        _msRemaining.value = lengthMs(_pomodoroLengthMin.value ?: 25L)
        _currentTask.value = null
    }

    /** Returns whether a countdown is currently in progress. */
    fun isRunning(): Boolean = running
}

/**
 * Factory that provides HomeViewModel instances scoped to
 * this Activity/Fragment, supplying the shared TaskRepository.
 */
class HomeViewModelFactory(private val ctx: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(
            TaskRepository.getInstance(ctx)
        ) as T
    }
}