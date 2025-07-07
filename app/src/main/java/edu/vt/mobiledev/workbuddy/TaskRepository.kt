package edu.vt.mobiledev.workbuddy.data

import android.content.Context
import edu.vt.mobiledev.workbuddy.PomodoroSession
import kotlinx.coroutines.flow.Flow

/**
 * Repository that mediates between the app and the Room database.
 * Provides methods to access Task and PomodoroSession data.
 */
class TaskRepository private constructor(
    private val taskDao: TaskDao,
    private val sessionDao: PomodoroSessionDao
) {

    /**
     * Observe all tasks, ordered by due time ascending.
     */
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    /**
     * Insert a new Task into the database.
     *
     * @param task The Task to insert.
     */
    suspend fun insertTask(task: Task) = taskDao.insert(task)

    /**
     * Update an existing Task in the database.
     *
     * @param task The Task with updated fields.
     */
    suspend fun updateTask(task: Task) = taskDao.update(task)

    /**
     * Delete a Task from the database.
     *
     * @param task The Task to delete.
     * @return Number of rows deleted (should be 1 if successful).
     */
    suspend fun deleteTask(task: Task): Int = taskDao.delete(task)

    /**
     * Count the number of Pomodoro sessions completed since the given timestamp.
     *
     * @param since Timestamp in milliseconds.
     * @return A Flow emitting the count of sessions.
     */
    fun countPomodorosSince(since: Long): Flow<Int> =
        sessionDao.countSessionsSince(since)

    /**
     * Record a new PomodoroSession for a given task ID, with the current time.
     *
     * @param taskId The ID of the Task just completed.
     */
    suspend fun recordSession(taskId: Long) {
        sessionDao.insert(
            PomodoroSession(
                id = 0,  // Auto-generated primary key
                taskId = taskId,
                completedAt = System.currentTimeMillis()
            )
        )
    }

    companion object {
        // Holds the singleton instance
        @Volatile private var instance: TaskRepository? = null

        /**
         * Obtain the singleton TaskRepository instance, creating it if necessary.
         *
         * @param ctx Application context for database access.
         */
        fun getInstance(ctx: Context): TaskRepository =
            instance ?: synchronized(this) {
                instance ?: TaskRepository(
                    TaskDatabase.getInstance(ctx).taskDao(),
                    TaskDatabase.getInstance(ctx).pomodoroSessionDao()
                ).also { instance = it }
            }
    }
}