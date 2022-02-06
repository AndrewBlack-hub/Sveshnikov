package com.example.tinkoffapp.features.devsLife.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.tinkoffapp.R
import com.example.tinkoffapp.core.view.utils.ListenersUtil
import com.example.tinkoffapp.databinding.ActivityDevsLifeBinding
import com.google.android.material.tabs.TabLayout
import org.koin.android.viewmodel.ext.android.viewModel

class DevsLifeActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityDevsLifeBinding::bind)
    private val viewModel: DevsLifeViewModel by viewModel()
    private val tabLabels = listOf(R.string.latest, R.string.top, R.string.hot)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devs_life)
        supportActionBar?.hide()
        observeRandomPost()
        initTabs()
        loadRandomPost()
        clickListeners()
        setTouchListeners()
    }

    private fun loadRandomPost() {
        viewModel.getRandomPost()
    }

    private fun observeRandomPost() {
        viewModel.randomPostLiveData.observe(this) { data ->
            if (data != null) {
                viewModel.randomPostList.add(data)
                binding.contentGroup.visibility = View.VISIBLE
                binding.errorGroup.visibility = View.INVISIBLE
                updateUi(description = data.description, gifUrl = data.gifUrl)
            } else {
                binding.contentGroup.visibility = View.INVISIBLE
                binding.prevPostBtn.visibility = View.INVISIBLE
                binding.errorGroup.visibility = View.VISIBLE
            }
        }
    }

    private fun initTabs() {
        tabLabels.forEach { label ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(label))
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                //загружать картинки из разделов табов
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun updateUi(description: String?, gifUrl: String?) {
        binding.textView.text = description
        setImageView(gifUrl)
    }

    private fun setImageView(gifUrl: String?) {
        Glide.with(applicationContext)
            .load(gifUrl)
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.INVISIBLE
                    return false
                }
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.INVISIBLE
                    return false
                }
            })
            .error(R.drawable.ic_error)
            .into(binding.imageView)
    }

    private fun clickListeners() {
        onClickNext()
        onClickPrev()
        onClickTryAgain()
    }

    private fun onClickTryAgain() {
        binding.tryAgainBtn.setOnClickListener {
            viewModel.getRandomPost()
        }
    }

    private fun onClickNext() {
        binding.nextPostBtn.setOnClickListener {
            if (viewModel.position < viewModel.randomPostList.size - 1) {
                val nextPost = viewModel.randomPostList[viewModel.position + 1]
                updateUi(description = nextPost?.description, gifUrl = nextPost?.gifUrl)
                viewModel.position++
            } else {
                binding.progressBar.visibility = View.VISIBLE
                viewModel.getRandomPost()
                viewModel.position++
            }
            if (viewModel.position > 0) binding.prevPostBtn.visibility = View.VISIBLE
        }
    }

    private fun onClickPrev() {
        binding.prevPostBtn.setOnClickListener {
            if (viewModel.position > 0) {
                val backPost = viewModel.randomPostList[viewModel.position - 1]
                updateUi(description = backPost?.description, gifUrl = backPost?.gifUrl)
                viewModel.position--
            }
            if (viewModel.position == 0) binding.prevPostBtn.visibility = View.INVISIBLE
        }
    }

    private fun setTouchListeners() {
        binding.nextPostBtn.setOnTouchListener(ListenersUtil.onTouch())
        binding.prevPostBtn.setOnTouchListener(ListenersUtil.onTouch())
    }
}