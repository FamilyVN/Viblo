package com.asia.viblo.view.fragment.author

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.asia.viblo.R
import com.asia.viblo.model.BaseModel
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.view.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_author.*

class AuthorFragment : BaseFragment() {
    private lateinit var mBaseModel: BaseModel
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_author, container, false)
    }

    override fun getLink(type: Int): String {
        return baseUrlViblo + when (type) {
            1 -> "/series"
            else -> ""
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

    override fun onUpdateFeedBar(feedBarList: List<String>?) {
        if (feedBarList != null && feedBarList.isNotEmpty()) {
            if (spinnerAuthor == null) return
            spinnerAuthor.visibility = View.VISIBLE
            val feedBar: Array<String> = feedBarList.toTypedArray()
            val adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, feedBar)
            spinnerAuthor.adapter = adapter
            spinnerAuthor.dropDownVerticalOffset = spinnerAuthor.height
            spinnerAuthor.dropDownWidth = spinnerAuthor.width - resources.getDimensionPixelSize(R.dimen.size_20)
            spinnerAuthor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mPosition = position
                    loadData(getLink(mPosition))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        } else {
            spinnerAuthor.visibility = View.INVISIBLE
        }
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
