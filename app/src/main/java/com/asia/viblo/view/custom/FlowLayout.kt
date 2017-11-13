package com.asia.viblo.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created by anhtv on 13/11/2017.
 */
class FlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        ViewGroup(context, attrs, defStyle) {
    private val mAllViews: MutableList<MutableList<View>> = arrayListOf()
    private val mLineHeight: MutableList<Int> = arrayListOf()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val modeWidth = View.MeasureSpec.getMode(widthMeasureSpec)
        val sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        val modeHeight = View.MeasureSpec.getMode(heightMeasureSpec)
        var width = 0
        var height = 0
        var lineWidth = 0
        var lineHeight = 0
        val cCount = childCount
        for (i in 0 until cCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val lp = child.layoutParams as ViewGroup.MarginLayoutParams
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            if (lineWidth + childWidth > sizeWidth - paddingLeft - paddingRight) {
                width = Math.max(width, lineWidth)
                lineWidth = childWidth
                height += lineHeight
                lineHeight = childHeight
            } else {
                lineWidth += childWidth
                lineHeight = Math.max(lineHeight, childHeight)
            }
            if (i == cCount - 1) {
                width = Math.max(lineWidth, width)
                height += lineHeight
            }
        }
        setMeasuredDimension(if (modeWidth == View.MeasureSpec.EXACTLY) sizeWidth
        else width + paddingLeft + paddingRight,
                if (modeHeight == View.MeasureSpec.EXACTLY) sizeHeight
                else height + paddingTop + paddingBottom)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mAllViews.clear()
        mLineHeight.clear()
        val width = width
        var lineWidth = 0
        var lineHeight = 0
        val cCount = childCount
        var lineViews: MutableList<View> = arrayListOf()
        for (i in 0 until cCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as ViewGroup.MarginLayoutParams
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - paddingLeft - paddingRight) {
                mLineHeight.add(lineHeight)
                mAllViews.add(lineViews)
                lineWidth = 0
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin
                lineViews = arrayListOf()
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin)
            lineViews.add(child)
        }
        mLineHeight.add(lineHeight)
        mAllViews.add(lineViews)
        var left = paddingLeft
        var top = paddingTop
        val lineNum = mAllViews.size
        for (i in 0 until lineNum) {
            lineViews = mAllViews[i]
            lineHeight = mLineHeight[i]
            for (j in lineViews.indices) {
                val child = lineViews[j]
                if (child.visibility == View.GONE) {
                    continue
                }
                val lp = child.layoutParams as ViewGroup.MarginLayoutParams
                val lc = left + lp.leftMargin
                val tc = top + lp.topMargin
                val rc = lc + child.measuredWidth
                val bc = tc + child.measuredHeight
                child.layout(lc, tc, rc, bc)
                left += child.measuredWidth + lp.leftMargin + lp.rightMargin
            }
            left = paddingLeft
            top += lineHeight
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return ViewGroup.MarginLayoutParams(context, attrs)
    }
}