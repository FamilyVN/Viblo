package com.asia.viblo.view.fragment.discussions

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asia.viblo.R
import com.asia.viblo.libs.recyclerview.SingleAdapter
import com.asia.viblo.listener.BaseListener
import com.asia.viblo.model.baseUrlDiscussion
import com.asia.viblo.model.discussion.Discussion
import com.asia.viblo.view.asyncTask.discussion.LoadDiscussionAsyncTask
import com.asia.viblo.view.fragment.BaseFragment
import com.asia.viblo.view.fragment.OnUpdateData
import kotlinx.android.synthetic.main.fragment_discussions.*

class DiscussionsFragment : BaseFragment(), OnUpdateData<Discussion> {
    private lateinit var mDiscussionAdapter: SingleAdapter<Discussion>
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_discussions, container, false)
    }

    override fun getLink(type: Int): String {
        return baseUrlDiscussion
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerDiscussions()
        loadData(getLink(0))
    }

    private fun initRecyclerDiscussions() {
        mDiscussionAdapter = SingleAdapter(context, R.layout.item_recycler_discussion)
        mDiscussionAdapter.setPresenter(BaseListener(activity))  // add listener for adapter
        recyclerDiscussions.adapter = mDiscussionAdapter
        recyclerDiscussions.layoutManager = LinearLayoutManager(context)
    }

    override fun loadData(url: String, page: String) {
        super.loadData(url, page)
        if (TextUtils.isEmpty(page)) {
            LoadDiscussionAsyncTask(this).execute(url)
        } else {
            LoadDiscussionAsyncTask(this).execute(url, page)
        }
    }

    override fun onUpdateData(data: Discussion?) {
        // nothing
    }

    override fun onUpdateDataList(dataList: MutableList<Discussion>?) {
        mDiscussionAdapter.setData(dataList)
        mProgressDialog.dismiss()
    }
}
