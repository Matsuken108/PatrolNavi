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
import com.patrolnavi.databinding.ActivityAddGroupsMapsBinding
import com.patrolnavi.utils.Constants
import java.util.*

class AddGroupsMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityAddGroupsMapsBinding

    private lateinit var mMap: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1
    private var mGroupsName: String = ""
    private var mGroupsPass: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddGroupsMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_NAME)) {
            mGroupsName = intent.getStringExtra(Constants.EXTRA_GROUPS_NAME)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_PASS)) {
            mGroupsPass = intent.getStringExtra(Constants.EXTRA_GROUPS_PASS)!!
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val center = LatLng(35.681358186751076, 139.7671140689504)
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

            mGroupsLat = latLng.latitude.toString()
            mGroupsLng = latLng.longitude.toString()

            Log.i(javaClass.simpleName, "MapsActivity lat : ${mGroupsLat} lng : ${mGroupsLng}")

            showActionSnackBar()
        }
    }

    fun showActionSnackBar() {
        val snackBar =
            Snackbar.make(
                findViewById(android.R.id.content),
                R.string.message_latLng_add,
                Snackbar.LENGTH_LONG
            )

        snackBar.setAction(R.string.latLng_add) {
            val intent =
                Intent(this@AddGroupsMapsActivity, AddGroupsActivity::class.java)
            intent.putExtra(Constants.EXTRA_GROUPS_NAME, mGroupsName)
            intent.putExtra(Constants.EXTRA_GROUPS_PASS, mGroupsPass)
            intent.putExtra(Constants.EXTRA_GROUPS_LAT, mGroupsLat)
            intent.putExtra(Constants.EXTRA_GROUPS_LNG, mGroupsLng)

            Log.i(
                javaClass.simpleName,
                "AddGroupsMap name:${mGroupsName} pass:${mGroupsPass} lat:${mGroupsLat} lng:${mGroupsLng}"
            )

            startActivity(intent)
            finish()
        }
        snackBar.show()
    }
}