package com.asia.viblo.view.asyncTask.question

import android.os.AsyncTask
import com.asia.viblo.model.questions.QuestionDetail
import com.asia.viblo.view.activity.questions.OnUpdateQuestionDetail

/**
 * Created by anhtv on 24/11/2017.
 */
class LoadQuestionDetailAsyncTask(onUpdateQuestionDetail: OnUpdateQuestionDetail) : AsyncTask<String, Void, QuestionDetail>() {
    private val mOnUpdateQuestionDetail: OnUpdateQuestionDetail = onUpdateQuestionDetail
    override fun doInBackground(vararg params: String?): QuestionDetail {
        val questionDetail = QuestionDetail()
        return questionDetail
    }

    override fun onPostExecute(result: QuestionDetail?) {
        super.onPostExecute(result)
        mOnUpdateQuestionDetail.onUpdateQuestionDetail(result)
    }
}