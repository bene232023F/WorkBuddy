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

class HomeViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    // Live-updating list of all tasks
    private val allTasks = repository.getAllTasks().asLiveData()

    // Pick the first unfinished task, or null if none
    val nextTask: LiveData<Task?> = allTasks.map { list ->
        list.firstOrNull { !it.isCompleted }
    }

    // Expose just its title (or a dash if there’s no next task)
    val nextTaskName: LiveData<String> = nextTask.map { task ->
        task?.title ?: "—"
    }

    // Live data holding the length (in minutes) for each interval
    private val _pomodoroLengthMin = MutableLiveData(25L)

    // Total milliseconds remaining
    private val _msRemaining = MutableLiveData(_pomodoroLengthMin.value!! * 60_000L)

    // Formatted timer text
    val timerText: LiveData<String> = _msRemaining.map { ms ->
        val m = TimeUnit.MILLISECONDS.toMinutes(ms)
        val s = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
        String.format("%02d:%02d", m, s)
    }

    // The task we’re currently working on (may be null)
    private val _currentTask = MutableLiveData<Task?>(null)
    private val _currentTaskName = MutableLiveData<String>("—")
    val currentTaskName: LiveData<String> = _currentTaskName

    private var timer: CountDownTimer? = null
    private var running = false

    // Whenever the length changes, reset the countdown
    init {
        _pomodoroLengthMin.observeForever { mins ->
            _msRemaining.value = mins * 60_000L
        }
    }

    // Helper to update the length
    fun setPomodoroLength(minutes: Long) {
        _pomodoroLengthMin.value = minutes.coerceAtLeast(1L)
    }

    // Helper to turn minutes → milliseconds
    private fun lengthMs(mins: Long) = mins * 60_000L

    // Begin counting down
    fun startTimerForTask(task: Task) {
        if (running) return

        // Store the picked Task
        _currentTask.value = task
        // Pull out its title for the label
        _currentTaskName.value = task.title
        val startFrom = _msRemaining.value
            ?: lengthMs(_pomodoroLengthMin.value ?: 25L)

        timer = object : CountDownTimer(startFrom, 1_000L) {
            override fun onTick(millisUntilFinished: Long) {
                _msRemaining.value = millisUntilFinished
            }

            // Track completed pomodoros
            override fun onFinish() {
                _msRemaining.value = 0L
                running = false

                // Increment the task’s counter
                viewModelScope.launch(Dispatchers.IO) {
                    repository.updateTask(task.copy(pomodoroCount = task.pomodoroCount + 1))
                    // Write a new session record
                    repository.recordSession(task.id)
                }
            }
        }.start()

        running = true
    }

    // Pause timer
    fun pauseTimer() {
        timer?.cancel()
        running = false
    }

    // Reset the timer and current task
    fun resetTimer() {
        timer?.cancel()
        running = false
        // reset back to the full interval
        _msRemaining.value = lengthMs(_pomodoroLengthMin.value ?: 25L)
        _currentTask.value = null
    }

    // To block Start if already running
    fun isRunning() = running
}

class HomeViewModelFactory(private val ctx: Context)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(
            TaskRepository.getInstance(ctx)
        ) as T
    }
}