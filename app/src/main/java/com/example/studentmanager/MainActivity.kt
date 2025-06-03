package com.example.studentmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var emptyView: TextView
    //private lateinit var dbHelper: StudentDatabase
    private lateinit var db: AppDatabase
    private lateinit var studentDao: StudentDao
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

        // Khoi tao Room db
        db = AppDatabase.getDatabase(this)
        studentDao = db.studentDao()

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

        /*dbHelper = StudentDatabase(this)
        val path = dbHelper.readableDatabase.path
        Log.d("DB_PATH", "Database path: $path")
        studentList.addAll(dbHelper.getAllStudents())*/

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
        //studentList.add(Student("Nguyen Van A", "12345", 0, "test@example.com", "0123456789"))
        //studentAdapter.notifyDataSetChanged()
        //updateEmptyView()
        loadStudents()
    }
    private fun loadStudents() {
        lifecycleScope.launch {
            try {
                val students = studentDao.getAll()
                studentList.clear()
                studentList.addAll(students)
                studentAdapter.notifyDataSetChanged()
                updateEmptyView()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error loading students", e)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val student = data.getSerializableExtra(EXTRA_STUDENT) as? Student ?: return
            /*when (requestCode) {
                REQUEST_ADD -> {
                    studentAdapter.addStudent(student)
                    dbHelper.insertStudent(student)
                }
                REQUEST_UPDATE -> {
                    val position = data.getIntExtra(EXTRA_POSITION, -1)
                    if (position != -1) {
                        val oldStudent = studentList[position]
                        dbHelper.updateStudent(student, oldStudent.mssv)
                        studentAdapter.updateStudent(student, position)
                    }
                }
            }
            updateEmptyView()*/
            lifecycleScope.launch {
                try {
                    when (requestCode) {
                        REQUEST_ADD -> {
                            studentDao.insert(student)
                            loadStudents()
                        }
                        REQUEST_UPDATE -> {
                            val position = data.getIntExtra(EXTRA_POSITION, -1)
                            if (position != -1) {
                                studentDao.update(student)
                                loadStudents()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Database operation failed", e)
                }
            }
        }
    }


    private fun confirmDelete(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận")
            .setMessage("Bạn có chắc muốn xóa sinh viên này không?")
            .setPositiveButton("Xóa") { _, _ ->
                //val student = studentList[position]
                //dbHelper.deleteStudent(student.mssv)
                //studentAdapter.removeStudent(position)
                //updateEmptyView()
                lifecycleScope.launch {
                    try {
                        val student = studentList[position]
                        studentDao.delete(student)
                        loadStudents()
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error deleting student", e)
                    }
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun updateEmptyView() {
        emptyView.visibility = if (studentList.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        recyclerView.visibility = if (studentList.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
    }
}