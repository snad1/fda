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
import com.snad.fdaproductauthenticscanner.db.entities.Complain
import kotlinx.android.synthetic.main.layout_complain_show.*
import kotlinx.android.synthetic.main.layout_product_show.*
import kotlinx.android.synthetic.main.layout_product_show.cancelDialog
import kotlinx.android.synthetic.main.layout_product_show.shareResult


class ComplainDialog(var context: Context) {

    private lateinit var dialog: Dialog

    private lateinit var dbHelperI: DbHelperI

    private var complain: Complain? = null

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
        dialog.setContentView(R.layout.layout_complain_show)
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

    fun show(complain: Complain) {
        this.complain = complain
//        dialog.comDate.text = complain!!.expireDate
        dialog.comDetail.text = complain.detail
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