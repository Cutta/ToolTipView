package com.cunoraz.tooltipview

import android.graphics.RectF
import android.view.View

internal object ArrowAlignmentHelper {

    fun calculateArrowMidPoint(view: TooltipView, rectF: RectF): Float {
        var middle = rectF.width() / 2
        if (view.anchoredViewId != View.NO_ID) {
            val anchoredView = (view.parent as View).findViewById<View>(view.anchoredViewId)
            middle += (anchoredView.x
                    + (anchoredView.width / 2)
                    - (view.x)
                    - (view.width / 2).toFloat())
        }
        return middle
    }
}
