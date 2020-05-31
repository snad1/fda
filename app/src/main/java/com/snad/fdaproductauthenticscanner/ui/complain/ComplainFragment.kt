package com.snad.fdaproductauthenticscanner.ui.complain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.snad.fdaproductauthenticscanner.R
import com.snad.fdaproductauthenticscanner.db.DbHelper
import com.snad.fdaproductauthenticscanner.db.DbHelperI
import com.snad.fdaproductauthenticscanner.db.database.QrResultDataBase
import com.snad.fdaproductauthenticscanner.db.entities.Complain
import com.snad.fdaproductauthenticscanner.ui.adapter.ComplainListAdapter
import com.snad.fdaproductauthenticscanner.utils.gone
import com.snad.fdaproductauthenticscanner.utils.visible
import kotlinx.android.synthetic.main.fragment_complain.view.*
import kotlinx.android.synthetic.main.layout_header_complain.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ComplainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComplainFragment : Fragment() {

    companion object {
        fun newInstance(): ComplainFragment {
            return ComplainFragment()
        }
    }

    private lateinit var mView: View

    private lateinit var dbHelperI: DbHelperI

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_complain, container, false)
        init()
        setSwipeRefresh()
//        onClicks()
        showListOfResults()
        return mView.rootView
    }

    private fun init() {
        dbHelperI = DbHelper(QrResultDataBase.getAppDatabase(context!!)!!)
        mView.layoutHeaderCom.tvHeaderTextCom.text = getString(R.string.com_header_text)
    }

    private fun showListOfResults() {
        showAllResults()
    }

    private fun showAllResults() {
        val listOfAllResult = dbHelperI.getAllComplains()
        showResults(listOfAllResult)
        mView.layoutHeaderCom.tvHeaderTextCom.text = getString(R.string.com_header_text)
    }

    private fun showResults(listOfComplain: List<Complain>) {
        if (listOfComplain.isNotEmpty())
            initRecyclerView(listOfComplain)
        else
            showEmptyState()
    }

    private fun initRecyclerView(listOfComplain: List<Complain>) {
        mView.complainRecyclerView.layoutManager = LinearLayoutManager(context)
        mView.complainRecyclerView.adapter =
            ComplainListAdapter(dbHelperI, context!!, listOfComplain.toMutableList())
        showRecyclerView()
    }

    private fun setSwipeRefresh() {
        mView.swipeRefreshCom.setOnRefreshListener {
            mView.swipeRefreshCom.isRefreshing = false
            showListOfResults()
        }
    }

    /*private fun onClicks() {
        mView.layoutHeaderCom.removeAll.setOnClickListener {
            showRemoveAllProductDialog()
        }
    }*/

    /*private fun showRemoveAllProductDialog() {
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
    }*/

    private fun showRecyclerView() {
//        mView.layoutHeaderCom.removeAll.visible()
        mView.complainRecyclerView.visible()
        mView.noResultFoundCom.gone()
    }

    private fun showEmptyState() {
//        mView.layoutHeaderCom.removeAll.gone()
        mView.complainRecyclerView.gone()
        mView.noResultFoundCom.visible()
    }
}
