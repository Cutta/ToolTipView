package com.cunoraz.tooltipview

import android.graphics.Canvas

internal interface ArrowLocation {

    fun configureDraw(view: TooltipView, canvas: Canvas)
}
