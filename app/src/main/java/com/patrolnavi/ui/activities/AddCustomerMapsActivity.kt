package com.patrolnavi.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import com.patrolnavi.databinding.ActivityAddCustomerMapsBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Groups
import com.patrolnavi.utils.Constants
import java.util.*

class AddCustomerMapsActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityAddCustomerMapsBinding

    private lateinit var mMap: GoogleMap
    private lateinit var mGroups: Groups
    private val REQUEST_LOCATION_PERMISSION = 1
    private var mGroupsId: String = ""
    private var mDateSelect: String = ""
    private var mCourseSelect: String = ""
    private var mNo: String = ""
    private var mFirstName: String = ""
    private var mLastName: String = ""
    private var mCustomerLat: String = ""
    private var mCustomerLng: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCustomerMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_DATE_SELECT)) {
            mDateSelect = intent.getStringExtra(Constants.EXTRA_DATE_SELECT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_COURSE_SELECT)) {
            mCourseSelect = intent.getStringExtra(Constants.EXTRA_COURSE_SELECT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_NO_SELECT)) {
            mNo = intent.getStringExtra(Constants.EXTRA_NO_SELECT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_FIRST_NAME)) {
            mFirstName = intent.getStringExtra(Constants.EXTRA_FIRST_NAME)!!
        }
        if (intent.hasExtra(Constants.EXTRA_LAST_NAME)) {
            mLastName = intent.getStringExtra(Constants.EXTRA_LAST_NAME)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_LAT)) {
            mGroupsLat = intent.getStringExtra(Constants.EXTRA_GROUPS_LAT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_LNG)) {
            mGroupsLng = intent.getStringExtra(Constants.EXTRA_GROUPS_LNG)!!
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val groupsLat = mGroupsLat.toDouble()
        val groupsLng = mGroupsLng.toDouble()

        val center = LatLng(groupsLat, groupsLng)
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

            Log.i(javaClass.simpleName, "MapsActivity lat : ${mCustomerLat} lng : ${mCustomerLng}")

            showActionSnackBar()
        }
    }

    fun showActionSnackBar() {
        val snackBar =
            Snackbar.make(
                findViewById(android.R.id.content),
                R.string.message_latLng_customer_add,
                Snackbar.LENGTH_LONG
            )

        snackBar.setAction(R.string.latLng_add) {
            val intent =
                Intent(this@AddCustomerMapsActivity, AddCustomerActivity::class.java)
            intent.putExtra(Constants.EXTRA_GROUPS_ID,mGroupsId)
            intent.putExtra(Constants.EXTRA_DATE_SELECT, mDateSelect)
            intent.putExtra(Constants.EXTRA_COURSE_SELECT, mCourseSelect)
            intent.putExtra(Constants.EXTRA_NO_SELECT, mNo)
            intent.putExtra(Constants.EXTRA_FIRST_NAME, mFirstName)
            intent.putExtra(Constants.EXTRA_LAST_NAME, mLastName)
            intent.putExtra(Constants.EXTRA_CUSTOMER_LAT, mCustomerLat)
            intent.putExtra(Constants.EXTRA_CUSTOMER_LNG, mCustomerLng)
            intent.putExtra(Constants.EXTRA_GROUPS_LAT,mGroupsLat)
            intent.putExtra(Constants.EXTRA_GROUPS_LNG,mGroupsLng)

            startActivity(intent)
            finish()
        }
        snackBar.show()
    }
}

