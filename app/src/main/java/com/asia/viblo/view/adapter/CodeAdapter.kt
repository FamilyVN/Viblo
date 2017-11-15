package com.asia.viblo.view.adapter

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.asia.viblo.R
import kotlinx.android.synthetic.main.item_text_content_default.view.*

/**
 * Created by FRAMGIA\vu.tuan.anh on 15/11/2017.
 */
class CodeAdapter(context: Context, codeList: MutableList<String>) :
        RecyclerView.Adapter<CodeAdapter.ViewHolder>() {
    private val mContext: Context = context
    private val mCodeList: MutableList<String> = codeList
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(mLayoutInflater.inflate(R.layout.item_text_content_default, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtCode.setText(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(mCodeList[position], Html.FROM_HTML_MODE_COMPACT) else
            Html.fromHtml(mCodeList[position]), TextView.BufferType.SPANNABLE)
        holder.txtCode.setTextColor(ContextCompat.getColor(mContext, android.R.color.white))
    }

    override fun getItemCount(): Int {
        return mCodeList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCode = itemView.textContent!!
    }
}