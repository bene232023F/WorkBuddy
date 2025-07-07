package edu.vt.mobiledev.workbuddy.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.vt.mobiledev.workbuddy.PomodoroSession
import kotlinx.coroutines.flow.Flow

/**
 * DAO for managing Task entities in the database.
 */
@Dao
interface TaskDao {
    /**
     * Streams all tasks, ordered by their due timestamp ascending.
     *
     * @return a Flow emitting the current list of all Task objects.
     */
    @Query("SELECT * FROM tasks ORDER BY dueAt ASC")
    fun getAllTasks(): Flow<List<Task>>

    /**
     * Inserts a new Task into the database.
     *
     * @param task the Task to insert.
     * @return the newly generated row ID for the inserted Task.
     */
    @Insert
    suspend fun insert(task: Task): Long

    /**
     * Updates an existing Task in the database.
     *
     * @param task the Task with updated fields.
     */
    @Update
    suspend fun update(task: Task)

    /**
     * Deletes a Task from the database.
     *
     * @param task the Task to remove.
     * @return the number of rows deleted (should be 1 if successful).
     */
    @Delete
    suspend fun delete(task: Task): Int

    /**
     * (Debug) Returns the sum of all pomodoroCounts across every Task.
     *
     * @return a Flow emitting the total, or null if no tasks exist.
     */
    @Query("SELECT SUM(pomodoroCount) FROM tasks")
    fun rawTotalPomodoros(): Flow<Int?>
}

/**
 * DAO for managing PomodoroSession entities in the database.
 */
@Dao
interface PomodoroSessionDao {
    /**
     * Inserts a new PomodoroSession record, indicating one completed session.
     *
     * @param session the PomodoroSession to insert.
     */
    @Insert
    suspend fun insert(session: PomodoroSession)

    /**
     * Counts how many PomodoroSession records have been logged since.
     *
     * @param since the earliest timestamp (inclusive) to count sessions from.
     * @return a Flow emitting the count of sessions; will be 0 if none match.
     */
    @Query("SELECT COUNT(*) FROM pomodoro_sessions WHERE completedAt >= :since")
    fun countSessionsSince(since: Long): Flow<Int>
}