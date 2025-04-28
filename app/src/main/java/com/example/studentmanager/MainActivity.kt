package com.example.studentmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var emptyView: TextView
    private val studentList = mutableListOf<Student>()

    companion object {
        const val REQUEST_ADD = 1
        const val REQUEST_UPDATE = 2
        const val EXTRA_STUDENT = "extra_student"
        const val EXTRA_POSITION = "extra_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Student Manager"

        val buttonAdd: ImageButton = findViewById(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD)
        }

        recyclerView = findViewById(R.id.recyclerView)
        emptyView = findViewById(R.id.emptyView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        studentAdapter = StudentAdapter(studentList,
            onDeleteClickListener = { position ->
                confirmDelete(position)
            },
            onUpdateClickListener = { student, position ->
                val intent = Intent(this, AddStudentActivity::class.java)
                intent.putExtra(EXTRA_STUDENT, student)
                intent.putExtra(EXTRA_POSITION, position)
                startActivityForResult(intent, REQUEST_UPDATE)
            })
        recyclerView.adapter = studentAdapter

        // Thêm dữ liệu mẫu
        studentList.add(Student("Nguyen Van A", "12345", 0, "test@example.com", "0123456789"))
        studentAdapter.notifyDataSetChanged()
        updateEmptyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val student = data.getSerializableExtra(EXTRA_STUDENT) as? Student ?: return
            when (requestCode) {
                REQUEST_ADD -> {
                    studentAdapter.addStudent(student)
                }
                REQUEST_UPDATE -> {
                    val position = data.getIntExtra(EXTRA_POSITION, -1)
                    if (position != -1) {
                        studentAdapter.updateStudent(student, position)
                    }
                }
            }
            updateEmptyView()
        }
    }

    private fun confirmDelete(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận")
            .setMessage("Bạn có chắc muốn xóa sinh viên này không?")
            .setPositiveButton("Xóa") { _, _ ->
                studentAdapter.removeStudent(position)
                updateEmptyView()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun updateEmptyView() {
        emptyView.visibility = if (studentList.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        recyclerView.visibility = if (studentList.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
    }
}