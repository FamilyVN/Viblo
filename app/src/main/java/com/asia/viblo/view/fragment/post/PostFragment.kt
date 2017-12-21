package com.asia.viblo.view.fragment.post

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.asia.viblo.R
import com.asia.viblo.libs.recyclerview.SingleAdapter
import com.asia.viblo.listener.BaseListener
import com.asia.viblo.model.*
import com.asia.viblo.model.constant.keyMaxPage
import com.asia.viblo.model.constant.keyPagePresent
import com.asia.viblo.model.post.Post
import com.asia.viblo.utils.SharedPrefs
import com.asia.viblo.view.asyncTask.post.LoadPostAsyncTask
import com.asia.viblo.view.fragment.BaseFragment
import com.asia.viblo.view.fragment.OnUpdateData
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.include_layout_next_back_page.*

class PostFragment : BaseFragment(), OnUpdateData<Post> {
    private lateinit var mPostAdapter: SingleAdapter<Post>
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_post, container, false)
    }

    override fun getLink(type: Int): String {
        return when (type) {
            0 -> baseUrlViblo
            1 -> baseUrlSeries
            2 -> baseUrlEditorsChoice
            3 -> baseUrlTrending
            4 -> baseUrlVideos
            else -> baseUrlViblo
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerPost()
    }

    private fun initRecyclerPost() {
        mPostAdapter = SingleAdapter(context, R.layout.item_recycler_post)
        mPostAdapter.setPresenter(BaseListener(activity))  // add listener for adapter
        recyclerPost.adapter = mPostAdapter
        recyclerPost.layoutManager = LinearLayoutManager(context)
    }

    private fun updateViewNextBackBottom() {
        val pagePresentStr = SharedPrefs.instance[keyPagePresent, String::class.java]
        val pageMaxStr = SharedPrefs.instance[keyMaxPage, String::class.java]
        textPageBack.visibility = if (TextUtils.equals("1", pagePresentStr)) View.GONE else View.VISIBLE
        textPageNext.visibility = if (TextUtils.equals(pageMaxStr, pagePresentStr)) View.GONE else View.VISIBLE
        viewNextBack.visibility = if (TextUtils.equals("0", pageMaxStr)) View.GONE else View.VISIBLE
        textPagePresent.text = getPagePresent(pagePresentStr, pageMaxStr)
    }

    override fun loadData(url: String, page: String) {
        super.loadData(url, page)
        if (TextUtils.isEmpty(page)) {
            LoadPostAsyncTask(this).execute(url)
        } else {
            LoadPostAsyncTask(this).execute(url, page)
        }
    }

    override fun onUpdateData(data: Post?) {
        // nothing
    }

    override fun onUpdateDataList(dataList: MutableList<Post>?) {
        mPostAdapter.setData(dataList) // set data for adapter
        mProgressDialog.dismiss()
        updateViewNextBackBottom()
    }

    override fun onUpdateFeedBar(feedBarList: MutableList<String>?) {
        if (feedBarList != null && feedBarList.isNotEmpty()) {
            if (spinnerPost == null) return
            spinnerPost.visibility = View.VISIBLE
            val feedBar: Array<String> = feedBarList.toTypedArray()
            val adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, feedBar)
            spinnerPost.adapter = adapter
            spinnerPost.dropDownVerticalOffset = spinnerPost.height
            spinnerPost.dropDownWidth = spinnerPost.width - resources.getDimensionPixelSize(R.dimen.size_20)
            spinnerPost.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mPosition = position
                    loadData(getLink(mPosition))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        } else {
            spinnerPost?.visibility = View.INVISIBLE
        }
    }
}