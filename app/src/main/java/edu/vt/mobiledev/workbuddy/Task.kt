package edu.vt.mobiledev.workbuddy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val isCompleted: Boolean = false,
    val pomodoroCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)