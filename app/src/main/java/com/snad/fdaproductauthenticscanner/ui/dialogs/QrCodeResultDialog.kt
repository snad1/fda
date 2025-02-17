package com.snad.fdaproductauthenticscanner.ui.dialogs

import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.telecom.Call
import android.text.ClipboardManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.snad.fdaproductauthenticscanner.R
import com.snad.fdaproductauthenticscanner.db.DbHelper
import com.snad.fdaproductauthenticscanner.db.DbHelperI
import com.snad.fdaproductauthenticscanner.db.database.QrResultDataBase
import com.snad.fdaproductauthenticscanner.db.entities.Product
import com.snad.fdaproductauthenticscanner.db.entities.QrResult
import com.snad.fdaproductauthenticscanner.utils.gone
import com.snad.fdaproductauthenticscanner.utils.toFormattedDisplay
import com.snad.fdaproductauthenticscanner.utils.visible
import kotlinx.android.synthetic.main.layout_qr_result_show.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class QrCodeResultDialog(var context: Context) {

    private lateinit var dialog: Dialog

    private lateinit var dbHelperI: DbHelperI

    private var qrResult: QrResult? = null
    private var product: Product? = null
    private var name: String? = null
    private var time: String? = null
    private var pId: Int? = null

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
        dialog.setContentView(R.layout.layout_qr_result_show)
        dialog.setCancelable(false)
        onClicks()
    }

    private fun onClicks() {

        dialog.favouriteIcon.setOnClickListener {
            if (it.isSelected) {
                removeFromFavourite()
            } else
                addToFavourite()
        }

        dialog.copyResult.setOnClickListener {
            copyResultToClipBoard()
        }

        dialog.shareResult.setOnClickListener {
            shareResult()
        }

        dialog.cancelDialog.setOnClickListener {
           onClo()
        }

        dialog.dc.setOnClickListener {
            onClo()
        }
    }

    private fun onClo(){
        dialog.dismiss()
        val dateFormat = SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss")
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        try {
            val date = Date()
            val dateTime: String = dateFormat.format(date)
//            println("Current Date Time : $dateTime")
            time = dateTime
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        onDismissListener?.onDismiss(dialog.complainText.text.toString(), name, pId, time!!)
    }

    private fun addToFavourite() {
        dialog.favouriteIcon.isSelected = true
        dbHelperI.addToFavourite(qrResult?.id!!)
    }

    private fun removeFromFavourite() {
        dialog.favouriteIcon.isSelected = false
        dbHelperI.removeFromFavourite(qrResult?.id!!)
    }

    fun show(recentQrResult: QrResult , b: Boolean) {
        if (b){
            dialog.complainText.visible()
            dialog.dc.visible()
        }else{
            dialog.complainText.gone()
            dialog.dc.gone()
        }
        this.qrResult = recentQrResult
        dialog.scannedDate.text = qrResult?.calendar?.toFormattedDisplay()
        try {
            product = Gson().fromJson(recentQrResult.result, Product::class.java)
        }catch (e: Exception){
            Log.w("showqr", e.message!! + " -- " + e.printStackTrace())
        }
        if (product != null){
            dialog.scannedText.text = product!!.name+ "\n" + product!!.expireDate
            dialog.authText.setTextColor(ContextCompat.getColor(context,R.color.lightColor))
            dialog.authText.text = context.getString(R.string.pro_auth)
            dialog.authIcon.setImageResource(R.drawable.ic_authentic)
            name = product!!.name
            pId = product!!.id
            product = null
        }
        else {
            name = ""
            pId = null
            dialog.scannedText.text = qrResult!!.result
            dialog.authText.setTextColor(ContextCompat.getColor(context,R.color.red))
            dialog.authText.text = context.getString(R.string.pro_auth_not)
            dialog.authIcon.setImageResource(R.drawable.ic_unauthentic)
        }
        dialog.favouriteIcon.isSelected = qrResult!!.favourite
        dialog.show()
    }

    fun setOnDismissListener(dismissListener: OnDismissListener) {
        this.onDismissListener = dismissListener
    }

    private fun copyResultToClipBoard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("QrCodeScannedResult", dialog.scannedText.text)
        clipboard.text = clip.getItemAt(0).text.toString()
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun shareResult() {
        val txtIntent = Intent(Intent.ACTION_SEND)
        txtIntent.type = "text/plain"
        txtIntent.putExtra(
            Intent.EXTRA_TEXT,
            dialog.scannedText.text.toString()
        )
        context.startActivity(Intent.createChooser(txtIntent, "Share QR Result"))
    }

    interface OnDismissListener {
        fun onDismiss(detail: String, name: String?, pId: Int?, time: String)
    }
}