package com.asia.viblo.view.activity.author

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.asia.viblo.R
import com.asia.viblo.model.BaseModel
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.extraData
import com.asia.viblo.utils.loadAvatar
import com.asia.viblo.view.fragment.author.AuthorFragment
import kotlinx.android.synthetic.main.activity_author.*

class AuthorActivity : AppCompatActivity() {
    private lateinit var mBaseModel: BaseModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author)
        mBaseModel = intent.getSerializableExtra(extraData) as BaseModel
        initViews()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameAuthor, AuthorFragment.newInstance(mBaseModel))
                .commit()
    }

    private fun initViews() {
        loadAvatar(imageAvatar, mBaseModel.avatar)
        txtName.text = mBaseModel.name
        txtAuthor.text = mBaseModel.authorUrl.replace(baseUrlViblo + "/u/", "@")
    }
}
