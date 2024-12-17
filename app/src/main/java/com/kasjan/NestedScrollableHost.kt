package com.kasjan

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class NestedScrollableHost @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val child = getChildAt(0)
        if (child is RecyclerView) {
            return super.onInterceptTouchEvent(ev) && canChildScrollHorizontally(ev)
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun canChildScrollHorizontally(event: MotionEvent): Boolean {
        val dx = event.x - (event.downTime)
        return dx > 0 && ViewCompat.canScrollHorizontally(this, -1) || dx < 0 && ViewCompat.canScrollHorizontally(this, 1)
    }
}
