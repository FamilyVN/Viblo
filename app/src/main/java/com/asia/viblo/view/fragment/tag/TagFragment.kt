package com.asia.viblo.view.fragment.tag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asia.viblo.R
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.utils.showProgressDialog
import com.asia.viblo.view.fragment.BaseFragment

/**
 * Created by FRAMGIA\vu.tuan.anh on 13/12/2017.
 */
class TagFragment : BaseFragment() {
    private lateinit var mTag: String
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_author, container, false)
    }

    override fun getLink(type: Int): String {
        return baseUrlViblo + mTag + when (type) {
            1 -> "/series"
            2 -> "/questions"
            3 -> "/followers"
            else -> ""
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        if (arguments != null) {
            mTag = arguments.getString(TagFragment.PARAM_TAG)
            mProgressDialog = showProgressDialog(context)
            initListener()
        }
    }

    companion object {
        private val PARAM_TAG = "param_tag"
        fun newInstance(tag: String): TagFragment {
            val fragment = TagFragment()
            val args = Bundle()
            args.putSerializable(TagFragment.PARAM_TAG, tag)
            fragment.arguments = args
            return fragment
        }
    }
}