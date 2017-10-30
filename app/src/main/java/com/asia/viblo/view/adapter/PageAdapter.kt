package com.asia.viblo.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asia.viblo.R
import kotlinx.android.synthetic.main.item_page.view.*

/**
 * Created by FRAMGIA\vu.tuan.anh on 30/10/2017.
 */
class PageAdapter(context: Context, pageList: MutableList<String>) : RecyclerView.Adapter<PageAdapter.ViewHolder>() {
    private val mPageList: MutableList<String> = pageList
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun getItemCount(): Int {
        return mPageList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(mLayoutInflater.inflate(R.layout.item_page, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pageNumber.text = mPageList[position]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pageNumber = itemView.pageNumber!!
    }
}