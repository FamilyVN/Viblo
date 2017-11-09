package com.asia.viblo.view.fragment.author

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asia.viblo.R
import com.asia.viblo.model.BaseModel
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.view.fragment.BaseFragment

class AuthorFragment : BaseFragment() {
    private lateinit var mBaseModel: BaseModel
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_author, container, false)
    }

    override fun getLink(type: Int): String {
        return baseUrlViblo + when (type) {
            1 -> "series"
            else -> baseUrlViblo
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            mBaseModel = arguments.getSerializable(PARAM_AUTHOR) as BaseModel
            initViews()
        }
    }

    private fun initViews() {
    }

    companion object {
        private val PARAM_AUTHOR = "param_author"
        fun newInstance(baseModel: BaseModel): AuthorFragment {
            val fragment = AuthorFragment()
            val args = Bundle()
            args.putSerializable(AuthorFragment.PARAM_AUTHOR, baseModel)
            fragment.arguments = args
            return fragment
        }
    }
}
