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

private const val LATEST = "latest"
private const val HOT = "hot"
private const val TOP = "top"
private const val RANDOM = "random"

class DevsLifeActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityDevsLifeBinding::bind)
    private val viewModel: DevsLifeViewModel by viewModel()
    private val tabLabels = listOf(R.string.latest, R.string.top, R.string.hot, R.string.random)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devs_life)
        supportActionBar?.hide()
        observeRandomPost()
        observePostsList()
        initTabs()
        loadPostsList()
        clickListeners()
        setTouchListeners()
    }

    private fun loadPostsList() {
        viewModel.getPostsList(type = viewModel.type, page = viewModel.page.toString())
    }

    private fun loadRandomPost() {
        viewModel.getRandomPost()
    }

    private fun observePostsList() {
        viewModel.postsListLiveData.observe(this) { data ->
            if (!data.isNullOrEmpty()) {//Успешный запрос
                viewModel.postsList.addAll(data)
                showContentView()
                updateUi(
                    description = viewModel.postsList[viewModel.position]?.description,
                    gifUrl = viewModel.postsList[viewModel.position]?.gifUrl
                )
            } else if (data != null && data.isEmpty()) {//data.size == 0
                //С сервера пришел пустой список
                binding.errorIc.setImageDrawable(getDrawable(R.drawable.ic_error))
                binding.errorGroup.visibility = View.VISIBLE
                binding.errorInfo.text = getString(R.string.empty_list_info)
                binding.contentGroup.visibility = View.INVISIBLE
                binding.prevPostBtn.visibility = View.INVISIBLE
            } else {// data == null
                //Проблемы с интернетом
                internetHasProblem()
            }
        }
    }

    private fun observeRandomPost() {
        viewModel.randomPostLiveData.observe(this) { data ->
            if (data != null) {
                viewModel.randomPostList.add(data)
                showContentView()
                updateUi(description = data.description, gifUrl = data.gifUrl)
            } else {
                internetHasProblem()
            }
        }
    }

    private fun showContentView() {
        binding.contentGroup.visibility = View.VISIBLE
        binding.errorGroup.visibility = View.INVISIBLE
    }

    private fun internetHasProblem() {
        binding.contentGroup.visibility = View.INVISIBLE
        binding.prevPostBtn.visibility = View.INVISIBLE
        binding.errorGroup.visibility = View.VISIBLE
    }

    private fun initTabs() {
        tabLabels.forEach { label ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(label))
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.prevPostBtn.visibility = View.INVISIBLE
                viewModel.postsList.clear()
                viewModel.position = 0
                viewModel.page = 0
                viewModel.type = when (tab?.position) {
                    0 -> LATEST
                    1 -> TOP
                    2 -> HOT //На момент написания по этому запросу приходит пустой список, с остальными все нормально
                    else -> RANDOM
                }
                if (viewModel.type != RANDOM) loadPostsList()
                else loadRandomPost()
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
        if (viewModel.type == RANDOM) {
            if (viewModel.randomPostList.size > 0 && viewModel.position > 0) binding.prevPostBtn.visibility = View.VISIBLE
            else binding.prevPostBtn.visibility = View.INVISIBLE
        } else {
            if (viewModel.postsList.size > 0 && viewModel.position > 0) binding.prevPostBtn.visibility = View.VISIBLE
            else binding.prevPostBtn.visibility = View.INVISIBLE
        }
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
            if (viewModel.type != RANDOM) loadPostsList()
            else loadRandomPost()
        }
    }

    private fun onClickNext() {
        binding.nextPostBtn.setOnClickListener {
            if (viewModel.type == RANDOM) onClickNextRandom()
            else onClickNextForTabs()
        }
    }

    private fun onClickNextRandom() {
        if (viewModel.position < viewModel.randomPostList.size - 1) {//если позиция != последнему элементу в списке
            showNextPostInCache()
        } else {// позиция равна последнему элементу в списке
            // делаем запрос на новый контент
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getRandomPost()
            viewModel.position++
        }
        showPrevBtnIfNeed()
    }

    private fun showNextPostInCache() {
        val nextPost =
            if (viewModel.type == RANDOM) viewModel.randomPostList[viewModel.position + 1]
            else viewModel.postsList[viewModel.position + 1]
        updateUi(description = nextPost?.description, gifUrl = nextPost?.gifUrl)
        viewModel.position++
    }

    private fun onClickNextForTabs() {
        if (viewModel.position < viewModel.postsList.size - 1) {
            showNextPostInCache()
        } else {
            viewModel.page++
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getPostsList(type = viewModel.type, page = viewModel.page.toString())
            viewModel.position++
        }
        showPrevBtnIfNeed()
    }

    private fun showPrevBtnIfNeed() {
        if (viewModel.position > 0) binding.prevPostBtn.visibility = View.VISIBLE
    }

    private fun onClickPrev() {
        binding.prevPostBtn.setOnClickListener {
            if (viewModel.position > 0) {
                val prevPost =
                    if (viewModel.type == RANDOM) viewModel.randomPostList[viewModel.position - 1]
                    else viewModel.postsList[viewModel.position - 1]
                updateUi(description = prevPost?.description, gifUrl = prevPost?.gifUrl)
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