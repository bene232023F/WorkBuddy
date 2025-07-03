package edu.vt.mobiledev.workbuddy.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository private constructor(
    private val dao: TaskDao
) {

    fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()

    suspend fun insertTask(task: Task) = dao.insert(task)

    suspend fun updateTask(task: Task) = dao.update(task)

    suspend fun deleteTask(task: Task) = dao.delete(task)

    // Returns total pomodoros since given timestamp (default 0 if null)
    fun countPomodorosSince(since: Long): Flow<Int> =
        dao.countPomodorosSince(since).map { it ?: 0 }

    companion object {
        @Volatile
        private var instance: TaskRepository? = null

        fun getInstance(context: Context): TaskRepository =
            instance ?: synchronized(this) {
                instance ?: TaskRepository(
                    TaskDatabase.getInstance(context).taskDao()
                ).also { instance = it }
            }
    }
}