package edu.vt.mobiledev.workbuddy.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.vt.mobiledev.workbuddy.PomodoroSession
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Task entities.
 */
@Dao
interface TaskDao {
    /**
     * Observe all tasks, ordered by due time descending.
     */
    @Query("SELECT * FROM tasks ORDER BY dueAt ASC")
    fun getAllTasks(): Flow<List<Task>>

    /**
     * Insert a new Task. Returns the new row ID.
     */
    @Insert
    suspend fun insert(task: Task): Long

    /**
     * Update an existing Task.
     */
    @Update
    suspend fun update(task: Task)

    /**
     * Delete a Task.
     */
    @Delete
    suspend fun delete(task: Task): Int

    /** For debugging only: total of all pomodoroCount, no WHERE clause */
    @Query("SELECT SUM(pomodoroCount) FROM tasks")
    fun rawTotalPomodoros(): Flow<Int?>
}

@Dao
interface PomodoroSessionDao {
    @Insert
    suspend fun insert(session: PomodoroSession)

    /**
     * Count total pomodoro sessions logged since the given timestamp.
     * Returns null if there are no records, so caller should default to 0.
     */
    @Query("SELECT COUNT(*) FROM pomodoro_sessions WHERE completedAt >= :since")
    fun countSessionsSince(since: Long): Flow<Int>
}