package com.example.studentmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class StudentAdapter(
    private val studentList: MutableList<Student>,
    private val onDeleteClickListener: (Int) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    // Danh sách các avatar có sẵn
    private val avatarResources = listOf(
        R.drawable.thumb0,
        R.drawable.thumb1,
        R.drawable.thumb11,
        R.drawable.thumb10,
        R.drawable.thumb13,
        R.drawable.thumb23
    )

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarImageView: ImageView = itemView.findViewById(R.id.image_avatar)
        val nameTextView: TextView = itemView.findViewById(R.id.text_hoten)
        val mssvTextView: TextView = itemView.findViewById(R.id.text_mssv)
        val deleteButton: Button = itemView.findViewById(R.id.button_delete)

        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_item, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = studentList[position]

        // Hiển thị thông tin sinh viên
        holder.nameTextView.text = currentStudent.name
        holder.mssvTextView.text = currentStudent.mssv

        // Hiển thị avatar (nếu có)
        if (currentStudent.avatarResId != 0) {
            holder.avatarImageView.setImageResource(currentStudent.avatarResId)
        } else {
            // Nếu không có avatar, chọn ngẫu nhiên từ danh sách
            val randomAvatar = avatarResources.random()
            holder.avatarImageView.setImageResource(randomAvatar)
            // Cập nhật lại student để lưu avatar đã chọn
            studentList[position] = currentStudent.copy(avatarResId = randomAvatar)
        }
    }

    override fun getItemCount() = studentList.size

    fun addStudent(student: Student) {
        // Thêm avatar ngẫu nhiên khi thêm sinh viên mới
        val studentWithAvatar = if (student.avatarResId == 0) {
            student.copy(avatarResId = avatarResources.random())
        } else {
            student
        }
        studentList.add(studentWithAvatar)
        notifyItemInserted(studentList.size - 1)
    }

    fun removeStudent(position: Int) {
        if (position in 0 until studentList.size) {
            studentList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // Cập nhật toàn bộ danh sách
    fun updateStudents(newStudents: List<Student>) {
        studentList.clear()
        studentList.addAll(newStudents)
        notifyDataSetChanged()
    }
}