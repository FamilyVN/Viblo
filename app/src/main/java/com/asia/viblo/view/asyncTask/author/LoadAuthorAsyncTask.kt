package com.asia.viblo.view.asyncTask.author

import android.os.AsyncTask
import com.asia.viblo.model.author.AuthorDetail
import com.asia.viblo.view.fragment.author.OnUpdateAuthorData

/**
 * Created by anhtv on 22/11/2017.
 */
class LoadAuthorAsyncTask(onUpdateAuthorData: OnUpdateAuthorData) : AsyncTask<String, Void, AuthorDetail>() {
    private val mOnUpdateAuthorData: OnUpdateAuthorData = onUpdateAuthorData
    override fun doInBackground(vararg params: String?): AuthorDetail {
        var authorDetail = AuthorDetail()
        return authorDetail
    }

    override fun onPostExecute(result: AuthorDetail?) {
        super.onPostExecute(result)
        mOnUpdateAuthorData.onUpdateAuthorData(result)
    }
}