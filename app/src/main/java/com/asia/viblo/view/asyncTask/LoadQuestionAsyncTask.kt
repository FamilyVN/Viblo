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
val cssQueryAvatarQuestions = "div.summary > div.asked-by > a > img"
val cssQueryNameQuestions = "div.summary > div.asked-by > div.text-small > a"
val cssQueryTimeQuestions = "div.summary > div.asked-by > div.text-small > span.text-muted"
val cssQueryTitleQuestions = "div.summary > div.q-title > a > h3"
val cssQueryStatusQuestions = "div.stats > div > div.question-stats > span.stats-item"
val cssQueryScoreQuestions = "div.stats > div > div.question-stats > div.points > span.text-muted"

@SuppressLint("StaticFieldLeak")
class LoadQuestionAsyncTask(onUpdateQuestionData: OnUpdateQuestionData) : AsyncTask<String, Void, List<Question>>() {
    private val mOnUpdateQuestionData = onUpdateQuestionData
    override fun doInBackground(vararg params: String?): List<Question> {
        val questionList: MutableList<Question> = arrayListOf()
        val baseUrl = params[0]
        val page: String = if (params.size == 1) "" else getPage(params[1])
        try {
            val document = Jsoup.connect(baseUrl + page).get()
            val elements = document?.select(cssQueryQuestions)
            for (element: Element in elements!!) {
                val question = Question()
//                val avatarSubject = element.select(cssQueryAvatarQuestions)
//                        .attr("srcset").split(",")
//                val avatar = avatarSubject[avatarSubject.size - 1]
//                question.avatar = avatar.substring(0, avatar.length - 3)
                val avatarSubject = element.select(cssQueryAvatarQuestions).first()
                question.avatar = avatarSubject?.attr("src")!!
                question.name = element.select(cssQueryNameQuestions).text()
                question.time = element.select(cssQueryTimeQuestions).text()
                question.title = element.select(cssQueryTitleQuestions).text()
                val questionsStatusSubject = element.select(cssQueryStatusQuestions)
                if (questionsStatusSubject != null) {
                    for ((index, statusSubject) in questionsStatusSubject.withIndex()) {
                        val viewSubject = statusSubject.getElementsByTag("span")
                        if (viewSubject != null) {
                            when (index) {
                                0 -> question.answers = viewSubject.text()
                                else -> question.views = viewSubject.text()
                            }
                        }
                    }
                }
                val scoreSubject = element.select(cssQueryScoreQuestions)
                if (scoreSubject.isNotEmpty()) {
                    question.score = scoreSubject.first().text()
                }
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

    private fun getPage(page: String?): String {
        var pageCheck = page
        try {
            if (page != null) {
                val pageMaxStr = SharedPrefs.instance[keyMaxPage, String::class.java]
                if (!TextUtils.isEmpty(pageMaxStr)) {
                    val pageMax = pageMaxStr.toInt()
                    val pagePresent = page.toInt()
                    if (pagePresent > pageMax) {
                        pageCheck = pageMaxStr
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        if (!TextUtils.isEmpty(pageCheck)) {
            SharedPrefs.instance.put(keyPagePresent, pageCheck)
        }
        return "?page=" + pageCheck
    }
}