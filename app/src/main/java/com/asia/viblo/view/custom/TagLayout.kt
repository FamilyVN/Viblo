package com.asia.viblo.view.custom

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.asia.viblo.R
import kotlinx.android.synthetic.main.tag_layout_default.view.*

/**
 * Created by FRAMGIA\vu.tuan.anh on 02/11/2017.
 */
class TagLayout : ViewGroup {
    private var mMaxWidth: Int = 0
    private var mMesWidth: Int = 0
    var spaceHorizontal: Int = 0
    var spaceVertical: Int = 0
    var rowCount: Int = 0
        private set
    private var mLayoutInflater: LayoutInflater? = null
    private var mTagArray: SparseArray<View>? = null
    private var mOnTagRemovedListener: OnTagRemovedListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        mMaxWidth = resources.displayMetrics.widthPixels
        mLayoutInflater = LayoutInflater.from(context)
        mTagArray = SparseArray()
    }

    fun setMesWidth(mesWidth: Int) {
        mMesWidth = mesWidth
    }

    fun setOnTagRemovedListener(onTagRemovedListener: OnTagRemovedListener) {
        mOnTagRemovedListener = onTagRemovedListener
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        var curWidth: Int
        var curHeight: Int
        var curLeft: Int
        var curTop: Int
        var maxHeight: Int
        val childLeft = paddingLeft
        val childTop = paddingTop
        if (mMesWidth == 0) mMesWidth = measuredWidth
        val childRight = mMesWidth - paddingRight
        val childBottom = measuredHeight - paddingBottom
        val childWidth = childRight - childLeft
        val childHeight = childBottom - childTop
        maxHeight = 0
        curLeft = childLeft
        curTop = childTop
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) continue
            child.measure(View.MeasureSpec.makeMeasureSpec(childWidth, View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(childHeight, View.MeasureSpec.AT_MOST))
            curWidth = child.measuredWidth
            curHeight = child.measuredHeight
            if (i != 0 && curLeft + curWidth > childRight) {
                curLeft = childLeft
                curTop += maxHeight + spaceVertical
                maxHeight = 0
            }
            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight)
            if (maxHeight < curHeight) {
                maxHeight = curHeight
            }
            curLeft += curWidth + spaceHorizontal
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val count = childCount
        var maxHeight = 0
        var childState = 0
        var mLeftWidth: Int
        var rowCount = 0
        var curRowWidth = 0
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) continue
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            curRowWidth += child.measuredWidth
            mLeftWidth = mMesWidth * rowCount + curRowWidth
            if (mMesWidth == 0) break
            if (i == 0)
                maxHeight = Math.max(maxHeight, child.measuredHeight)
            else if ((mLeftWidth + spaceHorizontal) / mMesWidth > rowCount) {
                maxHeight += child.measuredHeight + spaceVertical
                rowCount++
                curRowWidth = child.measuredWidth
            } else {
                maxHeight = Math.max(maxHeight, child.measuredHeight)
                curRowWidth += spaceHorizontal
            }
            childState = View.combineMeasuredStates(childState, child.measuredState)
        }
        maxHeight = Math.max(maxHeight, suggestedMinimumHeight)
        setMeasuredDimension(View.resolveSizeAndState(mMaxWidth, widthMeasureSpec, childState),
                View.resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState shl View.MEASURED_HEIGHT_STATE_SHIFT))
        this.rowCount = rowCount
    }

    fun addTagView(id: Int, name: String) {
        if (mTagArray?.get(id) != null) {
            val view = mTagArray?.get(id)
            val textView = view?.findViewById<View>(R.id.tagContent) as TextView
            textView.text = name
            return
        }
        val view = mLayoutInflater!!.inflate(R.layout.tag_layout_default, null, false)
        view.tag = id
        tagContent.text = name
        addView(view)
        mTagArray?.put(id, view)
        view.setOnClickListener { removeTagView(id) }
    }

    fun removeTagView(id: Int) {
        if (mTagArray?.get(id) != null) {
            removeView(mTagArray?.get(id))
            mOnTagRemovedListener?.onTagRemoved(id)
        }
    }

    fun clearTags() {
        removeAllViews()
        mTagArray!!.clear()
    }

    interface OnTagRemovedListener {
        fun onTagRemoved(id: Int)
    }
}
