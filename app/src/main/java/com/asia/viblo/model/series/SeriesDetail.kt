package com.asia.viblo.model.series

import com.asia.viblo.model.post.Post
import com.asia.viblo.model.post.PostDetail

/**
 * Created by TuanAnh on 11/19/2017.
 */
class SeriesDetail : PostDetail() {
    var seriesList: MutableList<Post> = arrayListOf()
}