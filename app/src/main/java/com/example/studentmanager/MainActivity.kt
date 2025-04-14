package com.example.studentmanager

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextMSSV: EditText
    private lateinit var buttonAdd: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter

    private val studentList = mutableListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextName = findViewById(R.id.editTextName)
        editTextMSSV = findViewById(R.id.editTextMSSV)
        buttonAdd = findViewById(R.id.buttonAdd)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        studentAdapter = StudentAdapter(studentList) { position ->
            removeStudentAt(position)
        }
        recyclerView.adapter = studentAdapter

        buttonAdd.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val mssv = editTextMSSV.text.toString().trim()

            if (name.isEmpty() || mssv.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            studentAdapter.addStudent(Student(name, mssv))
            editTextName.text.clear()
            editTextMSSV.text.clear()
        }
    }

    private fun removeStudentAt(position: Int) {
        studentAdapter.removeStudent(position)
    }
}