package com.asia.viblo.view.activity

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
import com.asia.viblo.view.asyncTask.AllPostAsyncTask
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mPostList: MutableList<Post> = arrayListOf()
    private lateinit var mAllPostAdapter: AllPostAdapter

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
        loadData("")
    }

    private fun loadData(page: String) {
        mPostList.clear()
        if (TextUtils.isEmpty(page)) {
            mPostList.addAll(AllPostAsyncTask().execute().get())
        } else {
            mPostList.addAll(AllPostAsyncTask().execute(page).get())
        }
        mAllPostAdapter.notifyDataSetChanged()
    }

    private fun initListener() {
        textPageNext.setOnClickListener {
            val pageNext = SharedPrefs.instance[keyPagePresent, String::class.java].toInt() + 1
            loadData(pageNext.toString())
            updateNavigationBottom()
        }
        textPageBack.setOnClickListener {
            val pageBack = SharedPrefs.instance[keyPagePresent, String::class.java].toInt() - 1
            loadData(pageBack.toString())
            updateNavigationBottom()
        }
        textPagePresent.setOnClickListener {
            loadData(SharedPrefs.instance[keyPagePresent, String::class.java])
            updateNavigationBottom()
        }
    }
}
