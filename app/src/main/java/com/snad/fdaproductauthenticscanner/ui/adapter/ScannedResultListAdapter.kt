package com.snad.fdaproductauthenticscanner.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.snad.fdaproductauthenticscanner.R
import com.snad.fdaproductauthenticscanner.db.DbHelperI
import com.snad.fdaproductauthenticscanner.db.entities.Product
import com.snad.fdaproductauthenticscanner.db.entities.QrResult
import com.snad.fdaproductauthenticscanner.ui.dialogs.QrCodeResultDialog
import com.snad.fdaproductauthenticscanner.utils.gone
import com.snad.fdaproductauthenticscanner.utils.toFormattedDisplay
import com.snad.fdaproductauthenticscanner.utils.visible
import kotlinx.android.synthetic.main.layout_single_item_qr_result.view.*
import java.text.SimpleDateFormat
import java.util.*


class ScannedResultListAdapter(
    var dbHelperI: DbHelperI,
    var context: Context,
    private var listOfScannedResult: MutableList<QrResult>
) :
    RecyclerView.Adapter<ScannedResultListAdapter.ScannedResultListViewHolder>() {

    private var product: Product? = null

    private var resultDialog: QrCodeResultDialog =
        QrCodeResultDialog(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannedResultListViewHolder {
        return ScannedResultListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_single_item_qr_result,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfScannedResult.size
    }

    override fun onBindViewHolder(holder: ScannedResultListViewHolder, position: Int) {
        holder.bind(listOfScannedResult[position], position)
    }

    inner class ScannedResultListViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bind(qrResult: QrResult, position: Int) {
            try {
                product = Gson().fromJson(qrResult.result, Product::class.java)
            }catch (e: Exception){
                Log.w("showqr", e.message!! + " -- " + e.printStackTrace())
            }

            if (product != null){
                view.result.text = product?.name + " - " + product?.licenseId
                val sdf = SimpleDateFormat("MM/yyyy")
                val strDate: Date = sdf.parse(product?.expireDate)
//                Toast.makeText(context, strDate.toString(), Toast.LENGTH_SHORT).show()
//                if (System.currentTimeMillis() > strDate.getTime()) {
                if (Date().time > strDate.getTime()) {
                    view.exDateR.visible()
                }else{
                    view.exDateR.gone()
                }
                view.favouriteIcon.setImageResource(R.drawable.ic_authentic)
                product = null
            }
            else {
                view.result.text = qrResult.result!!
                view.exDateR.gone()
                view.favouriteIcon.setImageResource(R.drawable.ic_unauthentic)
            }


            view.tvTime.text = qrResult.calendar.toFormattedDisplay()

            onClicks(qrResult, position)
        }

        private fun setFavourite(isFavourite: Boolean) {
            if (isFavourite)
                view.favouriteIcon.visible()
            else
                view.favouriteIcon.gone()
        }


        private fun onClicks(qrResult: QrResult, position: Int) {
            view.setOnClickListener {
                resultDialog.show(qrResult, false)
            }

            view.setOnLongClickListener {
                showDeleteDialog(qrResult, position)
                return@setOnLongClickListener true
            }
        }

        private fun showDeleteDialog(qrResult: QrResult, position: Int) {
            AlertDialog.Builder(context, R.style.CustomAlertDialog).setTitle(context.getString(R.string.delete))
                .setMessage(context.getString(R.string.want_to_delete))
                .setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                    deleteThisRecord(qrResult, position)
                }
                .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }.show()
        }

        private fun deleteThisRecord(qrResult: QrResult, position: Int) {
            dbHelperI.deleteQrResult(qrResult.id!!)
            listOfScannedResult.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}