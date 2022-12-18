package com.example.classattendance.adapater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.classattendance.Constants
import com.example.classattendance.R
import com.example.classattendance.Utils.toast
import com.example.classattendance.models.Attendance
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterAttendance(val context: Context, val studentList:
ArrayList<Attendance>): RecyclerView.Adapter<AdapterAttendance.StudentViewHolder>() {


    //private lateinit var currentStudent: Student

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        //initialize view
        val view: View = LayoutInflater.from(context).inflate(R.layout.row_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        //get data
         val currentStudent = studentList[position]

        //set data
        holder.pos_tv.text = "${position + 1}" //first position is 0, so add 1 to start position 1
        holder.name_tv.text = currentStudent.name
        holder.reg_nu_tV.text = currentStudent.reg_num
        holder.attended_cb.isChecked = true

    }


    override fun getItemCount(): Int {
        return studentList.size
    }

    inner class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val pos_tv= itemView.findViewById<TextView>(R.id.pos_tv)
        val name_tv= itemView.findViewById<TextView>(R.id.name_tv)
        val reg_nu_tV= itemView.findViewById<TextView>(R.id.reg_num_tv)
        val attended_cb= itemView.findViewById<CheckBox>(R.id.cbAttended)

    }


}