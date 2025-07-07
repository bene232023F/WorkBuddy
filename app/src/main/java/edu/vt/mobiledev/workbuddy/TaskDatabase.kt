package edu.vt.mobiledev.workbuddy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.vt.mobiledev.workbuddy.PomodoroSession

/**
 * The Room database for the app, holding both Task and PomodoroSession entities.
 *
 * @Database:
 *  entities: the list of data classes annotated with @Entity
 *  version: increment when you change the schema
 *  exportSchema: whether to export the database schema into a JSON file
 */
@Database(
    entities = [Task::class, PomodoroSession::class],
    version = 2,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    /**
     * Provides the implementation of TaskDao for CRUD on Task entities.
     */
    abstract fun taskDao(): TaskDao

    /**
     * Provides the implementation of PomodoroSessionDao for CRUD on PomodoroSession entities.
     */
    abstract fun pomodoroSessionDao(): PomodoroSessionDao

    companion object {
        // The volatile instance guarantees visibility of changes to other threads immediately
        @Volatile
        private var instance: TaskDatabase? = null

        /**
         * Returns the singleton TaskDatabase. Builds it if it doesn't already exist.
         *
         * @param ctx Application context for database builder
         * @return TaskDatabase singleton instance
         */
        fun getInstance(ctx: Context): TaskDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(ctx).also { instance = it }
            }

        /**
         * Helper to construct the Room database.
         * Uses fallbackToDestructiveMigration during development
         * so no migration path.
         */
        private fun buildDatabase(ctx: Context): TaskDatabase =
            Room.databaseBuilder(
                ctx.applicationContext,
                TaskDatabase::class.java,
                "workbuddy.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}
