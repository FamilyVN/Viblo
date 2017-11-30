package com.asia.viblo.view.fragment.author

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
import com.asia.viblo.model.BaseModel
import com.asia.viblo.model.author.AuthorDetail
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.post.Post
import com.asia.viblo.utils.showProgressDialog
import com.asia.viblo.view.asyncTask.author.LoadAuthorAsyncTask
import com.asia.viblo.view.fragment.BaseFragment
import com.asia.viblo.view.fragment.OnUpdateData
import kotlinx.android.synthetic.main.fragment_author.*

class AuthorFragment : BaseFragment(), OnUpdateData<AuthorDetail> {
    private lateinit var mBaseModel: BaseModel
    private lateinit var mPostAdapter: SingleAdapter<Post>
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_author, container, false)
    }

    override fun getLink(type: Int): String {
        return baseUrlViblo + mBaseModel.authorUrl + when (type) {
            1 -> "/series"
            2 -> "/questions"
            3 -> "/clips/posts"
            4 -> "/following"
            5 -> "/followers"
            6 -> "/following-tags"
            else -> ""
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        if (arguments != null) {
            mBaseModel = arguments.getSerializable(PARAM_AUTHOR) as BaseModel
            mProgressDialog = showProgressDialog(context)
            initListener()
            initSpinner(mBaseModel.authorUrl)
            initRecyclerContent()
        }
    }

    private fun initRecyclerContent() {
        mPostAdapter = SingleAdapter(context, R.layout.item_post)
        mPostAdapter.setPresenter(BaseListener(activity))
        recyclerContent.adapter = mPostAdapter
        recyclerContent.layoutManager = LinearLayoutManager(context)
    }

    override fun loadData(url: String, page: String) {
        super.loadData(url, page)
        if (TextUtils.isEmpty(page)) {
            LoadAuthorAsyncTask(this).execute(url)
        } else {
            LoadAuthorAsyncTask(this).execute(url, page)
        }
    }

    override fun onUpdateData(data: AuthorDetail?) {
        mProgressDialog.dismiss()
        if (data != null && data.postList.size > 0) {
            mPostAdapter.setData(data.postList)
            recyclerContent.visibility = View.VISIBLE
            llNothingHere.visibility = View.GONE
        } else {
            recyclerContent.visibility = View.GONE
            llNothingHere.visibility = View.VISIBLE
        }
    }

    override fun onUpdateDataList(dataList: MutableList<AuthorDetail>?) {
    }

    override fun onUpdateFeedBar(feedBarList: MutableList<String>?) {
        if (feedBarList != null && feedBarList.isNotEmpty()) {
            if (spinnerAuthor == null) return
            spinnerAuthor.visibility = View.VISIBLE
            val feedBar: Array<String> = feedBarList.toTypedArray()
            val adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, feedBar)
            spinnerAuthor.adapter = adapter
            spinnerAuthor.dropDownVerticalOffset = spinnerAuthor.height
            spinnerAuthor.dropDownWidth = spinnerAuthor.width - resources.getDimensionPixelSize(R.dimen.size_20)
            spinnerAuthor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mPosition = position
                    loadData(getLink(mPosition))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        } else {
            spinnerAuthor.visibility = View.INVISIBLE
        }
    }

    companion object {
        private val PARAM_AUTHOR = "param_author"
        fun newInstance(baseModel: BaseModel): AuthorFragment {
            val fragment = AuthorFragment()
            val args = Bundle()
            args.putSerializable(AuthorFragment.PARAM_AUTHOR, baseModel)
            fragment.arguments = args
            return fragment
        }
    }
}
