package com.example.tinkoffapp.features.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        viewModel.randomPostLiveData.observe(this) {
            binding.textView.text = it?.description ?: "пусто"
        }
        viewModel.getRandomPost()
    }
}