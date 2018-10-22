package com.cunoraz.tooltipview

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.annotation.*
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class TooltipView : LinearLayout {
    private var toolTipTitle: String? = null
    private var toolTipMessage: String? = null
    internal var arrowHeight: Int = 0
    internal var arrowWidth: Int = 0
    internal var cornerRadius: Int = 0
    internal var anchoredViewId: Int = 0
    internal var tooltipBgColor: Int = 0
    private var arrowLocation: ArrowLocation? = null
    private var arrowPositioning: Int = 0
    internal var tooltipPaint: Paint? = null
        private set
    internal var tooltipPath: Path? = null

    private var titleTextView: TextView? = null
    private var messageTextView: TextView? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        setWillNotDraw(false)
        val res = resources
        val a = context.obtainStyledAttributes(attrs, R.styleable.TooltipView, defStyle, 0)
        try {
            toolTipTitle = a.getString(R.styleable.TooltipView_toolTipTitle)
            toolTipMessage = a.getString(R.styleable.TooltipView_toolTipMessage)
            anchoredViewId = a.getResourceId(R.styleable.TooltipView_anchoredView, View.NO_ID)
            tooltipBgColor =
                    a.getColor(R.styleable.TooltipView_tooltipBgColor, res.getColor(R.color.default_tooltip_bg_color))
            cornerRadius = getDimension(a, R.styleable.TooltipView_cornerRadius, R.dimen.tooltip_default_corner_radius)
            arrowHeight = getDimension(a, R.styleable.TooltipView_arrowHeight, R.dimen.tooltip_default_arrow_height)
            arrowWidth = getDimension(a, R.styleable.TooltipView_arrowWidth, R.dimen.tooltip_default_arrow_width)
            arrowPositioning = a.getInteger(
                R.styleable.TooltipView_arrowLocation,
                res.getInteger(R.integer.tooltip_default_arrow_location)
            )
            arrowLocation = if (arrowPositioning == TOP) TopArrowLocation() else BottomArrowLocation()

        } finally {
            a.recycle()

            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            if (inflater != null) {
                inflater.inflate(R.layout.tooltip_with_title, this, true)
                initViews()
                setTexts(toolTipTitle, toolTipMessage)
            }
        }
    }

    private fun initViews() {
        titleTextView = findViewById(R.id.titleTextView)
        messageTextView = findViewById(R.id.messageTextView)
    }

    private fun setTexts(toolTipTitle: String?, toolTipMessage: String?) {
        titleTextView!!.text = toolTipTitle
        messageTextView!!.text = toolTipMessage
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (arrowPositioning == TOP) {
            setPadding(0, arrowHeight, 0, 0)
        } else {
            setPadding(0, 0, 0, arrowHeight)
        }
        Log.d("onAttachedToWindow", "onAttachedToWindow: ")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight + arrowHeight)
    }

    override fun invalidate() {
        super.invalidate()
        tooltipPath = null
        tooltipPaint = null
    }

    override fun onDraw(canvas: Canvas) {
        if (tooltipPath == null || tooltipPaint == null) {
            arrowLocation!!.configureDraw(this, canvas)
        }
        canvas.drawPath(tooltipPath!!, tooltipPaint!!)
        super.onDraw(canvas)
    }

    internal fun setPaint(paint: Paint) {
        this.tooltipPaint = paint
    }

    fun getArrowHeight(): Int {
        return arrowHeight
    }

    fun setArrowHeight(arrowHeight: Int) {
        this.arrowHeight = arrowHeight
        invalidate()
    }

    fun setArrowHeightResource(@DimenRes resId: Int) {
        arrowHeight = resources.getDimensionPixelSize(resId)
        invalidate()
    }

    fun getArrowWidth(): Int {
        return arrowWidth
    }

    fun setArrowWidth(arrowWidth: Int) {
        this.arrowWidth = arrowWidth
        invalidate()
    }

    fun setArrowWidthResource(@DimenRes resId: Int) {
        arrowWidth = resources.getDimensionPixelSize(resId)
        invalidate()
    }

    fun getCornerRadius(): Int {
        return cornerRadius
    }

    fun setCornerRadius(cornerRadius: Int) {
        this.cornerRadius = cornerRadius
        invalidate()
    }

    fun setCornerRadiusResource(@DimenRes resId: Int) {
        cornerRadius = resources.getDimensionPixelSize(resId)
        invalidate()
    }

    fun getAnchoredViewId(): Int {
        return anchoredViewId
    }

    fun setAnchoredViewId(@IdRes anchoredViewId: Int) {
        this.anchoredViewId = anchoredViewId
        invalidate()
    }

    fun getTooltipBgColor(): Int {
        return tooltipBgColor
    }

    fun setTooltipBgColor(tooltipBgColor: Int) {
        this.tooltipBgColor = tooltipBgColor
        invalidate()
    }

    fun setArrowPositioning(arrowPositioning: Int) {
        this.arrowPositioning = arrowPositioning
        invalidate()
    }

    private fun getDimension(
        typedArray: TypedArray,
        @StyleableRes styleableId: Int,
        @DimenRes defaultDimension: Int
    ): Int {
        var result = typedArray.getDimensionPixelSize(styleableId, NOT_PRESENT)
        if (result == NOT_PRESENT) {
            result = resources.getDimensionPixelSize(defaultDimension)
        }
        return result
    }


    companion object {

        val TOP = 0
        val BOTTOM = 1

        private val NOT_PRESENT = Integer.MIN_VALUE
    }
}
