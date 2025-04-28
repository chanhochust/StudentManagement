package com.example.studentmanager

import java.io.Serializable

data class Student(
    val name: String,
    val mssv: String,
    val avatarResId: Int = 0,
    var email: String,
    var phone: String
) : Serializable
