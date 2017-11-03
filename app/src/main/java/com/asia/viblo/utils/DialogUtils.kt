package com.asia.viblo.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.asia.viblo.R


/**
 * Created by FRAMGIA\vu.tuan.anh on 03/11/2017.
 */
fun showProgressDialog(context: Context): Dialog {
    val progressDialog = Dialog(context, android.R.style.Animation_Dialog)
    val view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null)
    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    progressDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    progressDialog.setContentView(view)
    return progressDialog
}