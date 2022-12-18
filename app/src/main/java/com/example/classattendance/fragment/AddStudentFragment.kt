package com.example.classattendance.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.classattendance.R
import com.example.classattendance.Utils.toast
import com.example.classattendance.databinding.FragmentAddStudentBinding
import com.example.classattendance.models.Student
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddStudentFragment: Fragment(R.layout.fragment_add_student) {

    lateinit var refernce: DatabaseReference
    private lateinit var name: String
    private lateinit var reg_num: String
    private lateinit var year: String
    private  var level: String = ""
    private  var field: String=""

    lateinit var binding: FragmentAddStudentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddStudentBinding.bind(view)

        getSelectedLevel()

        getSelectedField()


        binding.btnAdd.setOnClickListener{
            saveData()
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
                requireContext().toast("Field selected: ${adapterView?.getItemAtPosition(position).toString()}")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                field=""
            }

        }
    }


    private fun saveData() {
        showProgressBar()
        if (validateData()){

            refernce = FirebaseDatabase.getInstance().getReference("Students")

//            val hashMap = HashMap<String, String>()
//            hashMap.put("name", name)
//            hashMap["reg_num"] = reg_num
//            hashMap.put("year", year)

            reg_num = reg_num.toString().replace("/", "_") //replace / with _ ... since "/" is used in directory

            val student = Student(name, reg_num, year)

            refernce
                .child(year)
                .child(level)
                .child(field)
                .child(reg_num)
                .setValue(student)
                .addOnSuccessListener {
                    hideProgressBae()
                    requireContext().toast("Student Add Successfully")
                    clearUI()
                }
                .addOnFailureListener{
                    hideProgressBae()
                    requireContext().toast("Failed to add Student. Hint\n ${it.toString()}")
                }

        }
    }

    private fun clearUI() {
        binding.edtRegNu.setText("")
        binding.edtName.setText("")
        binding.edtYear.setText("")

    }

    private fun validateData():Boolean {
        //get data from ui
        reg_num = binding.edtRegNu.text.toString()
        name= binding.edtName.text.toString()
        year = binding.edtYear.text.toString()

        //validate
        if (reg_num.isEmpty()){
            binding.edtRegNu.setError("Registration Number is empty")
            return false
        }
        if (name.isEmpty()){
            binding.edtName.error = "Name is empty"
            return false
        }
        if (year.isEmpty()){
            binding.edtYear.setError("year is empty")
            return false
        }
        if (level.isEmpty()){
            requireContext().toast("You have not selected any level")
            return false
        }

        if (field.isEmpty()){
            requireContext().toast("You have not selected any Field")
            return false
        }

        //check if year is a number
        try {
            year.toInt()
            return true
        }
        catch (e: Exception){
            binding.edtYear.setError("Wrong year format")
            return false
        }
    }

    private fun showProgressBar(){
        binding.loadingProgress.loadingProgress.visibility = View.VISIBLE
    }

    private fun hideProgressBae(){
        binding.loadingProgress.loadingProgress.visibility = View.GONE
    }

}