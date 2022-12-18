package com.example.classattendance.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.classattendance.R
import com.example.classattendance.Utils.toast
import com.example.classattendance.databinding.FragmentAddUnitBinding
import com.example.classattendance.models.Unit
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddUnitFragment: Fragment(R.layout.fragment_add_unit) {

    lateinit var refernce: DatabaseReference
    private lateinit var name: String
    private lateinit var code: String

    lateinit var binding: FragmentAddUnitBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddUnitBinding.bind(view)

        binding.btnAdd.setOnClickListener{
            saveData()
        }
    }

    private fun saveData() {

        if (validateData()){
            showProgressBar()
            refernce = FirebaseDatabase.getInstance().getReference("Units")

//            val hashMap = HashMap<String, String>()
//            hashMap.put("name", name)
//            hashMap["code"] = code

            val unit = Unit(name, code)

            refernce
                .child(code)
                .setValue(unit)
                .addOnSuccessListener {
                    hideProgressBae()
                    requireContext().toast("Unit Add Successfully")
                    clearUI()
                }
                .addOnFailureListener{
                    hideProgressBae()
                    requireContext().toast("Failed to add Unit. Hint\n ${it.toString()}")
                }

        }
    }

    private fun clearUI() {
        binding.edtName.setText("")
        binding.edtCode.setText("")

    }

    private fun validateData():Boolean {
        //get data from ui
        name= binding.edtName.text.toString()
        code = binding.edtCode.text.toString()

        //validate
        if (name.isEmpty()){
            binding.edtName.error = "Name is empty"
            return false
        }
        if (code.isEmpty()){
            binding.edtCode.setError("Code is empty")
            return false
        }

        return true
    }

    private fun showProgressBar(){
        binding.loadingProgress.loadingProgress.visibility = View.VISIBLE
    }

    private fun hideProgressBae(){
        binding.loadingProgress.loadingProgress.visibility = View.GONE
    }

}