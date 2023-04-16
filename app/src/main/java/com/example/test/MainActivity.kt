package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.Button
import com.example.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.setOnClickListener(::search)
        binding.searchField.setOnEditorActionListener { v, i, e ->

            binding.search.isActivated = binding.searchField.text.length > 3

            true
        }
    }

    private fun search(view: View){

    }

//    private fun keyInput(view: View): Boolean{
//
//    }
}
