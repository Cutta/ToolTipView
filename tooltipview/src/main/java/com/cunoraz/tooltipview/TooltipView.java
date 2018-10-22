package com.cunoraz.tooltipview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TooltipView extends LinearLayout {

    private static final int NOT_PRESENT = Integer.MIN_VALUE;
    private String toolTipTitle;
    private String toolTipMessage;
    private int arrowHeight;
    private int arrowWidth;
    private int cornerRadius;
    private int anchoredViewId;
    private int tooltipBgColor;
    private ArrowLocation arrowLocation;
    private int arrowPositioning;
    private Paint paint;
    private Path tooltipPath;

    private TextView titleTextView;
    private TextView messageTextView;

    public TooltipView(Context context) {
        super(context);
        init(null, 0);
    }

    public TooltipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TooltipView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        setWillNotDraw(false);
        Resources res = getResources();
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TooltipView, defStyle, 0);
        try {
            toolTipTitle = a.getString(R.styleable.TooltipView_toolTipTitle);
            toolTipMessage = a.getString(R.styleable.TooltipView_toolTipMessage);
            anchoredViewId = a.getResourceId(R.styleable.TooltipView_anchoredView, View.NO_ID);
            tooltipBgColor = a.getColor(R.styleable.TooltipView_tooltipBgColor, res.getColor(R.color.default_tooltip_bg_color));
            cornerRadius = getDimension(a, R.styleable.TooltipView_cornerRadius, R.dimen.tooltip_default_corner_radius);
            arrowHeight = getDimension(a, R.styleable.TooltipView_arrowHeight, R.dimen.tooltip_default_arrow_height);
            arrowWidth = getDimension(a, R.styleable.TooltipView_arrowWidth, R.dimen.tooltip_default_arrow_width);
            arrowPositioning = a.getInteger(R.styleable.TooltipView_arrowLocation, res.getInteger(R.integer.tooltip_default_arrow_location));
            arrowLocation = arrowPositioning == Location.TOP ? new TopArrowLocation() : new BottomArrowLocation();

        } finally {
            a.recycle();

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                inflater.inflate(R.layout.tooltip_with_title, this, true);
                initViews();
                setTexts(toolTipTitle, toolTipMessage);
            }
        }
    }

    private void initViews() {
        titleTextView = findViewById(R.id.titleTextView);
        messageTextView = findViewById(R.id.messageTextView);
    }

    private void setTexts(String toolTipTitle, String toolTipMessage) {
        titleTextView.setText(toolTipTitle);
        messageTextView.setText(toolTipMessage);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (arrowPositioning == Location.TOP) {
            setPadding(0, arrowHeight, 0, 0);
        } else {
            setPadding(0, 0, 0, arrowHeight);
        }
        Log.d("onAttachedToWindow", "onAttachedToWindow: ");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + arrowHeight);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        tooltipPath = null;
        paint = null;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (tooltipPath == null || paint == null) {
            arrowLocation.configureDraw(this, canvas);
        }
        canvas.drawPath(tooltipPath, paint);
        super.onDraw(canvas);
    }

    Paint getTooltipPaint() {
        return paint;
    }

    void setPaint(Paint paint) {
        this.paint = paint;
    }

    Path getTooltipPath() {
        return tooltipPath;
    }

    void setTooltipPath(Path tooltipPath) {
        this.tooltipPath = tooltipPath;
    }

    public int getArrowHeight() {
        return arrowHeight;
    }

    public void setArrowHeight(int arrowHeight) {
        this.arrowHeight = arrowHeight;
        invalidate();
    }

    public void setArrowHeightResource(@DimenRes int resId) {
        arrowHeight = getResources().getDimensionPixelSize(resId);
        invalidate();
    }

    public int getArrowWidth() {
        return arrowWidth;
    }

    public void setArrowWidth(int arrowWidth) {
        this.arrowWidth = arrowWidth;
        invalidate();
    }

    public void setArrowWidthResource(@DimenRes int resId) {
        arrowWidth = getResources().getDimensionPixelSize(resId);
        invalidate();
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidate();
    }

    public void setCornerRadiusResource(@DimenRes int resId) {
        cornerRadius = getResources().getDimensionPixelSize(resId);
        invalidate();
    }

    public int getAnchoredViewId() {
        return anchoredViewId;
    }

    public void setAnchoredViewId(@IdRes int anchoredViewId) {
        this.anchoredViewId = anchoredViewId;
        invalidate();
    }

    public int getTooltipBgColor() {
        return tooltipBgColor;
    }

    public void setTooltipBgColor(int tooltipBgColor) {
        this.tooltipBgColor = tooltipBgColor;
        invalidate();
    }

    public void setArrowPositioning(int arrowPositioning) {
        this.arrowPositioning = arrowPositioning;
        invalidate();
    }

    private int getDimension(TypedArray typedArray,
                             @StyleableRes int styleableId,
                             @DimenRes int defaultDimension) {
        int result = typedArray.getDimensionPixelSize(styleableId, NOT_PRESENT);
        if (result == NOT_PRESENT) {
            result = getResources().getDimensionPixelSize(defaultDimension);
        }
        return result;
    }

    @IntDef({Location.TOP, Location.BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    @interface Location {
        int TOP = 0;
        int BOTTOM = 1;
    }
}
