package com.asia.viblo.model.series

import com.asia.viblo.model.BaseModel
import com.asia.viblo.model.post.Post

/**
 * Created by TuanAnh on 11/19/2017.
 */
class SeriesDetail : BaseModel() {
    var seriesList: MutableList<Post> = arrayListOf()
    var posts = ""
    var clips = ""
}