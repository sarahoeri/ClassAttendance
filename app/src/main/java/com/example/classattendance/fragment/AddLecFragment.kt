package com.example.classattendance.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.classattendance.R
import com.example.classattendance.Utils.toast
import com.example.classattendance.databinding.FragmentAddLecBinding
import com.example.classattendance.models.Lecturer
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddLecFragment: Fragment(R.layout.fragment_add_lec) {

    lateinit var refernce: DatabaseReference
    private lateinit var name: String
    private lateinit var phone: String


    lateinit var binding: FragmentAddLecBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddLecBinding.bind(view)

        binding.btnAdd.setOnClickListener{
            saveData()
        }
    }

    private fun saveData() {

        if (validateData()){
            showProgressBar()
            refernce = FirebaseDatabase.getInstance().getReference("Lecturers")

//            val hashMap = HashMap<String, String>()
//            hashMap.put("name", name)
//            hashMap["phone"] = phone

            val lecturer = Lecturer(name, phone)

            refernce
                .child(phone)
                .setValue(lecturer)
                .addOnSuccessListener {
                    hideProgressBae()
                    requireContext().toast("Lecturer Add Successfully")
                    clearUI()
                }
                .addOnFailureListener{
                    hideProgressBae()
                    requireContext().toast("Failed to add Lecturer. Hint\n ${it.toString()}")
                }

        }
    }

    private fun clearUI() {
        binding.edtName.setText("")
        binding.edtPhone.setText("")

    }

    private fun validateData():Boolean {
        //get data from ui
        name= binding.edtName.text.toString()
        phone = binding.edtPhone.text.toString()

        //validate
        if (name.isEmpty()){
            binding.edtName.error = "Name is empty"
            return false
        }
        if (phone.isEmpty()){
            binding.edtPhone.setError("Phone is empty")
            return false
        }
        if (phone.length != 10){
            binding.edtPhone.setError("Phone number length should consist 10 digits")
            return false
        }
        //check if year is a number
        try {
            phone.toInt()
            return true
        }
        catch (e: Exception){
            binding.edtPhone.setError("Wrong Phone format")
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