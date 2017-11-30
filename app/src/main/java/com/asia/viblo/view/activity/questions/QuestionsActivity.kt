package com.asia.viblo.view.activity.questions

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.asia.viblo.R
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.constant.extraUrl
import com.asia.viblo.model.questions.QuestionDetail
import com.asia.viblo.view.asyncTask.question.LoadQuestionDetailAsyncTask

class QuestionsActivity : AppCompatActivity(), OnUpdateQuestionDetail {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        val baseUrl = baseUrlViblo + intent.getStringExtra(extraUrl)
        LoadQuestionDetailAsyncTask(this).execute(baseUrl)
    }

    override fun onUpdateQuestionDetail(questionDetail: QuestionDetail?) {
    }
}
