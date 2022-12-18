package com.example.classattendance.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classattendance.Constants
import com.example.classattendance.R
import com.example.classattendance.Utils.toast
import com.example.classattendance.adapater.AdapterStudent
import com.example.classattendance.databinding.FragmentAttendanceBinding
import com.example.classattendance.models.Student
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class AttendanceFragment: Fragment(R.layout.fragment_attendance) {

    private val  TAG="AttendanceFragment"

    lateinit var binding: FragmentAttendanceBinding

    private lateinit var unit: String
    private lateinit var lecturer: String
    private lateinit var year: String
    private lateinit var level: String
    private lateinit var field: String

    private lateinit var adapterStudent: AdapterStudent
    private lateinit var studentList: ArrayList<Student>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAttendanceBinding.bind(view)

        getUnitSelected()
        getSelectedLecturer()
        getSelectedYear()
        getSelectedLevel()
        getSelectedField()

        //handle search click listner
        binding.searchBtn.setOnClickListener {
            getAllStudents()
        }

        binding.tvPrevious.setOnClickListener {
            findNavController().navigate(R.id.action_attendanceFragment_to_previousAttendanceFragment)
        }

    }

    private fun saveAttendance() {
        //get current date
        val date =  Date()
        val formatter = SimpleDateFormat("dd_MM_yyyy")
        val dateStr = formatter.format(date)

        val reference : DatabaseReference

        var hashMap = HashMap<String, Any>()

        hashMap["lecturer"] = lecturer
        hashMap["unit"] = unit
        hashMap["year"] = year
        hashMap["field"] = field
        hashMap["date"] = dateStr
        //hashMap["students"] = studentList


        reference = FirebaseDatabase.getInstance().getReference()
        reference
            .child("Attendance")
            .child(year)
            .child(level)
            .child(field)
            .child(dateStr)
            .child(unit)
            .setValue(hashMap)
            .addOnSuccessListener {
                hideProgressBar()
                requireActivity().toast("Attendance Saved successfully")
            }
            .addOnFailureListener{
                hideProgressBar()
                requireActivity().toast("Failed to save Attendance.\nHint ${it.toString()}")
            }

    }


    private fun getAllStudents(){
        showProgressBar()

        //initialize arraylist
        studentList = ArrayList()

        //initialize adapter
        adapterStudent = AdapterStudent(requireContext(), studentList)

        //set layout manager
        binding.studentRv.layoutManager = LinearLayoutManager(requireContext()) //no need since already set in the ui
        binding.studentRv.adapter = adapterStudent

        val reference : DatabaseReference
        reference = FirebaseDatabase.getInstance().getReference()
        reference
            .child("Students")
            .child(year)
            .child(level)
            .child(field)
            .addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //hideProgressBar()

                //clear the previous list
                studentList.clear()

                for (postSnapshot in snapshot.children){
                    //get one user
                    val currentUser = postSnapshot.getValue(Student::class.java)

                    //show all users apart from me
                    //add the current user to the user list
                    studentList.add(currentUser!!)

                    saveAttendance()
                    binding.searchBtn.visibility = View.GONE

                }

                //when a new user is add, reflect on the list
                adapterStudent.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                hideProgressBar()
            }

        })

    }


    private fun getSelectedYear() {
        val customList = listOf("2018", "2019", "2020", "2021", "2023", "2024","2025", "2026")
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, customList)
        binding.spYear.adapter = adapter

        binding.spYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                year = adapterView?.getItemAtPosition(position).toString()
                Constants.year=year
                requireContext().toast( "You selected: ${adapterView?.getItemAtPosition(position).toString()}")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                year = ""
            }

        }
    }

    private fun getSelectedLecturer() {
        val customList = listOf("Peter Ngugi", "Peter Kariuki", "Felix Okoth")
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, customList)
        binding.spLec.adapter = adapter

        binding.spLec.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                lecturer = adapterView?.getItemAtPosition(position).toString()
                Constants.lecturer=lecturer
                requireContext().toast( "You selected: ${adapterView?.getItemAtPosition(position).toString()}")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                lecturer = ""
            }

        }
    }

    private fun getUnitSelected() {
        val customList = listOf("Visual Programming", "Artificial Intelligence", "Software Engineering", "Calculus", "Numerical Methods")
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, customList)
        binding.spUnits.adapter = adapter

        binding.spUnits.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                unit = adapterView?.getItemAtPosition(position).toString()
                Constants.unit = unit
                requireContext().toast( "You selected: ${adapterView?.getItemAtPosition(position).toString()}")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                unit = ""
            }

        }
    }


    private fun getSelectedLevel() {
        val levelList = listOf("Degree", "Diploma")
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, levelList)
        binding.spLevel.adapter = adapter

        //onclick listener
        binding.spLevel.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                level=adapterView?.getItemAtPosition(position).toString()
                Constants.level = level
                requireContext().toast("Level selected: ${adapterView?.getItemAtPosition(position).toString()}")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                level=""
            }

        }
    }

    private fun getSelectedField() {
        val fieldList = listOf("Computer_Technology", "Information_Technology", "Communication_Computer_Network")
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, fieldList)
        binding.spField.adapter = adapter

        //onclick listener
        binding.spField.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                field=adapterView?.getItemAtPosition(position).toString()
                Constants.field = field
                binding.searchBtn.visibility=View.VISIBLE
                requireContext().toast("Field selected: ${adapterView?.getItemAtPosition(position).toString()}")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                field=""
            }

        }
    }

    private fun showProgressBar(){
        binding.loadingProgress.loadingProgress.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.loadingProgress.loadingProgress.visibility = View.GONE
    }
}