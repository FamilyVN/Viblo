package com.asia.viblo.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.asia.viblo.utils.showProgressDialog

/**
 * Created by FRAMGIA\vu.tuan.anh on 03/11/2017.
 */
open class BaseFragment : Fragment() {
    lateinit var mProgressDialog: Dialog
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProgressDialog = showProgressDialog(context)
    }
}