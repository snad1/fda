package com.snad.fdaproductauthenticscanner.ui.mainactivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.snad.fdaproductauthenticscanner.ui.complain.ComplainFragment
import com.snad.fdaproductauthenticscanner.ui.product.ProductFragment
import com.snad.fdaproductauthenticscanner.ui.scanner_history.ScannedHistoryFragment
import com.snad.fdaproductauthenticscanner.ui.qrscanner.QRScannerFragment

/**
 * Developed by Happy on 6/7/19
 */
class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                QRScannerFragment.newInstance()
            }

            1 -> {
                ScannedHistoryFragment.newInstance(ScannedHistoryFragment.ResultListType.ALL_RESULT)
            }

            2 -> {
                ProductFragment.newInstance()
            }

            3 -> {
                ComplainFragment.newInstance()
            }

            else -> {
                QRScannerFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 4
    }
}