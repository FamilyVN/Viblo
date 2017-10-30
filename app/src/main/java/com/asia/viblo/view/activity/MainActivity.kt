package com.asia.viblo.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.asia.viblo.R
import com.asia.viblo.model.Post
import com.asia.viblo.view.adapter.AllPostAdapter
import com.asia.viblo.view.asyncTask.AllPostAsyncTask
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mPostList: MutableList<Post> = arrayListOf()
    private val mPageList: MutableList<String> = arrayListOf()
    private lateinit var mAllPostAdapter: AllPostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerPost()
        initRecyclerPage()
    }

    private fun initRecyclerPost() {
        mAllPostAdapter = AllPostAdapter(this, mPostList)
        recyclerAllPost.adapter = mAllPostAdapter
        recyclerAllPost.layoutManager = LinearLayoutManager(this)
        mPostList.addAll(AllPostAsyncTask().execute().get())
        mAllPostAdapter.notifyDataSetChanged()
    }

    private fun initRecyclerPage() {
        recyclerPage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
}
