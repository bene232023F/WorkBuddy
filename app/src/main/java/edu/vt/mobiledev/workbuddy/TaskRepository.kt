package edu.vt.mobiledev.workbuddy.data

import android.content.Context
import edu.vt.mobiledev.workbuddy.PomodoroSession
import kotlinx.coroutines.flow.Flow

class TaskRepository private constructor(
    private val taskDao: TaskDao,
    private val sessionDao: PomodoroSessionDao
) {

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insertTask(task: Task) = taskDao.insert(task)

    suspend fun updateTask(task: Task) = taskDao.update(task)

    // Delete the given task from the DB
    suspend fun deleteTask(task: Task): Int = taskDao.delete(task)

    // Returns total pomodoros since given timestamp (default 0 if null)
    fun countPomodorosSince(since: Long): Flow<Int> =
        sessionDao.countSessionsSince(since)

    suspend fun recordSession(taskId: Long) {
        sessionDao.insert(
            PomodoroSession(
                id = 0,
                taskId = taskId,
                completedAt = System.currentTimeMillis()
            )
        )
    }

    companion object {
        @Volatile private var instance: TaskRepository? = null

        fun getInstance(ctx: Context): TaskRepository =
            instance ?: synchronized(this) {
                instance ?: TaskRepository(
                    TaskDatabase.getInstance(ctx).taskDao(),
                    TaskDatabase.getInstance(ctx).pomodoroSessionDao()
                ).also { instance = it }
            }
    }
}