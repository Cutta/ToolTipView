package com.cunoraz.tooltipview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


class TooltipView : LinearLayout {
    private var toolTipTitle: String? = null
    private var toolTipMessage: String? = null

    private lateinit var arrowLocation: ArrowLocation
    private lateinit var titleTextView: TextView
    private lateinit var messageTextView: TextView
    private lateinit var closeButton: ImageView

    private var arrowPosition: Int = 0
    private var closeButtonGravity: Int = 0
    private var closeButtonVisibility: Int = 0
    internal var arrowHeight: Int = 0
    internal var arrowWidth: Int = 0
    internal var cornerRadius: Int = 0
    internal var anchoredViewId: Int = 0
    internal var tooltipBgColor: Int = 0
    internal var tooltipPaint: Paint? = null
    internal var tooltipPath: Path? = null


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
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TooltipView, defStyle, 0)
        try {
            with(typedArray) {
                toolTipTitle = getString(R.styleable.TooltipView_toolTipTitle)
                toolTipMessage = getString(R.styleable.TooltipView_toolTipMessage)
                anchoredViewId = getResourceId(R.styleable.TooltipView_anchoredView, View.NO_ID)
                tooltipBgColor = getColor(
                    R.styleable.TooltipView_tooltipBgColor,
                    ContextCompat.getColor(context, R.color.default_tooltip_bg_color)
                )
                cornerRadius = getDimension(
                    typedArray,
                    R.styleable.TooltipView_cornerRadius,
                    R.dimen.tooltip_default_corner_radius
                )
                arrowHeight = getDimension(
                    typedArray,
                    R.styleable.TooltipView_arrowHeight,
                    R.dimen.tooltip_default_arrow_height
                )
                arrowWidth = getDimension(
                    typedArray,
                    R.styleable.TooltipView_arrowWidth,
                    R.dimen.tooltip_default_arrow_width
                )
                arrowPosition = getInteger(
                    R.styleable.TooltipView_arrowLocation,
                    res.getInteger(R.integer.tooltip_default_arrow_location)
                )
                arrowLocation = if (arrowPosition == LOCATION_TOP) TopArrowLocation() else BottomArrowLocation()
                closeButtonGravity = getInteger(
                    R.styleable.TooltipView_closeButtonGravity,
                    res.getInteger(R.integer.tooltip_default_close_button_gravity)
                )
                closeButtonVisibility = getInteger(R.styleable.TooltipView_closeButtonVisibility, VISIBILITY_VISIBLE)
            }
        } finally {
            typedArray.recycle()

            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.tooltip_with_title, this, true)

            initViews()

        }
    }

    private fun initViews() {
        titleTextView = findViewById(R.id.titleTextView)
        messageTextView = findViewById(R.id.messageTextView)
        closeButton = findViewById(R.id.closeButton)


        setCloseButtonProperties()
        setTexts()
    }

    private fun setCloseButtonProperties() {
        if (closeButtonVisibility == VISIBILITY_GONE) {
            closeButton.visibility = View.GONE
            return
        }

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = when (closeButtonGravity) {

            GRAVITY_TOP -> Gravity.TOP

            GRAVITY_CENTER -> Gravity.CENTER_VERTICAL

            GRAVITY_BOTTOM -> Gravity.BOTTOM

            else -> Gravity.TOP
        }

        closeButton.layoutParams = layoutParams
    }

    private fun setTexts() {
        if (toolTipTitle == null) {
            titleTextView.visibility = View.GONE
        } else {
            titleTextView.text = toolTipTitle
            titleTextView.visibility = View.VISIBLE
        }

        if (toolTipMessage == null) {
            messageTextView.visibility = View.GONE
        } else {
            messageTextView.text = toolTipMessage
            messageTextView.visibility = View.VISIBLE
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (arrowPosition == LOCATION_TOP) {
            setPadding(0, arrowHeight, 0, 0)
        } else {
            setPadding(0, 0, 0, arrowHeight)
        }
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
            arrowLocation.configureDraw(this, canvas)
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

    fun setArrowPosition(arrowPosition: Int) {
        arrowLocation = if (arrowPosition == LOCATION_TOP) TopArrowLocation() else BottomArrowLocation()
        if (arrowPosition == LOCATION_TOP) { // magic number will be dynamic
            (findViewById<LinearLayout>(R.id.rootLayout)).setPadding(32, arrowHeight + 32, 32, 0)
        } else {
            (findViewById<LinearLayout>(R.id.rootLayout)).setPadding(32, 32, 32, arrowHeight)
        }
        invalidate()
    }

    fun setTitle(title: String) {
        titleTextView.text = title
        titleTextView.visibility = View.VISIBLE
    }

    fun getTitle() = toolTipTitle

    fun setMessage(message: String) {
        messageTextView.text = message
        messageTextView.visibility = View.VISIBLE
    }

    fun getMessage() = toolTipMessage

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

        const val VISIBILITY_VISIBLE = 0
        const val VISIBILITY_GONE = 1

        const val LOCATION_TOP = 0
        const val LOCATION_BOTTOM = 1

        const val GRAVITY_TOP = 0
        const val GRAVITY_CENTER = 1
        const val GRAVITY_BOTTOM = 2

        private const val NOT_PRESENT = Integer.MIN_VALUE
    }
}
