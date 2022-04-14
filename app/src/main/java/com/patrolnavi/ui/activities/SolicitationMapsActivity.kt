package com.patrolnavi.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivitySolicitationMapsBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Customer
import com.patrolnavi.models.Groups
import com.patrolnavi.utils.Constants
import java.util.*

class SolicitationMapsActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivitySolicitationMapsBinding

    private lateinit var mMap: GoogleMap
    private lateinit var mGroups: Groups
    private val REQUEST_LOCATION_PERMISSION = 1
    private var mCustomerLat: String = ""
    private var mCustomerLng: String = ""
    private var mGroupsId: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySolicitationMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_LAT)) {
            mGroupsLat = intent.getStringExtra(Constants.EXTRA_GROUPS_LAT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_LNG)) {
            mGroupsLng = intent.getStringExtra(Constants.EXTRA_GROUPS_LNG)!!
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val groupsLat = mGroupsLat.toDouble()
        val groupsLng = mGroupsLng.toDouble()

        val center = LatLng(groupsLat, groupsLng)

        Log.i(javaClass.simpleName, "Solicitation Lat : ${groupsLat} Lng ${groupsLng}")
//        val center = LatLng(34.66871470163587, 133.74921330231805)
//        mMap.addMarker(MarkerOptions().position(center).title("Center"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15f))

        setMapLongClick(mMap)

        enableMyLocation()

    }

    // 位置情報取得
    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // permission取得
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getMapsList()
    }

    private fun getMapsList() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCustomerAll(this@SolicitationMapsActivity, mGroupsId)
    }

    fun MapsListUI(mapsList: ArrayList<Customer>) {
        hideProgressDialog()

        for (i in 0..mapsList.size - 1) {
            val lat: Double = mapsList.get(i).customer_lat.toDouble()
            val lng: Double = mapsList.get(i).customer_lng.toDouble()
            val marker = LatLng(lat, lng)
            Log.i(
                javaClass.simpleName,
                "lat: ${mapsList.get(i).customer_lat} , lng: ${mapsList.get(i).customer_lng} , LatLng: ${marker}"
            )
            mMap.addMarker(
                MarkerOptions().position(marker)
                    .title(
                        "曜日: ${mapsList.get(i).date} コース: ${mapsList.get(i).course} No:${
                            mapsList.get(
                                i
                            ).no
                        } " +
                                "${mapsList.get(i).firstName} ${mapsList.get(i).lastName}"
                    )
            )
            Log.i(javaClass.simpleName, "firstName: ${mapsList.get(i).firstName}")
        }
    }

    // マーカー追加（latLng表示）
    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("位置情報")
                    .snippet(snippet)
            )

            mCustomerLat = latLng.latitude.toString()
            mCustomerLng = latLng.longitude.toString()

            Log.i(javaClass.simpleName, "SolicitationMapsActivity lat : ${mCustomerLat} lng : ${mCustomerLng}")

            showActionSnackBar()
        }
    }

    fun showActionSnackBar() {
        val snackBar =
            Snackbar.make(
                findViewById(android.R.id.content),
                "lat: ${mCustomerLat} lng: ${mCustomerLng}",
//                R.string.message_latLng_add,
                Snackbar.LENGTH_LONG
            )

        snackBar.setAction(R.string.latLng_add) {
            val intent =
                Intent(this@SolicitationMapsActivity, AddCustomerActivity::class.java)
            intent.putExtra(Constants.EXTRA_CUSTOMER_LAT, mCustomerLat)
            intent.putExtra(Constants.EXTRA_CUSTOMER_LNG, mCustomerLng)
            startActivity(intent)
            finish()
        }
        snackBar.show()
    }
}