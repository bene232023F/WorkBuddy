package edu.vt.mobiledev.workbuddy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a task that the user wants to track.
 *
 * @property id            Auto-generated unique identifier for this Task.
 * @property title         The user-provided title or description of the task.
 * @property isCompleted   Whether the task has been marked completed.
 * @property pomodoroCount How many Pomodoro sessions have been applied to this task.
 * @property createdAt     Timestamp (in ms) when the task was created.
 * @property dueAt         Timestamp (in ms) by which the task should be completed.
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /** The name or description of the task. */
    val title: String,

    /** True if the user has checked off this task as completed. */
    val isCompleted: Boolean = false,

    /** Number of completed Pomodoro intervals logged against this task. */
    val pomodoroCount: Int = 0,

    /**
     * When this Task was created.
     * Defaults to the current system time at insertion.
     */
    val createdAt: Long = System.currentTimeMillis(),

    /**
     * When this Task is due.
     */
    val dueAt: Long = System.currentTimeMillis()
)
