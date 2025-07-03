package edu.vt.mobiledev.workbuddy.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Task entities.
 */
@Dao
interface TaskDao {
    /**
     * Observe all tasks, ordered by creation time descending.
     */
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    /**
     * Insert a new Task. Returns the new row ID.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
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
    suspend fun delete(task: Task)

    /**
     * Count total pomodoro sessions logged since the given timestamp.
     * Returns null if there are no records, so caller should default to 0.
     */
    @Query("SELECT SUM(pomodoroCount) FROM tasks WHERE createdAt >= :since")
    fun countPomodorosSince(since: Long): Flow<Int?>
}