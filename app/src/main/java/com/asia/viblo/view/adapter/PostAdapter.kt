package com.asia.viblo.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asia.viblo.R
import com.asia.viblo.model.post.Post
import com.asia.viblo.utils.loadAvatar
import com.asia.viblo.view.activity.home.OnClickDetail
import kotlinx.android.synthetic.main.include_layout_views_clips_comments.view.*
import kotlinx.android.synthetic.main.item_post.view.*

/**
 * Created by FRAMGIA\vu.tuan.anh on 27/10/2017.
 */
class PostAdapter(context: Context, postList: MutableList<Post>, listener: OnClickDetail) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    private val mPostList: MutableList<Post> = postList
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mListener: OnClickDetail = listener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(mLayoutInflater.inflate(R.layout.item_post, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = mPostList[position]
        holder.name.text = post.name
        holder.time.text = post.time
        holder.title.text = post.title
        holder.views.text = post.views
        holder.clips.text = post.clips
        holder.comments.text = post.comments
        holder.score.text = post.score
        //
        if (TextUtils.isEmpty(post.views)) {
            holder.views.visibility = View.INVISIBLE
        } else {
            holder.views.visibility = View.VISIBLE
        }
        //
        if (TextUtils.isEmpty(post.clips)) {
            holder.clips.visibility = View.INVISIBLE
        } else {
            holder.clips.visibility = View.VISIBLE
        }
        //
        if (TextUtils.isEmpty(post.comments)) {
            holder.comments.visibility = View.INVISIBLE
        } else {
            holder.comments.visibility = View.VISIBLE
        }
        //
        if (TextUtils.isEmpty(post.score)) {
            holder.score.visibility = View.INVISIBLE
        } else {
            holder.score.visibility = View.VISIBLE
        }
        loadAvatar(holder.imageAvatar, post.avatar)
        holder.llRoot.setOnClickListener { mListener.onClickDetail(post.url) }
    }

    override fun getItemCount(): Int {
        return mPostList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llRoot = itemView.llRoot!!
        val imageAvatar = itemView.imageAvatar!!
        val name = itemView.name!!
        val time = itemView.time!!
        val title = itemView.title!!
        val score = itemView.layoutView.score!!
        val views = itemView.layoutView.views!!
        val clips = itemView.layoutView.clips!!
        val comments = itemView.layoutView.comments!!
    }
}