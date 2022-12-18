package com.example.classattendance.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.classattendance.R
import com.example.classattendance.databinding.FragmentHomeSecretaryBinding

class HomeSecretaryFragment: Fragment(R.layout.fragment_home_secretary) {

    lateinit var binding: FragmentHomeSecretaryBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeSecretaryBinding.bind(view)

        //navigate to add student fragment
        binding.addStudentsCV.setOnClickListener {
            findNavController().navigate(R.id.action_homeSecretaryFragment_to_addStudentFragment2)
        }

        //navigate to add Lecturer fragment
        binding.addLecturerCV.setOnClickListener {
            findNavController().navigate(R.id.action_homeSecretaryFragment_to_addLecFragment)
        }

        //navigate to add Unit fragment
        binding.addUnitCV.setOnClickListener {
            findNavController().navigate(R.id.action_homeSecretaryFragment_to_addUnitFragment)
        }

        //navigate to add Attendance fragment
        binding.attendanceCL.setOnClickListener {
            findNavController().navigate(R.id.action_homeSecretaryFragment_to_attendanceFragment)
        }


    }
}