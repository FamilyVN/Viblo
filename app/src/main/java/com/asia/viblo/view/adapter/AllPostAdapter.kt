package com.asia.viblo.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asia.viblo.R
import com.asia.viblo.model.Post
import com.asia.viblo.utils.loadAvatar
import kotlinx.android.synthetic.main.item_topic.view.*

/**
 * Created by FRAMGIA\vu.tuan.anh on 27/10/2017.
 */
class AllPostAdapter(context: Context, postList: MutableList<Post>) : RecyclerView.Adapter<AllPostAdapter.ViewHolder>() {
    private val mPostList: MutableList<Post> = postList
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(mLayoutInflater.inflate(R.layout.item_topic, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = mPostList[position]
        holder.name.text = topic.name
        holder.time.text = topic.time
        holder.title.text = topic.title
        holder.views.text = topic.views
        holder.clips.text = topic.clips
        holder.comments.text = topic.comments
        holder.score.text = topic.score
        //
        if (TextUtils.isEmpty(topic.views)) {
            holder.views.visibility = View.INVISIBLE
        } else {
            holder.views.visibility = View.VISIBLE
        }
        //
        if (TextUtils.isEmpty(topic.clips)) {
            holder.clips.visibility = View.INVISIBLE
        } else {
            holder.clips.visibility = View.VISIBLE
        }
        //
        if (TextUtils.isEmpty(topic.comments)) {
            holder.comments.visibility = View.INVISIBLE
        } else {
            holder.comments.visibility = View.VISIBLE
        }
        //
        if (TextUtils.isEmpty(topic.score)) {
            holder.score.visibility = View.INVISIBLE
        } else {
            holder.score.visibility = View.VISIBLE
        }
        loadAvatar(holder.imageAvatar, topic.avatar)
    }

    override fun getItemCount(): Int {
        return mPostList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageAvatar = itemView.imageAvatar!!
        val name = itemView.name!!
        val time = itemView.time!!
        val title = itemView.title!!
        val score = itemView.score!!
        val views = itemView.views!!
        val clips = itemView.clips!!
        val comments = itemView.comments!!
    }
}