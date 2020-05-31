package com.snad.fdaproductauthenticscanner.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.snad.fdaproductauthenticscanner.R
import com.snad.fdaproductauthenticscanner.db.DbHelperI
import com.snad.fdaproductauthenticscanner.db.entities.Complain
import com.snad.fdaproductauthenticscanner.ui.dialogs.ComplainDialog
import com.snad.fdaproductauthenticscanner.utils.gone
import com.snad.fdaproductauthenticscanner.utils.visible
import kotlinx.android.synthetic.main.layout_single_item_complain.view.*

class ComplainListAdapter(
    var dbHelperI: DbHelperI,
    var context: Context,
    private var listOfComplain: MutableList<Complain>
) :
    RecyclerView.Adapter<ComplainListAdapter.ComplainListViewHolder>() {

    private var complainDialog: ComplainDialog =
        ComplainDialog(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplainListViewHolder {
        return ComplainListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_single_item_complain,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfComplain.size
    }

    override fun onBindViewHolder(holder: ComplainListViewHolder, position: Int) {
        holder.bind(listOfComplain[position], position)
    }

    inner class ComplainListViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bind(complain: Complain, position: Int) {
            view.detailCom.text = complain.detail
//            view.expirePro.text = product.expireDate.toString()
            onClicks(complain, position)
        }


        private fun onClicks(complain: Complain, position: Int) {
            view.setOnClickListener {
                complainDialog.show(complain)
            }

            /*view.setOnLongClickListener {
                showDeleteDialog(complain, position)
                return@setOnLongClickListener true
            }*/
        }

        /*private fun showDeleteDialog(complain: Complain, position: Int) {
            AlertDialog.Builder(context, R.style.CustomAlertDialog).setTitle(context.getString(R.string.delete))
                .setMessage(context.getString(R.string.want_to_delete))
                .setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                    deleteThisRecord(product, position)
                }
                .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }.show()
        }

        private fun deleteThisRecord(complain: Complain, position: Int) {
            dbHelperI.deleteProduct(product.id!!)
            listOfComplain.removeAt(position)
            notifyItemRemoved(position)
        }*/
    }
}