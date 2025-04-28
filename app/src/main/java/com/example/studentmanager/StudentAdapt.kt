package com.example.studentmanager

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val studentList: MutableList<Student>,
    private val onDeleteClickListener: (Int) -> Unit,
    private val onUpdateClickListener: (Student, Int) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

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
        val optionButton: ImageButton = itemView.findViewById(R.id.button_options)

        init {
            optionButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    showPopupMenu(it, position)
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
        holder.nameTextView.text = currentStudent.name
        holder.mssvTextView.text = currentStudent.mssv

        try {
            if (currentStudent.avatarResId != 0) {
                holder.avatarImageView.setImageResource(currentStudent.avatarResId)
            } else {
                val randomAvatar = avatarResources.random()
                holder.avatarImageView.setImageResource(randomAvatar)
                studentList[position] = currentStudent.copy(avatarResId = randomAvatar)
            }
        } catch (e: Exception) {
            holder.avatarImageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    override fun getItemCount() = studentList.size

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.menu_student_popup, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val student = studentList[position]
            when (menuItem.itemId) {
                R.id.menu_update -> {
                    onUpdateClickListener(student, position)
                    true
                }
                R.id.menu_delete -> {
                    onDeleteClickListener(position)
                    true
                }
                R.id.menu_call -> {
                    if (student.phone.isEmpty()) {
                        Toast.makeText(view.context, "Không có số điện thoại", Toast.LENGTH_SHORT).show()
                        false
                    } else {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${student.phone}")
                        view.context.startActivity(intent)
                        true
                    }
                }
                R.id.menu_email -> {
                    if (student.email.isEmpty()) {
                        Toast.makeText(view.context, "Không có email", Toast.LENGTH_SHORT).show()
                        false
                    } else {
                        val intent = Intent(Intent.ACTION_SENDTO)
                        intent.data = Uri.parse("mailto:${student.email}")
                        view.context.startActivity(intent)
                        true
                    }
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    fun addStudent(student: Student) {
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

    fun updateStudent(student: Student, position: Int) {
        if (position in 0 until studentList.size) {
            val studentWithAvatar = if (student.avatarResId == 0) {
                student.copy(avatarResId = avatarResources.random())
            } else {
                student
            }
            studentList[position] = studentWithAvatar
            notifyItemChanged(position)
        }
    }

    fun updateStudents(newStudents: List<Student>) {
        studentList.clear()
        studentList.addAll(newStudents)
        notifyDataSetChanged()
    }
}