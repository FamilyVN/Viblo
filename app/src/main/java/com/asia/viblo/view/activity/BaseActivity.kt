package com.asia.viblo.view.activity

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.asia.viblo.utils.showProgressDialog

/**
 * Created by TuanAnh on 11/19/2017.
 */
abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var mProgressDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProgressDialog = showProgressDialog(this)
        loadData()
    }

    open fun loadData() {
        mProgressDialog.show()
    }
}