package com.asia.viblo.libs.recyclerview

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.asia.viblo.BR

/**
 * Created by FRAMGIA\vu.tuan.anh on 30/11/2017.
 */
class SingleAdapter<T>(context: Context, layoutRes: Int) : RecyclerView.Adapter<SingleAdapter.ViewHolder>() {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mLayoutRes = layoutRes
    private var mDataList: MutableList<T> = arrayListOf()
    private var mPresenter: Presenter? = null
    private var mDecorator: Decorator? = null
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(mLayoutInflater, mLayoutRes, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mDataList[position]
        holder.getBinding().setVariable(BR.position, position)
        holder.getBinding().setVariable(BR.data, data)
        holder.getBinding().setVariable(BR.listener, mPresenter)
        if (mDecorator != null) {
            mDecorator!!.decorator(holder, position, getItemViewType(position))
        }
        holder.getBinding().executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setData(dataList: MutableList<T>?) {
        mDataList.clear()
        addAll(dataList)
    }

    fun addAll(dataList: MutableList<T>?) {
        if (dataList == null) return
        mDataList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        mDataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        mDataList.clear()
        notifyDataSetChanged()
    }

    fun setDecorator(decorator: Decorator) {
        mDecorator = decorator
    }

    fun setPresenter(presenter: Presenter) {
        mPresenter = presenter
    }

    interface Presenter

    interface Decorator {
        fun decorator(holder: ViewHolder, position: Int, viewType: Int)
    }

    class ViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        private val mBinding = binding
        fun getBinding(): ViewDataBinding {
            return mBinding
        }
    }
}