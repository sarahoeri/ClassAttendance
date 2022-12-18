package com.example.classattendance.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classattendance.Constants
import com.example.classattendance.R
import com.example.classattendance.Utils.toast
import com.example.classattendance.adapater.AdapterAttendance
import com.example.classattendance.adapater.AdapterStudent
import com.example.classattendance.databinding.FragmentPreviousAttendanceBinding
import com.example.classattendance.models.Attendance
import com.example.classattendance.models.Student
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class PreviousAttendanceFragment: Fragment(R.layout.fragment_previous_attendance){

    private lateinit var binding: FragmentPreviousAttendanceBinding

    var cal = Calendar.getInstance()
    private var search_date: String = ""

    private lateinit var unit: String
    private lateinit var lecturer: String
    private lateinit var year: String
    private lateinit var level: String
    private lateinit var field: String

    private lateinit var adapterStudent: AdapterAttendance
    private lateinit var studentList: ArrayList<Attendance>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPreviousAttendanceBinding.bind(view)

        getDateSelected()
        getUnitSelected()
        getSelectedLecturer()
        getSelectedYear()
        getSelectedLevel()
        getSelectedField()

        binding.searchBtn.setOnClickListener {
            if (search_date.isNotEmpty()){
                getAllStudents()
            }
        }
    }

    private fun getDateSelected() {
        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        binding.edtSearchDate!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(requireContext(),
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })
    }

    private fun updateDateInView() {
        val myFormat = "dd_MM_yyyy"
        val sdf = SimpleDateFormat(myFormat)
        val date = sdf.format(cal.getTime())
        binding.edtSearchDate.setText(date)
        search_date = date
        binding.searchBtn.visibility = View.VISIBLE
    }


    private fun getAllStudents(){
        showProgressBar()

        //initialize arraylist
        studentList = ArrayList()

        //initialize adapter
        adapterStudent = AdapterAttendance(requireContext(), studentList)

        //set layout manager
        binding.studentRv.layoutManager = LinearLayoutManager(requireContext()) //no need since already set in the ui
        binding.studentRv.adapter = adapterStudent

        val reference : DatabaseReference
        reference = FirebaseDatabase.getInstance().getReference()
        reference
            .child("Attendance")
            .child(year)
            .child(level)
            .child(field)
            .child(search_date)
            .child(unit)
            .child("Students")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    hideProgressBar()

                    //clear the previous list
                    studentList.clear()

                    for (postSnapshot in snapshot.children){
                        //get one user
                        val currentStudent = postSnapshot.getValue(Attendance::class.java)

                        //add the current user to the user list
                        studentList.add(currentStudent!!)

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
        val levelList = listOf("Computer_Technology", "Information_Technology", "Communication_Computer_Network")
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, levelList)
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