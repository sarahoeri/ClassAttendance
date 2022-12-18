package com.example.classattendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.classattendance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}