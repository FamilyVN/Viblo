package com.asia.viblo.view.fragment.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asia.viblo.R
import com.asia.viblo.view.fragment.BaseFragment

class QuestionsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_questions, container, false)
    }
}
