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
        observePostsList()//По запросу "https://developerslife.ru/hot/0?json=true" приходит пустой список
        initTabs()
        loadPostsList()
        clickListeners()
        setTouchListeners()
    }

    private fun loadPostsList() {
        when (viewModel.type) {
            LATEST -> {
                viewModel.getPostsList(
                    type = viewModel.type,
                    page = viewModel.latestPage.toString()
                )
            }
            TOP -> {
                viewModel.getPostsList(
                    type = viewModel.type,
                    page = viewModel.topPage.toString()
                )
            }
            HOT -> {
                viewModel.getPostsList(
                    type = viewModel.type,
                    page = viewModel.hotPage.toString()
                )
            }
            else -> {//random
                loadRandomPost()
            }
        }
    }

    private fun loadRandomPost() {
        viewModel.getRandomPost()
    }

    private fun observePostsList() {
        viewModel.postsListLiveData.observe(this) { data ->
            if (!data.isNullOrEmpty()) {//Успешный запрос
                showContentView()
                when (viewModel.type) {
                    LATEST -> {
                        viewModel.latestList.addAll(data)
                        updateUi(
                            description = viewModel
                                .latestList[viewModel.latestListPosition]?.description,
                            gifUrl = viewModel.latestList[viewModel.latestListPosition]?.gifUrl
                        )
                    }
                    TOP -> {
                        viewModel.topList.addAll(data)
                        updateUi(
                            description = viewModel.topList[viewModel.topListPosition]?.description,
                            gifUrl = viewModel.topList[viewModel.topListPosition]?.gifUrl
                        )
                    }
                    HOT -> {
                        viewModel.hotList.addAll(data)
                        updateUi(
                            description = viewModel.hotList[viewModel.hotListPosition]?.description,
                            gifUrl = viewModel.hotList[viewModel.hotListPosition]?.gifUrl
                        )
                    }
                }
            } else if (data != null && data.isEmpty()) { //data.size == 0
                //С сервера пришел пустой список
                dataEmptyState()
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

    private fun dataEmptyState() {
        binding.errorIc.setImageDrawable(getDrawable(R.drawable.ic_error))
        binding.errorGroup.visibility = View.VISIBLE
        binding.errorInfo.text = getString(R.string.empty_list_info)
        binding.contentGroup.visibility = View.INVISIBLE
        binding.prevPostBtn.visibility = View.INVISIBLE
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
                viewModel.type = when (tab?.position) {
                    0 -> LATEST
                    1 -> TOP
                    2 -> HOT //На момент написания по этому запросу приходит пустой список, с остальными все нормально
                    else -> RANDOM
                }
                when (viewModel.type) {
                    LATEST -> {
                        if (!viewModel.latestList.isNullOrEmpty())
                            updateUi(
                                description = viewModel
                                    .latestList[viewModel.latestListPosition]?.description,
                                gifUrl = viewModel.latestList[viewModel.latestListPosition]?.gifUrl
                            )
                        else {
                            loadPostsList()
                        }
                    }
                    TOP -> {
                        if (!viewModel.topList.isNullOrEmpty())
                            updateUi(
                                description = viewModel
                                    .topList[viewModel.topListPosition]?.description,
                                gifUrl = viewModel.topList[viewModel.topListPosition]?.gifUrl
                            )
                        else {
                            loadPostsList()
                        }
                    }
                    HOT -> {
                        if (!viewModel.hotList.isNullOrEmpty())
                            updateUi(
                                description = viewModel
                                    .hotList[viewModel.hotListPosition]?.description,
                                gifUrl = viewModel.hotList[viewModel.hotListPosition]?.gifUrl
                            )
                        else {
                            loadPostsList()
                        }
                    }
                    RANDOM -> {
                        if (!viewModel.randomPostList.isNullOrEmpty())
                            updateUi(
                                description = viewModel
                                    .randomPostList[viewModel.position]?.description,
                                gifUrl = viewModel.randomPostList[viewModel.position]?.gifUrl
                            )
                        else {
                            loadPostsList()
                        }
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun updateUi(description: String?, gifUrl: String?) {
        binding.textView.text = description
        setImageView(gifUrl)
        showContentView()
        when (viewModel.type) {
            LATEST -> {
                if (viewModel.latestList.size > 0 && viewModel.latestListPosition > 0)
                    binding.prevPostBtn.visibility = View.VISIBLE
                else
                    binding.prevPostBtn.visibility = View.INVISIBLE }
            TOP -> {
                if (viewModel.topList.size > 0 && viewModel.topListPosition > 0)
                    binding.prevPostBtn.visibility = View.VISIBLE
                else
                    binding.prevPostBtn.visibility = View.INVISIBLE }
            HOT -> {
                if (viewModel.hotList.size > 0 && viewModel.hotListPosition > 0)
                    binding.prevPostBtn.visibility = View.VISIBLE
                else
                    binding.prevPostBtn.visibility = View.INVISIBLE }
            RANDOM -> {
                if (viewModel.randomPostList.size > 0 && viewModel.position > 0)
                    binding.prevPostBtn.visibility = View.VISIBLE
                else
                    binding.prevPostBtn.visibility = View.INVISIBLE
            }
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
            when (viewModel.type) {
                LATEST -> { onClickNextLatest() }
                TOP -> { onClickNextTop() }
                HOT -> { onClickNextHot() }
                RANDOM -> { onClickNextRandom() }
            }
        }
    }

    private fun onClickNextRandom() {
        //если позиция != последнему элементу в списке
        if (viewModel.position < viewModel.randomPostList.size - 1) {
            showNextPostInCache()
        } else {// позиция равна последнему элементу в списке
            // делаем запрос на новый контент
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getRandomPost()
            viewModel.position++
        }
        showPrevBtnIfNeed()
    }

    private fun onClickNextTop() {
        //если позиция != последнему элементу в списке
        if (viewModel.topListPosition < viewModel.topList.size - 1) {
            showNextPostInCache()
        } else {// позиция равна последнему элементу в списке
            // делаем запрос на новый контент
            viewModel.topPage++
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getPostsList(type = viewModel.type, page = viewModel.topPage.toString())
            viewModel.topListPosition++
        }
        showPrevBtnIfNeed()
    }

    private fun showNextPostInCache() {
        val nextPost = when (viewModel.type) {
            LATEST -> {
                viewModel.latestListPosition++
                viewModel.latestList[viewModel.latestListPosition]
            }
            TOP -> {
                viewModel.topListPosition++
                viewModel.topList[viewModel.topListPosition]
            }
            HOT -> {
                viewModel.hotListPosition++
                viewModel.hotList[viewModel.hotListPosition]
            }
            else -> {
                viewModel.position++
                viewModel.randomPostList[viewModel.position]
            }
        }
        updateUi(description = nextPost?.description, gifUrl = nextPost?.gifUrl)
    }

    private fun onClickNextLatest() {
        if (viewModel.latestListPosition < viewModel.latestList.size - 1) {
            showNextPostInCache()
        } else {
            viewModel.latestPage++
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getPostsList(type = viewModel.type, page = viewModel.latestPage.toString())
            viewModel.latestListPosition++
        }
        showPrevBtnIfNeed()
    }

    private fun onClickNextHot() {
        if (viewModel.hotListPosition < viewModel.hotList.size - 1) {
            showNextPostInCache()
        } else {
            viewModel.hotPage++
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getPostsList(type = viewModel.type, page = viewModel.hotPage.toString())
            viewModel.hotListPosition++
        }
        showPrevBtnIfNeed()
    }

    private fun showPrevBtnIfNeed() {
        when (viewModel.type) {
            LATEST -> {
                if (viewModel.latestListPosition > 0) binding.prevPostBtn.visibility = View.VISIBLE
            }
            TOP -> {
                if (viewModel.topListPosition > 0) binding.prevPostBtn.visibility = View.VISIBLE
            }
            HOT -> {
                if (viewModel.hotListPosition > 0) binding.prevPostBtn.visibility = View.VISIBLE
            }
            else -> {//random
                if (viewModel.position > 0) binding.prevPostBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun onClickPrev() {
        binding.prevPostBtn.setOnClickListener {
            val prevPost = when (viewModel.type) {
                LATEST -> {
                    viewModel.latestListPosition--
                    viewModel.latestList[viewModel.latestListPosition]
                }
                TOP -> {
                    viewModel.topListPosition--
                    viewModel.topList[viewModel.topListPosition]
                }
                HOT -> {
                    viewModel.hotListPosition--
                    viewModel.hotList[viewModel.hotListPosition]
                }
                else -> {//random
                    viewModel.position--
                    viewModel.randomPostList[viewModel.position]
                }
            }
            updateUi(description = prevPost?.description, gifUrl = prevPost?.gifUrl)
            showPrevBtnIfNeed()
        }
    }

    private fun setTouchListeners() {
        binding.nextPostBtn.setOnTouchListener(ListenersUtil.onTouch())
        binding.prevPostBtn.setOnTouchListener(ListenersUtil.onTouch())
    }
}