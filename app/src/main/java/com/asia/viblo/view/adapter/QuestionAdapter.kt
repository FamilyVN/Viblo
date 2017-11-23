package com.asia.viblo.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asia.viblo.R
import com.asia.viblo.model.questions.Question
import com.asia.viblo.utils.loadAvatar
import com.asia.viblo.utils.setTags
import com.asia.viblo.view.activity.home.OnClickDetail
import com.asia.viblo.view.activity.home.OnClickTag
import kotlinx.android.synthetic.main.include_layout_status_questions.view.*
import kotlinx.android.synthetic.main.item_question.view.*

/**
 * Created by FRAMGIA\vu.tuan.anh on 08/11/2017.
 */
class QuestionAdapter(context: Context, questionList: MutableList<Question>, onClickDetail: OnClickDetail, onClickTag: OnClickTag) :
        RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
    private val mQuestionList: MutableList<Question> = questionList
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mOnClickDetail: OnClickDetail = onClickDetail
    private val mOnClickTag: OnClickTag = onClickTag
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(mLayoutInflater.inflate(R.layout.item_question, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = mQuestionList[position]
        holder.name.text = question.name
        holder.time.text = question.time
        holder.title.text = question.title
        holder.answers.text = question.answers
        holder.score.text = question.score
        holder.views.text = question.views
        holder.comments.text = question.comments
        loadAvatar(holder.avatar, question.avatar)
        // tags
        setTags(holder.flowLayout, question.tags, question.tagUrlList, mOnClickTag)
        // listener
        holder.llRoot.setOnClickListener { mOnClickDetail.onOpenDetail(question.questionUrl, false) }
        holder.avatar.setOnClickListener { mOnClickDetail.onOpenAuthor(question) }
        holder.name.setOnClickListener { mOnClickDetail.onOpenAuthor(question) }
    }

    override fun getItemCount(): Int {
        return mQuestionList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar = itemView.imageAvatar!!
        val name = itemView.name!!
        val time = itemView.time!!
        val title = itemView.title!!
        val answers = itemView.layoutStatus.answers!!
        val score = itemView.layoutStatus.score!!
        val views = itemView.layoutStatus.views!!
        val comments = itemView.layoutStatus.comments!!
        val flowLayout = itemView.flowLayout!!
        val llRoot = itemView.llRoot!!
    }
}