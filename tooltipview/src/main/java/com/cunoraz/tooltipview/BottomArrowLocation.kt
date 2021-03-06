package com.cunoraz.tooltipview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.View

import android.graphics.Path.Direction

internal class BottomArrowLocation : ArrowLocation {

    override fun configureDraw(view: TooltipView, canvas: Canvas) {
        view.tooltipPath = Path()
        val rectF = RectF(canvas.clipBounds)
        rectF.bottom -= view.arrowHeight.toFloat()

        with(view.tooltipPath!!) {
            addRoundRect(rectF, view.cornerRadius.toFloat(), view.cornerRadius.toFloat(), Direction.CW)
            val middle = ArrowAlignmentHelper.calculateArrowMidPoint(view, rectF)
            moveTo(middle, view.height.toFloat())
            val arrowDx = view.arrowWidth / 2
            lineTo(middle - arrowDx, rectF.bottom)
            lineTo(middle + arrowDx, rectF.bottom)
            close()
        }

        view.setPaint(Paint(Paint.ANTI_ALIAS_FLAG))
        view.tooltipPaint!!.color = view.tooltipBgColor
    }
}
