package com.snad.fdaproductauthenticscanner.ui.dialogs

import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.text.ClipboardManager
import android.widget.Toast
import com.snad.fdaproductauthenticscanner.R
import com.snad.fdaproductauthenticscanner.db.DbHelper
import com.snad.fdaproductauthenticscanner.db.DbHelperI
import com.snad.fdaproductauthenticscanner.db.database.QrResultDataBase
import com.snad.fdaproductauthenticscanner.db.entities.Product
import kotlinx.android.synthetic.main.layout_product_show.*


class ProductDialog(var context: Context) {

    private lateinit var dialog: Dialog

    private lateinit var dbHelperI: DbHelperI

    private var product: Product? = null

    private var onDismissListener: OnDismissListener? = null

    init {
        init()
        initDialog()
    }


    private fun init() {
        dbHelperI = DbHelper(QrResultDataBase.getAppDatabase(context)!!)
    }

    private fun initDialog() {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_product_show)
        dialog.setCancelable(false)
        onClicks()
    }

    private fun onClicks() {

        dialog.shareResult.setOnClickListener {
            shareResult()
        }

        dialog.cancelDialog.setOnClickListener {
            dialog.dismiss()
            onDismissListener?.onDismiss()
        }
    }

    fun show(recentProduct: Product) {
        this.product = recentProduct
        dialog.proDate.text = product!!.expireDate
        dialog.proName.text = product!!.name
        dialog.show()
    }

    fun setOnDismissListener(dismissListener: OnDismissListener) {
        this.onDismissListener = dismissListener
    }

    private fun copyResultToClipBoard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("QrCodeScannedResult", dialog.proName.text)
        clipboard.text = clip.getItemAt(0).text.toString()
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun shareResult() {
        val txtIntent = Intent(Intent.ACTION_SEND)
        txtIntent.type = "text/plain"
        txtIntent.putExtra(
            Intent.EXTRA_TEXT,
            dialog.proName.text.toString()
        )
        context.startActivity(Intent.createChooser(txtIntent, "Share QR Result"))
    }

    interface OnDismissListener {
        fun onDismiss()
    }
}