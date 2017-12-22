package com.asia.viblo.view.asyncTask.discussion

import android.os.AsyncTask
import com.asia.viblo.model.discussion.Discussion
import com.asia.viblo.utils.getDocument
import com.asia.viblo.view.fragment.OnUpdateData
import org.jsoup.nodes.Element

/**
 * Created by FRAMGIA\vu.tuan.anh on 21/12/2017.
 */
private val cssQueryDiscussion = "div#__nuxt > div#app-container > div#main-content > " +
        "div > div#comment-section > div.item"
private val cssQueryDiscussionAvatar = "a > img"

class LoadDiscussionAsyncTask(onUpdateData: OnUpdateData<Discussion>) : AsyncTask<String, Void, MutableList<Discussion>>() {
    private val mOnUpdateData = onUpdateData
    override fun doInBackground(vararg params: String?): MutableList<Discussion> {
        val discussionList: MutableList<Discussion> = arrayListOf()
        val baseUrl = params[0]
        val page: String = if (params.size == 1) "" else "?page=" + params[1]
        try {
            val document = getDocument(baseUrl + page)
            val elements = document?.select(cssQueryDiscussion)
            for (element: Element in elements!!) {
                val discussion = Discussion()
                discussion.avatar = element.select(cssQueryDiscussionAvatar)?.attr("src")!!
                discussionList.add(discussion)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return discussionList
    }

    override fun onPostExecute(result: MutableList<Discussion>?) {
        super.onPostExecute(result)
        mOnUpdateData.onUpdateDataList(result)
    }
}