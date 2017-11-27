package com.asia.viblo.view.custom

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.asia.viblo.R
import com.asia.viblo.utils.getSize
import com.asia.viblo.utils.loadAvatar
import com.asia.viblo.view.activity.home.OnClickComment
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by anhtv on 27/11/2017.
 */
class TagListOneLine : LinearLayout {
    private val inflater: LayoutInflater
    private var itemLayout: Int = 0
    private var fixedSize: Int = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflater = LayoutInflater.from(context)
        val array = getContext().obtainStyledAttributes(attrs, R.styleable.TagListOneLine, 0, 0)
        try {
            itemLayout = array.getResourceId(R.styleable.TagListOneLine_item_layout, -1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        array.recycle()
    }

    fun setFixedSize(fixedSize: Int) {
        this.fixedSize = fixedSize
    }

    private fun getViewWidth(): Int {
        return if (fixedSize == 0) width else fixedSize
    }

    private fun getItemLayoutId(): Int {
        return if (itemLayout != -1) itemLayout else R.layout.item_comment_default //default
    }

    open fun setImageList(commentList: MutableList<String>, layoutView: Int, numberMore: String,
                          onClickComment: OnClickComment) {
        itemLayout = layoutView
        setImageList(commentList, numberMore, onClickComment)
    }

    open fun setImageList(commentList: MutableList<String>, numberMore: String,
                          onClickComment: OnClickComment) {
        val size = getSize(R.dimen.size_20)
        val moreTextView = inflater.inflate(
                R.layout.item_comment_text_default, this, false) as TextView
        moreTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        moreTextView.measure(moreTextView.width, moreTextView.height)
        var currentWidth = 0
        for ((i, comment) in commentList.withIndex()) {
            val moreTextWidth: Int = if (i < commentList.size - 1) moreTextView.measuredWidth else 0
            val imageView = inflater.inflate(getItemLayoutId(), this, false) as CircleImageView
            loadAvatar(imageView, comment)
            imageView.measure(size, size)
            imageView.setOnClickListener {
                onClickComment.onOpenAuthorComment("")
            }
            if (commentList.size == 1) {
                addView(imageView)
                break
            }
            currentWidth += imageView.measuredWidth + if (i == 0) 0 else 2 * getSize(R.dimen.size_4)
            if (currentWidth + moreTextWidth <= getViewWidth()) {
                if (i != 0) {
                    val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    layoutParams.leftMargin = getSize(R.dimen.size_4)
                    layoutParams.topMargin = getSize(R.dimen.size_4)
                    imageView.layoutParams = layoutParams
                }
                addView(imageView)
            } else {
                if (i == 0) {
                    addView(imageView)
                } else {
                    layoutParams.width = getViewWidth() + moreTextWidth + getSize(R.dimen.size_4)
                    var sizeComment = commentList.size - childCount
                    var moreText = sizeComment.toString()
                    if (!TextUtils.isEmpty(numberMore)) {
                        try {
                            sizeComment += numberMore.toInt()
                            moreText = if (sizeComment > 9) "9+" else sizeComment.toString()
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                        }
                    }
                    moreTextView.text = moreText
                    addView(moreTextView)
                }
                break
            }
        }
        moreTextView.setOnClickListener {
            onClickComment.onOpenAllAuthorComment(null)
        }
    }
}