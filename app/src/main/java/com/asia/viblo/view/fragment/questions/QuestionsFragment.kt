package com.asia.viblo.view.fragment.questions

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.asia.viblo.R
import com.asia.viblo.model.*
import com.asia.viblo.model.questions.Question
import com.asia.viblo.utils.SharedPrefs
import com.asia.viblo.view.activity.home.OnClickDetail
import com.asia.viblo.view.activity.home.OnClickTag
import com.asia.viblo.view.adapter.QuestionAdapter
import com.asia.viblo.view.asyncTask.question.LoadQuestionAsyncTask
import com.asia.viblo.view.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_questions.*
import kotlinx.android.synthetic.main.include_layout_next_back_page.*

class QuestionsFragment : BaseFragment(), OnClickDetail, OnUpdateQuestionData, OnClickTag {
    private val mQuestionList: MutableList<Question> = arrayListOf()
    private lateinit var mQuestionAdapter: QuestionAdapter
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_questions, container, false)
    }

    override fun getLink(type: Int): String {
        return when (type) {
            1 -> baseUrlQuestionUnsolved
            2 -> baseUrlQuestionFollowings
            3 -> baseUrlQuestionMyClips
            else -> baseUrlQuestionNewest
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerQuestions()
    }

    private fun initRecyclerQuestions() {
        mQuestionAdapter = QuestionAdapter(context, mQuestionList, this)
        recyclerQuestions.adapter = mQuestionAdapter
        recyclerQuestions.layoutManager = LinearLayoutManager(context)
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
            LoadQuestionAsyncTask(this).execute(url)
        } else {
            LoadQuestionAsyncTask(this).execute(url, page)
        }
    }

    override fun onOpenPostDetail(url: String) {
    }

    override fun onOpenAuthor(baseModel: BaseModel) {
    }

    override fun onOpenTag(tagUrl: String) {
    }

    override fun onUpdateQuestionData(questionList: List<Question>?) {
        if (questionList != null) {
            mQuestionList.clear()
            mQuestionList.addAll(questionList)
            mQuestionAdapter.notifyDataSetChanged()
        }
        mProgressDialog.dismiss()
        updateViewNextBackBottom()
    }

    override fun onUpdateFeedBar(feedBarList: List<String>?) {
        if (feedBarList != null && feedBarList.isNotEmpty()) {
            if (spinnerQuestions == null) return
            spinnerQuestions.visibility = View.VISIBLE
            val feedBar: Array<String> = feedBarList.toTypedArray()
            val adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, feedBar)
            spinnerQuestions.adapter = adapter
            spinnerQuestions.dropDownVerticalOffset = spinnerQuestions.height
            spinnerQuestions.dropDownWidth = spinnerQuestions.width - resources.getDimensionPixelSize(R.dimen.size_20)
            spinnerQuestions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mPosition = position
                    loadData(getLink(mPosition))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        } else {
            spinnerQuestions.visibility = View.INVISIBLE
        }
    }
}
