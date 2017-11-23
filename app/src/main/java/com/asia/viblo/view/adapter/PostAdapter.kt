package com.asia.viblo.view.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asia.viblo.R
import com.asia.viblo.model.post.Post
import com.asia.viblo.utils.getSpannableStringFirst
import com.asia.viblo.utils.loadAvatar
import com.asia.viblo.utils.setTags
import com.asia.viblo.view.activity.home.OnClickDetail
import com.asia.viblo.view.activity.home.OnClickTag
import kotlinx.android.synthetic.main.include_layout_stats.view.*
import kotlinx.android.synthetic.main.include_layout_status.view.*
import kotlinx.android.synthetic.main.item_post.view.*

/**
 * Created by FRAMGIA\vu.tuan.anh on 27/10/2017.
 */
class PostAdapter(context: Context, postList: MutableList<Post>, onClickDetail: OnClickDetail, onClickTag: OnClickTag) :
        RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    private val mPostList: MutableList<Post> = postList
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mOnClickDetail: OnClickDetail = onClickDetail
    private val mOnClickTag: OnClickTag = onClickTag
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(mLayoutInflater.inflate(R.layout.item_post, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = mPostList[position]
        holder.name.text = post.name
        holder.time.text = post.time
        // layout status
        holder.views.text = post.views
        holder.clips.text = post.clips
        holder.comments.text = post.comments
        holder.posts.text = post.posts
        holder.score.text = post.score
        //
        if (TextUtils.isEmpty(post.views)) {
            holder.views.visibility = View.INVISIBLE
            holder.layoutStatus.visibility = View.GONE
            holder.name.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, android.R.color.black))
            holder.name.typeface = Typeface.DEFAULT_BOLD
        } else {
            holder.views.visibility = View.VISIBLE
            holder.layoutStatus.visibility = View.VISIBLE
            holder.name.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.colorName))
            holder.name.typeface = Typeface.DEFAULT
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
        if (TextUtils.isEmpty(post.posts)) {
            holder.posts.visibility = View.INVISIBLE
        } else {
            holder.posts.visibility = View.VISIBLE
        }
        //
        if (TextUtils.isEmpty(post.score)) {
            holder.score.visibility = View.INVISIBLE
        } else {
            holder.score.visibility = View.VISIBLE
        }
        // layout stats
        holder.reputation.text = post.reputation
        holder.followers.text = post.followers
        holder.post.text = post.post
        if (TextUtils.isEmpty(post.reputation)) {
            holder.reputation.visibility = View.INVISIBLE
            holder.layoutStats.visibility = View.GONE
        } else {
            holder.reputation.visibility = View.VISIBLE
            holder.layoutStats.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(post.followers)) {
            holder.followers.visibility = View.INVISIBLE
        } else {
            holder.followers.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(post.post)) {
            holder.post.visibility = View.INVISIBLE
        } else {
            holder.post.visibility = View.VISIBLE
        }
        // isVideo
        if (post.isVideo) {
            holder.title.text = getSpannableStringFirst(
                    holder.itemView.context, R.drawable.ic_play, post.title)
        } else {
            holder.title.text = post.title
            holder.title.visibility = if (TextUtils.isEmpty(post.title)) View.GONE else View.VISIBLE
        }
        // avatar
        loadAvatar(holder.imageAvatar, post.avatar)
        // tags
        setTags(holder.flowLayout, post.tags, post.tagUrlList, mOnClickTag)
        // listener
        holder.llRoot.setOnClickListener { mOnClickDetail.onOpenDetail(post.postUrl, post.isVideo) }
        holder.imageAvatar.setOnClickListener { mOnClickDetail.onOpenAuthor(post) }
        holder.name.setOnClickListener { mOnClickDetail.onOpenAuthor(post) }
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
        val layoutStatus = itemView.layoutStatus!!
        val score = itemView.layoutStatus.score!!
        val views = itemView.layoutStatus.views!!
        val clips = itemView.layoutStatus.clips!!
        val comments = itemView.layoutStatus.comments!!
        val posts = itemView.layoutStatus.posts!!
        val flowLayout = itemView.flowLayout!!
        val reputation = itemView.layoutStats.reputation!!
        val followers = itemView.layoutStats.followers!!
        val post = itemView.layoutStats.post!!
        val layoutStats = itemView.layoutStats!!
    }
}