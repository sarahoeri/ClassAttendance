package com.example.classattendance.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.classattendance.R
import com.example.classattendance.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.classattendance.Utils.toast2

class LoginFragment: Fragment(R.layout.fragment_login) {

    //declare
    private lateinit var firebaseAuth: FirebaseAuth

    lateinit var binding: FragmentLoginBinding
    lateinit var email: String
    lateinit var password: String
    private var TAG = "LoginFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)

        //initialize
        firebaseAuth = FirebaseAuth.getInstance()



        //handle register button click listener
        binding.btnRegister.setOnClickListener {
            email = binding.edtEmail.text.toString()
            password = binding.edtPassword.text.toString()
            createAccount()
        }

        binding.btnLogin.setOnClickListener{
            email = binding.edtEmail.text.toString()
            password = binding.edtPassword.text.toString()
            login()
        }

    }

    private fun login() {
        if (validateData()){
            showProgressBar()
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    hideProgressBae()
                    //navigate home
                    findNavController().navigate(R.id.action_loginFragment_to_homeSecretaryFragment)

                }
                .addOnFailureListener{
                    hideProgressBae()
                    toast2(requireContext(), "Error ${it.toString()}")
                }

        }
    }

    private fun createAccount() {
        if (validateData()){
            showProgressBar()

            //create account
            firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    hideProgressBae()
                    //navigate home
                    findNavController().navigate(R.id.action_loginFragment_to_homeSecretaryFragment)

                }
                .addOnFailureListener{
                    hideProgressBae()
                    toast2(requireContext(), "Error ${it.toString()}")
                }
        }
    }

    private fun validateData():Boolean {

        if (email.isEmpty()){
            binding.edtEmail.error = "Email is emty"
            return false
        }
        if (password.isEmpty()){
            binding.edtPassword.setError("Password is empty")
            return false
        }
        if (! Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtEmail.setError("Wrong email address")
            return false
        }
        if (password.length < 6){
            binding.edtPassword.setError("Password length can't be less than 6")
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