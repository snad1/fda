package com.snad.fdaproductauthenticscanner.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.snad.fdaproductauthenticscanner.R
import com.snad.fdaproductauthenticscanner.db.DbHelperI
import com.snad.fdaproductauthenticscanner.db.entities.Product
import com.snad.fdaproductauthenticscanner.db.entities.QrResult
import com.snad.fdaproductauthenticscanner.ui.dialogs.ProductDialog
import com.snad.fdaproductauthenticscanner.ui.dialogs.QrCodeResultDialog
import com.snad.fdaproductauthenticscanner.utils.gone
import com.snad.fdaproductauthenticscanner.utils.toFormattedDisplay
import com.snad.fdaproductauthenticscanner.utils.visible
import kotlinx.android.synthetic.main.layout_single_item_product.view.*
import kotlinx.android.synthetic.main.layout_single_item_qr_result.view.*

class ProductListAdapter(
    var dbHelperI: DbHelperI,
    var context: Context,
    private var listOfProduct: MutableList<Product>
) :
    RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>() {

    private var productDialog: ProductDialog =
        ProductDialog(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        return ProductListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_single_item_product,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfProduct.size
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        holder.bind(listOfProduct[position], position)
    }

    inner class ProductListViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bind(product: Product, position: Int) {
            view.namePro.text = product.name!!
            view.expirePro.text = product.expireDate.toString()
            onClicks(product, position)
        }

        private fun setFavourite(isFavourite: Boolean) {
            if (isFavourite)
                view.favouriteIcon.visible()
            else
                view.favouriteIcon.gone()
        }


        private fun onClicks(product: Product, position: Int) {
            view.setOnClickListener {
                productDialog.show(product)
            }

            view.setOnLongClickListener {
                showDeleteDialog(product, position)
                return@setOnLongClickListener true
            }
        }

        private fun showDeleteDialog(product: Product, position: Int) {
            AlertDialog.Builder(context, R.style.CustomAlertDialog).setTitle(context.getString(R.string.delete))
                .setMessage(context.getString(R.string.want_to_delete))
                .setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                    deleteThisRecord(product, position)
                }
                .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }.show()
        }

        private fun deleteThisRecord(product: Product, position: Int) {
            dbHelperI.deleteProduct(product.id!!)
            listOfProduct.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}