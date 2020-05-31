package com.snad.fdaproductauthenticscanner.ui.product

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager

import com.snad.fdaproductauthenticscanner.R
import com.snad.fdaproductauthenticscanner.db.DbHelper
import com.snad.fdaproductauthenticscanner.db.DbHelperI
import com.snad.fdaproductauthenticscanner.db.database.QrResultDataBase
import com.snad.fdaproductauthenticscanner.db.entities.Product
import com.snad.fdaproductauthenticscanner.ui.adapter.ProductListAdapter
import com.snad.fdaproductauthenticscanner.utils.gone
import com.snad.fdaproductauthenticscanner.utils.visible
import kotlinx.android.synthetic.main.fragment_product.view.*
import kotlinx.android.synthetic.main.layout_header_product.view.*

/**
 * A simple [Fragment] subclass.
 */
class ProductFragment : Fragment() {

    companion object {
        fun newInstance(): ProductFragment {
            return ProductFragment()
        }
    }

    private lateinit var mView: View

    private lateinit var dbHelperI: DbHelperI

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_product, container, false)
        init()
        setSwipeRefresh()
        onClicks()
        showListOfResults()
        return mView.rootView
    }

    private fun init() {
        dbHelperI = DbHelper(QrResultDataBase.getAppDatabase(context!!)!!)
        mView.layoutHeaderPro.tvHeaderTextPro.text = getString(R.string.products)
    }

    private fun showListOfResults() {
        showAllResults()
    }

    private fun showAllResults() {
        val listOfAllResult = dbHelperI.getAllProducts()
        showResults(listOfAllResult)
        mView.layoutHeaderPro.tvHeaderTextPro.text = getString(R.string.products)
    }

    private fun showResults(listOfProduct: List<Product>) {
        if (listOfProduct.isNotEmpty())
            initRecyclerView(listOfProduct)
        else
            showEmptyState()
    }

    private fun initRecyclerView(listOfProduct: List<Product>) {
        mView.productRecyclerView.layoutManager = LinearLayoutManager(context)
        mView.productRecyclerView.adapter =
            ProductListAdapter(dbHelperI, context!!, listOfProduct.toMutableList())
        showRecyclerView()
    }

    private fun setSwipeRefresh() {
        mView.swipeRefreshPro.setOnRefreshListener {
            mView.swipeRefreshPro.isRefreshing = false
            showListOfResults()
        }
    }

    private fun onClicks() {
        mView.layoutHeaderPro.removeAll.setOnClickListener {
            showRemoveAllProductDialog()
        }

        mView.fabAddPro.setOnClickListener {
            activity?.let {
                it.startActivity(Intent(activity, ProductAdd::class.java))
            }
        }
    }

    private fun showRemoveAllProductDialog() {
        AlertDialog.Builder(context!!, R.style.CustomAlertDialog).setTitle(getString(R.string.clear_all))
            .setMessage(getString(R.string.clear_all_result))
            .setPositiveButton(getString(R.string.clear)) { _, _ ->
                clearAllRecords()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }.show()

    }

    private fun clearAllRecords() {
        dbHelperI.deleteAllQRScannedResult()
        mView.productRecyclerView.adapter?.notifyDataSetChanged()
        showListOfResults()
    }

    private fun showRecyclerView() {
        mView.layoutHeaderPro.removeAll.visible()
        mView.productRecyclerView.visible()
        mView.noResultFoundPro.gone()
    }

    private fun showEmptyState() {
        mView.layoutHeaderPro.removeAll.gone()
        mView.productRecyclerView.gone()
        mView.noResultFoundPro.visible()
    }

}
