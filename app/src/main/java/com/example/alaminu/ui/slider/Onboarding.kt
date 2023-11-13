package com.example.alaminu.ui.slider

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.alaminu.LoginActivity
import com.example.alaminu.R

class Onboarding : AppCompatActivity() {

    private lateinit var mSLideViewPager: ViewPager
    private lateinit var mDotLayout: LinearLayout
    private lateinit var nextbtn: Button
    private lateinit var skipbtn: Button
    private lateinit var dots: Array<TextView>
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding)
        supportActionBar?.hide()

        nextbtn = findViewById(R.id.nextbtn)
        skipbtn = findViewById(R.id.skipButton)

        nextbtn.setOnClickListener {
            if (getItem(0) < 2) {
                mSLideViewPager.setCurrentItem(getItem(1), true)
            } else {
                val intent = Intent(this@Onboarding, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        skipbtn.setOnClickListener {
            val intent = Intent(this@Onboarding, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        mSLideViewPager = findViewById(R.id.slideViewPager)
        mDotLayout = findViewById(R.id.indicator_layout)

        viewPagerAdapter = ViewPagerAdapter(this)

        mSLideViewPager.adapter = viewPagerAdapter

        setUpIndicator(0)
        mSLideViewPager.addOnPageChangeListener(viewListener)
    }

    private fun setUpIndicator(position: Int) {
        dots = Array(3) {
            TextView(this).apply {
                text = Html.fromHtml("&#8226")
                textSize = 35f
                setTextColor(ContextCompat.getColor(this@Onboarding, R.color.inactive))
            }
        }

        mDotLayout.removeAllViews()

        for (i in dots.indices) {
            mDotLayout.addView(dots[i])
        }

        dots[position].setTextColor(ContextCompat.getColor(this, R.color.active))
    }

    private val viewListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            setUpIndicator(position)
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    private fun getItem(i: Int): Int {
        return mSLideViewPager.currentItem + i
    }
}
