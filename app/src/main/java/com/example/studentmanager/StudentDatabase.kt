package com.example.studentmanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "students.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "students"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_MSSV = "mssv"
        private const val COLUMN_AVATAR = "avatar"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NAME TEXT,
            $COLUMN_MSSV TEXT,
            $COLUMN_AVATAR INTEGER,
            $COLUMN_EMAIL TEXT,
            $COLUMN_PHONE TEXT
        )"""
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertStudent(student: Student) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_MSSV, student.mssv)
            put(COLUMN_AVATAR, student.avatarResId)
            put(COLUMN_EMAIL, student.email)
            put(COLUMN_PHONE, student.phone)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateStudent(student: Student, mssv: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_MSSV, student.mssv)
            put(COLUMN_AVATAR, student.avatarResId)
            put(COLUMN_EMAIL, student.email)
            put(COLUMN_PHONE, student.phone)
        }
        db.update(TABLE_NAME, values, "$COLUMN_MSSV = ?", arrayOf(mssv))
        db.close()
    }

    fun deleteStudent(mssv: String) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_MSSV = ?", arrayOf(mssv))
        db.close()
    }

    fun getAllStudents(): MutableList<Student> {
        val list = mutableListOf<Student>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val mssv = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MSSV))
                val avatar = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVATAR))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
                list.add(Student(name, mssv, avatar, email, phone))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }
}
