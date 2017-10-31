package com.asia.viblo

import android.app.Application

import com.google.gson.Gson

/**
 * Created by FRAMGIA\vu.tuan.anh on 31/10/2017.
 */
class App : Application() {
    var gSon: Gson? = null
        private set

    override fun onCreate() {
        super.onCreate()
        mSelf = this
        gSon = Gson()
    }

    companion object {

        private var mSelf: App? = null

        fun self(): App? {
            return mSelf
        }
    }
}
