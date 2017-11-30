package com.asia.viblo.view.activity.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import com.asia.viblo.R
import com.asia.viblo.model.constant.extraSearch
import com.asia.viblo.view.activity.search.SearchActivity
import com.asia.viblo.view.fragment.discussions.DiscussionsFragment
import com.asia.viblo.view.fragment.post.PostFragment
import com.asia.viblo.view.fragment.questions.QuestionsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mSearchViewListener: SearchView.OnQueryTextListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSearchViewListener()
        initBottomNavigationView()
    }

    private fun initSearchViewListener() {
        mSearchViewListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val intent = Intent(applicationContext, SearchActivity::class.java)
                intent.putExtra(extraSearch, query)
                startActivity(intent)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        }
    }

    private fun initBottomNavigationView() {
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
        bottomNavigationHome.selectedItemId = R.id.itemPost
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_action_bar, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(mSearchViewListener)
        return true
    }
}
