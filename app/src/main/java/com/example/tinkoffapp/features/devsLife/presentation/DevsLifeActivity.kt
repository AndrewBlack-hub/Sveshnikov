package com.example.tinkoffapp.features.devsLife.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.tinkoffapp.R
import com.example.tinkoffapp.databinding.ActivityDevsLifeBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DevsLifeActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityDevsLifeBinding::bind)
    private val viewModel: DevsLifeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devs_life)
        updateUI()
        loadRandomPost()
    }

    private fun loadRandomPost() {
        viewModel.getRandomPost()
    }

    private fun updateUI() {
        viewModel.randomPostLiveData.observe(this) { data ->
            if (data != null) {
                binding.textView.text = data.description
            } else {
                Toast.makeText(applicationContext, "Пусто", Toast.LENGTH_SHORT).show()
            }
        }
    }
}