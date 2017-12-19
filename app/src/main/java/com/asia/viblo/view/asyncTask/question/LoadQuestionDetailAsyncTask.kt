package com.asia.viblo.view.asyncTask.question

import android.os.AsyncTask
import com.asia.viblo.model.questions.QuestionDetail
import com.asia.viblo.view.fragment.OnUpdateData

/**
 * Created by anhtv on 24/11/2017.
 */
class LoadQuestionDetailAsyncTask(onUpdateData: OnUpdateData<QuestionDetail>) : AsyncTask<String, Void, QuestionDetail>() {
    private val mOnUpdateData = onUpdateData
    override fun doInBackground(vararg params: String?): QuestionDetail {
        val questionDetail = QuestionDetail()
        return questionDetail
    }

    override fun onPostExecute(result: QuestionDetail?) {
        super.onPostExecute(result)
        mOnUpdateData.onUpdateData(result)
    }
}