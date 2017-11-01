package com.asia.viblo.view.activity.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.asia.viblo.R
import com.asia.viblo.model.Post
import com.asia.viblo.model.keyMaxPage
import com.asia.viblo.model.keyPagePresent
import com.asia.viblo.utils.SharedPrefs
import com.asia.viblo.view.adapter.AllPostAdapter
import com.asia.viblo.view.asyncTask.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mPostList: MutableList<Post> = arrayListOf()
    private lateinit var mAllPostAdapter: AllPostAdapter
    private var mPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerPost()
        updateNavigationBottom()
        initListener()
    }

    private fun updateNavigationBottom() {
        val pagePresentStr = SharedPrefs.instance[keyPagePresent, String::class.java]
        val pageMaxStr = SharedPrefs.instance[keyMaxPage, String::class.java]
        textPageBack.visibility = if (TextUtils.equals("1", pagePresentStr)) View.GONE else View.VISIBLE
        textPageNext.visibility = if (TextUtils.equals(pageMaxStr, pagePresentStr)) View.GONE else View.VISIBLE
        textPagePresent.text = pagePresentStr
    }

    private fun initRecyclerPost() {
        mAllPostAdapter = AllPostAdapter(this, mPostList)
        recyclerAllPost.adapter = mAllPostAdapter
        recyclerAllPost.layoutManager = LinearLayoutManager(this)
        loadData(baseUrlNewest, "")
    }

    private fun loadData(url: String, page: String) {
        mPostList.clear()
        if (TextUtils.isEmpty(page)) {
            mPostList.addAll(LoadPostAsyncTask().execute(url).get())
        } else {
            mPostList.addAll(LoadPostAsyncTask().execute(url, page).get())
        }
        mAllPostAdapter.notifyDataSetChanged()
    }

    private fun getLink(type: Int): String {
        return when (type) {
            0 -> baseUrlNewest
            1 -> baseUrlSeries
            2 -> baseUrlEditorsChoice
            3 -> baseUrlTrending
            4 -> baseUrlVideos
            else -> baseUrlNewest
        }
    }

    private fun initListener() {
        textPageNext.setOnClickListener {
            val pageNext = SharedPrefs.instance[keyPagePresent, String::class.java].toInt() + 1
            loadData(getLink(mPosition), pageNext.toString())
            updateNavigationBottom()
        }
        textPageBack.setOnClickListener {
            val pageBack = SharedPrefs.instance[keyPagePresent, String::class.java].toInt() - 1
            loadData(getLink(mPosition), pageBack.toString())
            updateNavigationBottom()
        }
        textPagePresent.setOnClickListener {
            loadData(getLink(mPosition), SharedPrefs.instance[keyPagePresent, String::class.java])
            updateNavigationBottom()
        }
    }
}
