package edu.vt.mobiledev.workbuddy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.vt.mobiledev.workbuddy.PomodoroSession

@Database(entities = [Task::class, PomodoroSession::class], version = 2, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun pomodoroSessionDao(): PomodoroSessionDao

    companion object {
        @Volatile private var instance: TaskDatabase? = null
        fun getInstance(ctx: Context): TaskDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    ctx.applicationContext,
                    TaskDatabase::class.java,
                    "workbuddy.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
    }
}