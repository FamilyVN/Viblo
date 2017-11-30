package com.asia.viblo.view.fragment

/**
 * Created by FRAMGIA\vu.tuan.anh on 30/11/2017.
 */
interface OnUpdateData<T> {
    fun onUpdateData(data: T?)
    fun onUpdateDataList(dataList: MutableList<T>?)
}