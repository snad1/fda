package com.snad.fdaproductauthenticscanner.ui.mainactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.snad.fdaproductauthenticscanner.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewPager()
        setBottomViewListener()
        setViewPagerListener()
    }


    private fun setViewPager() {
        viewPager.adapter = MainPagerAdapter(supportFragmentManager)
        viewPager.offscreenPageLimit = 2
    }

    private fun setBottomViewListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.qrScanMenuId -> {
                    viewPager.currentItem = 0
                }
                R.id.scannedResultMenuId -> {
                    viewPager.currentItem = 1

                }
                R.id.productsMenuId -> {
                    viewPager.currentItem = 2
                }
                R.id.complainMenuId -> {
                    viewPager.currentItem = 3
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }


    private fun setViewPagerListener() {
        viewPager.setOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        bottomNavigationView.selectedItemId = R.id.qrScanMenuId
                    }
                    1 -> {
                        bottomNavigationView.selectedItemId = R.id.scannedResultMenuId
                    }
                    2 -> {
                        bottomNavigationView.selectedItemId = R.id.productsMenuId
                    }
                    3 -> {
                        bottomNavigationView.selectedItemId = R.id.complainMenuId
                    }
                }
            }
        })
    }
}