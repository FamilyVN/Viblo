package com.asia.viblo.view.asyncTask.tag

import android.os.AsyncTask
import com.asia.viblo.model.tag.TagDetail
import com.asia.viblo.utils.getDocument
import com.asia.viblo.view.fragment.OnUpdateData

/**
 * Created by FRAMGIA\vu.tuan.anh on 13/12/2017.
 */
class LoadTagsAsyncTask(onUpdateData: OnUpdateData<TagDetail>) : AsyncTask<String, Void, TagDetail>() {
    private val mOnUpdateData = onUpdateData
    override fun doInBackground(vararg params: String?): TagDetail {
        val tagDetail = TagDetail()
        val baseUrl = params[0]
        try {
            val document = getDocument(baseUrl)
            val body = document?.body() ?: return tagDetail
            val element = body
            // header

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return tagDetail
    }

    override fun onPostExecute(result: TagDetail?) {
        super.onPostExecute(result)
        mOnUpdateData.onUpdateData(result)
    }
}