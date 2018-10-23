package com.cunoraz.tooltipviewdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.cunoraz.tooltipview.TooltipView
import com.cunoraz.tooltipview.TooltipView.Companion.LOCATION_BOTTOM
import com.cunoraz.tooltipview.TooltipView.Companion.LOCATION_TOP
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tooltipView = TooltipView(this)
        tooltipView.setAnchoredViewId(R.id.header_4)
        tooltipView.setTitle("Dynamic Tool Tip Title")
        tooltipView.setMessage("Dynamic Tool Tip Message body")
        tooltipView.setArrowPosition(LOCATION_TOP)

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        mainRootLayout.addView(tooltipView,layoutParams)

    }


}
