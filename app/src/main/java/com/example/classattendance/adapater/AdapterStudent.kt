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
import com.example.classattendance.models.Student
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterStudent(val context: Context, val studentList:
ArrayList<Student>): RecyclerView.Adapter<AdapterStudent.StudentViewHolder>() {


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
        holder.pos_tv.setText("${position + 1}") //first position is 0, so add 1 to start position 1
        holder.name_tv.setText(currentStudent.name)
        holder.reg_nu_tV.text = currentStudent.reg_num

//        holder.itemView.setOnClickListener {
//
//        }

        //handle checkbox click listner
        //update single user
        holder.itemView.setOnClickListener {
            holder.attended_cb.isChecked = true
            //holder.itemView.findViewById<CheckBox>(R.id.cbAttended).isChecked = true
            updateAttendedToDB(holder, currentStudent.name, currentStudent.reg_num)
        }
    }

    private fun updateAttendedToDB(holder: StudentViewHolder, name: String?, regNum: String?) {
        //get current date
        val date =  Date()
        val formatter = SimpleDateFormat("dd_MM_yyyy")
        val dateStr = formatter.format(date)
        val timeStamp = System.currentTimeMillis()

        var year = Constants.year
        var level = Constants.level
        var field = Constants.field
        var unit = Constants.unit
        var isAttended = true


        val attendance = Attendance(year, level, field,name!!, regNum!!, isAttended)

        val reference = FirebaseDatabase.getInstance().getReference()
        reference
            .child("Attendance")
            .child(year)
            .child(level)
            .child(field)
            .child(dateStr)
            .child(unit)
            .child("Students")
            //.child(timeStamp.toString())
            .child(regNum)
            .setValue(attendance)
            .addOnSuccessListener {
                //hideProgressBar()
                //studentList.clear() //after student clicks , clear the list
                holder.itemView.visibility=View.GONE
                context.toast("Attendance Saved successfully")
            }
            .addOnFailureListener{
                ///hideProgressBar()
                context.toast("Failed to save Attendance.\nHint ${it.toString()}")
            }

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