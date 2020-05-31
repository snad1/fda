package com.snad.fdaproductauthenticscanner.ui.qrscanner


import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.snad.fdaproductauthenticscanner.R
import com.snad.fdaproductauthenticscanner.db.DbHelper
import com.snad.fdaproductauthenticscanner.db.DbHelperI
import com.snad.fdaproductauthenticscanner.db.database.QrResultDataBase
import com.snad.fdaproductauthenticscanner.ui.dialogs.QrCodeResultDialog
import kotlinx.android.synthetic.main.fragment_qrscanner.view.*
import kotlinx.android.synthetic.main.fragment_qrscanner1.view.*
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class QRScannerFragment : Fragment() {

    companion object {

        fun newInstance(): QRScannerFragment {
            return QRScannerFragment()
        }
    }

    private lateinit var mView: View

    lateinit var scannerView: ZBarScannerView

    lateinit var resultDialog: QrCodeResultDialog

    private lateinit var codeScanner: CodeScanner

    private lateinit var dbHelperI: DbHelperI

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_qrscanner1, container, false)
        init()
        initViews()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
//        onClicks()
        return mView.rootView
    }

    private fun init() {
        dbHelperI = DbHelper(QrResultDataBase.getAppDatabase(context!!)!!)
    }

    private fun initViews() {
        initializeQRCamera1()
        setResultDialog()
    }

    private fun setResultDialog() {
        resultDialog = QrCodeResultDialog(context!!)
        resultDialog.setOnDismissListener(object : QrCodeResultDialog.OnDismissListener {
            override fun onDismiss(co: String) {
//                resetPreview()
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        // Got last known location. In some rare situations this can be null.
                        Toast.makeText(context, "Your location: (${location?.latitude},${location?.longitude})", Toast.LENGTH_LONG).show()
                    }
                if (!TextUtils.isEmpty(co)){
                    saveComplain(co)
                }
            }
        })
    }

    private fun onQrResult(contents: String?) {
        if (contents.isNullOrEmpty())
            showToast("Empty Qr Result")
        else
            saveToDataBase(contents)
    }

    private fun showToast(message: String) {
        Toast.makeText(context!!, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveToDataBase(contents: String) {
        val insertedResultId = dbHelperI.insertQRResult(contents)
        val qrResult = dbHelperI.getQRResult(insertedResultId)
        resultDialog.show(qrResult, true)
    }

    private fun saveComplain(contents: String) {
        dbHelperI.insertComplain(contents)
    }


    private fun initializeQRCamera1() {

        codeScanner = CodeScanner(activity!!, mView.scanner_view)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            activity?.runOnUiThread {
                onQrResult(it.text)
//                Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            activity?.runOnUiThread {
                Toast.makeText(context, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        mView.scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun startQRCamera1() {
        scannerView.startCamera()
    }

    /*private fun onClicks() {
        mView.flashToggle.setOnClickListener {
            if (mView.flashToggle.isSelected) {
                offFlashLight()
            } else {
                onFlashLight()
            }
        }
    }

    private fun onFlashLight() {
        mView.flashToggle.isSelected = true
        scannerView.flash = true
    }*/

    private fun offFlashLight() {
        mView.flashToggle.isSelected = false
        scannerView.flash = false
    }

    override fun onResume() {
        super.onResume()
        /*scannerView.setResultHandler(this)
        scannerView.startCamera()*/
        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
//        scannerView.stopCamera()
        codeScanner.releaseResources()
    }

    override fun onDestroy() {
        super.onDestroy()
//        scannerView.stopCamera()
        codeScanner.stopPreview()
    }

    /*fun getLocation() {

        var locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        var locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                var latitute = location!!.latitude
                var longitute = location!!.longitude

                Log.i("test", "Latitute: $latitute ; Longitute: $longitute")

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }

        }

        try {
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch (ex:SecurityException) {
            Toast.makeText(context, "Fehler bei der Erfassung!", Toast.LENGTH_SHORT).show()
        }
    }*/

}
