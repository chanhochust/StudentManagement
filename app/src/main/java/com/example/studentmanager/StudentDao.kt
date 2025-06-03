package com.example.studentmanager

import androidx.room.*

@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    suspend fun getAll(): List<Student>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: Student)

    @Delete
    suspend fun delete(student: Student)

    @Update
    suspend fun update(student: Student)
}
