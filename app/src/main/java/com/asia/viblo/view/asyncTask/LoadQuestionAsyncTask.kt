package com.asia.viblo.view.asyncTask

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.text.TextUtils
import com.asia.viblo.model.keyMaxPage
import com.asia.viblo.model.keyPagePresent
import com.asia.viblo.model.questions.Question
import com.asia.viblo.utils.SharedPrefs
import com.asia.viblo.view.fragment.questions.OnUpdateQuestionData
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Created by FRAMGIA\vu.tuan.anh on 08/11/2017.
 */
val cssQueryPageQuestions = "div#__nuxt > div#app-container > div#main-content > div > " +
        "div.container > div.row > div.col-md-9 > div.question-list > ul.pagination"
val cssQueryQuestions = "div#__nuxt > div#app-container > div#main-content > div > div.container " +
        "> div.row > div.col-md-9 > div.question-list > div.question-item"

@SuppressLint("StaticFieldLeak")
class LoadQuestionAsyncTask(onUpdateQuestionData: OnUpdateQuestionData) : AsyncTask<String, Void, List<Question>>() {
    private val mOnUpdateQuestionData = onUpdateQuestionData
    override fun doInBackground(vararg params: String?): List<Question> {
        val questionList: MutableList<Question> = arrayListOf()
        val baseUrl = params[0]
        val page: String = if (params.size == 1) "" else "?page=" + params[1]
        try {
            val document = Jsoup.connect(baseUrl + page).get()
            val elements = document?.select(cssQueryQuestions)
            for (element: Element in elements!!) {
                val question = Question()
                questionList.add(question)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        val pageList: MutableList<String> = arrayListOf()
        try {
            val document = Jsoup.connect(baseUrl + page).get()
            val elements = document?.select(cssQueryPageQuestions)
            elements!!
                    .map { it.select("li") }
                    .forEach { data ->
                        data.asSequence()
                                .map { it.getElementsByTag("a").text() }
                                .filterNotTo(pageList) { TextUtils.isEmpty(it) }
                    }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        SharedPrefs.instance.put(keyMaxPage, if (pageList.isNotEmpty()) pageList.last() else "0")
        if (params.size == 1) {
            SharedPrefs.instance.put(keyPagePresent, "1")
        }
        return questionList
    }

    override fun onPostExecute(result: List<Question>?) {
        super.onPostExecute(result)
        mOnUpdateQuestionData.onUpdateQuestionData(result)
    }
}