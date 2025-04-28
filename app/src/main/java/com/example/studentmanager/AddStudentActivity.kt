package com.example.studentmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddStudentActivity : AppCompatActivity() {

    private lateinit var editTextName: TextInputEditText
    private lateinit var editTextMSSV: TextInputEditText
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPhone: TextInputEditText
    private lateinit var layoutName: TextInputLayout
    private lateinit var layoutMSSV: TextInputLayout
    private lateinit var layoutEmail: TextInputLayout
    private lateinit var buttonSave: Button

    private var position: Int = -1
    private var isUpdateMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        layoutName = findViewById(R.id.layoutName)
        layoutMSSV = findViewById(R.id.layoutMSSV)
        layoutEmail = findViewById(R.id.layoutEmail)
        editTextName = findViewById(R.id.editTextName)
        editTextMSSV = findViewById(R.id.editTextMSSV)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPhone = findViewById(R.id.editTextPhone)
        buttonSave = findViewById(R.id.buttonSave)

        val receivedStudent = intent.getSerializableExtra(MainActivity.EXTRA_STUDENT) as? Student
        position = intent.getIntExtra(MainActivity.EXTRA_POSITION, -1)

        if (receivedStudent != null) {
            isUpdateMode = true
            editTextName.setText(receivedStudent.name)
            editTextMSSV.setText(receivedStudent.mssv)
            editTextEmail.setText(receivedStudent.email)
            editTextPhone.setText(receivedStudent.phone)
            title = "Cập nhật sinh viên"
        } else {
            title = "Thêm sinh viên"
        }

        buttonSave.setOnClickListener {
            saveStudent()
        }
    }

    private fun saveStudent() {
        val name = editTextName.text.toString().trim()
        val mssv = editTextMSSV.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val phone = editTextPhone.text.toString().trim()

        // Kiểm tra dữ liệu đầu vào
        var isValid = true
        if (name.isEmpty()) {
            layoutName.error = "Vui lòng nhập họ tên"
            isValid = false
        } else {
            layoutName.error = null
        }

        if (mssv.isEmpty()) {
            layoutMSSV.error = "Vui lòng nhập MSSV"
            isValid = false
        } else {
            layoutMSSV.error = null
        }

        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layoutEmail.error = "Email không hợp lệ"
            isValid = false
        } else {
            layoutEmail.error = null
        }

        if (!isValid) {
            return
        }

        val student = Student(name, mssv, avatarResId = 0, email = email, phone = phone)
        val resultIntent = Intent()
        resultIntent.putExtra(MainActivity.EXTRA_STUDENT, student)
        resultIntent.putExtra(MainActivity.EXTRA_POSITION, position)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}