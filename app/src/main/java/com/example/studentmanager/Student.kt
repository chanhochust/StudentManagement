package com.example.studentmanager

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "students")
data class Student(
    val name: String,
    @PrimaryKey val mssv: String,
    val avatarResId: Int = 0,
    var email: String,
    var phone: String
) : Serializable
