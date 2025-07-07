package edu.vt.mobiledev.workbuddy

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a single Pomodoro session.
 * Each session is linked to a Task and records when it was completed.
 */
@Entity(tableName = "pomodoro_sessions")
data class PomodoroSession(
    /** Auto-generated primary key for this session record */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /** ID of the Task this session belongs to */
    val taskId: Long,

    /** Timestamp (in milliseconds) when the session was completed */
    val completedAt: Long
)
