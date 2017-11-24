package com.asia.viblo.view.fragment.author

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.asia.viblo.R
import com.asia.viblo.model.BaseModel
import com.asia.viblo.model.author.AuthorDetail
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.extraData
import com.asia.viblo.model.extraUrl
import com.asia.viblo.model.post.Post
import com.asia.viblo.utils.showProgressDialog
import com.asia.viblo.view.activity.author.AuthorActivity
import com.asia.viblo.view.activity.post.PostDetailActivity
import com.asia.viblo.view.activity.home.OnClickDetail
import com.asia.viblo.view.activity.home.OnClickTag
import com.asia.viblo.view.activity.series.SeriesActivity
import com.asia.viblo.view.activity.webview.WebViewActivity
import com.asia.viblo.view.adapter.PostAdapter
import com.asia.viblo.view.asyncTask.author.LoadAuthorAsyncTask
import com.asia.viblo.view.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_author.*

class AuthorFragment : BaseFragment(), OnUpdateAuthorData, OnClickTag, OnClickDetail {
    private lateinit var mBaseModel: BaseModel
    private val mPostList: MutableList<Post> = arrayListOf()
    private lateinit var mPostAdapter: PostAdapter
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
        mPostAdapter = PostAdapter(context, mPostList, this, this)
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

    override fun onOpenDetail(url: String, isVideo: Boolean) {
        val intent = when {
            url.contains("/s/") -> {
                Intent(context, SeriesActivity::class.java)
            }
            isVideo -> {
                Intent(context, WebViewActivity::class.java)
            }
            else -> {
                Intent(context, PostDetailActivity::class.java)
            }
        }
        intent.putExtra(extraUrl, url)
        startActivity(intent)
    }

    override fun onOpenAuthor(baseModel: BaseModel) {
        val intent = Intent(context, AuthorActivity::class.java)
        intent.putExtra(extraData, baseModel)
        startActivity(intent)
    }

    override fun onOpenTag(tagUrl: String) {
        Log.d("TAG.PostFragment", "tagUrl = " + baseUrlViblo + tagUrl)
        Toast.makeText(context, tagUrl, Toast.LENGTH_SHORT).show()
    }

    override fun onUpdateAuthorData(authorDetail: AuthorDetail?) {
        mProgressDialog.dismiss()
        if (authorDetail != null && authorDetail.postList.size > 0) {
            mPostList.clear()
            mPostList.addAll(authorDetail.postList)
            mPostAdapter.notifyDataSetChanged()
            recyclerContent.visibility = View.VISIBLE
            llNothingHere.visibility = View.GONE
        } else {
            recyclerContent.visibility = View.GONE
            llNothingHere.visibility = View.VISIBLE
        }
    }

    override fun onUpdateFeedBar(feedBarList: List<String>?) {
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
