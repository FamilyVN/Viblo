package com.asia.viblo.view.activity.questions

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.asia.viblo.R
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.constant.extraUrl
import com.asia.viblo.model.questions.QuestionDetail
import com.asia.viblo.view.asyncTask.question.LoadQuestionDetailAsyncTask
import com.asia.viblo.view.fragment.OnUpdateData

class QuestionsActivity : AppCompatActivity(), OnUpdateData<QuestionDetail> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        val baseUrl = baseUrlViblo + intent.getStringExtra(extraUrl)
        LoadQuestionDetailAsyncTask(this).execute(baseUrl)
    }

    override fun onUpdateData(data: QuestionDetail?) {
    }

    override fun onUpdateDataList(dataList: MutableList<QuestionDetail>?) {
        // nothing
    }
}
