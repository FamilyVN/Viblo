package com.asia.viblo.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.asia.viblo.R
import com.asia.viblo.view.fragment.OnSelectPage
import kotlinx.android.synthetic.main.dialog_select_page.view.*

/**
 * Created by FRAMGIA\vu.tuan.anh on 06/11/2017.
 */
class DialogSelectPage : LinearLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, null)
    constructor(context: Context, attrs: AttributeSet?, onSelectPage: OnSelectPage?) : this(context, attrs, 0, onSelectPage)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, onSelectPage: OnSelectPage?) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.dialog_select_page, this, true)
        btnOK.setOnClickListener {
            onSelectPage?.onSelectPage(editPage.text.toString())
        }
        btnCancel.setOnClickListener {
            onSelectPage?.onCancel()
        }
    }
}