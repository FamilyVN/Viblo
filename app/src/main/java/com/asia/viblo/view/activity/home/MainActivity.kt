package com.asia.viblo.view.activity.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.asia.viblo.R
import com.asia.viblo.view.fragment.discussions.DiscussionsFragment
import com.asia.viblo.view.fragment.post.PostFragment
import com.asia.viblo.view.fragment.questions.QuestionsFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBottomNavigationView()
    }

    private fun initBottomNavigationView() {
        bottomNavigationHome.selectedItemId = 0
        bottomNavigationHome.setOnNavigationItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.itemPost -> PostFragment()
                R.id.itemQuestions -> QuestionsFragment()
                R.id.itemDiscussion -> DiscussionsFragment()
                else -> PostFragment()
            }
            supportFragmentManager.beginTransaction().replace(R.id.frameHome, fragment).commit()
            true
        }
    }
}
