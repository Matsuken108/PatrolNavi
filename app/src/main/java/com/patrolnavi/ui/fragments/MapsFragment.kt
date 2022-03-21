package com.patrolnavi.ui.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.patrolnavi.R
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Customer
import com.patrolnavi.models.Groups
import java.util.*

class MapsFragment() : BaseFragment() {

    private lateinit var mMap: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1
    private var mDateSelect: String = ""
    private var mCourseSelect: String = ""
    private var mGroupsId: String = ""
    private var mGroupsLat: Double = 0.00
    private var mGroupsLng: Double = 0.00

    private val callback = OnMapReadyCallback { googleMap ->

        mMap = googleMap

        val center = LatLng(mGroupsLat,mGroupsLng)
        mMap.addMarker(MarkerOptions().position(center).title("Center"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15f))

        enableMyLocation()

        setMapLongClick(mMap)
    }

    // 位置情報取得
    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // permission取得
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
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
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val extras: Bundle?
        val intent = activity?.intent

        extras = intent?.extras

        mDateSelect = extras?.getString("dateSelect").toString()
        mCourseSelect = extras?.getString("courseSelect").toString()
        mGroupsId = extras?.getString("groupsId").toString()
        mGroupsLat = extras?.getString("groupsLat").toString().toDouble()
        mGroupsLng = extras?.getString("groupsLng").toString().toDouble()

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    private fun getMapsList() {

        FirestoreClass().getMapsList(this@MapsFragment, mGroupsId, mDateSelect, mCourseSelect)
    }

    override fun onResume() {
        super.onResume()
        getMapsList()
    }

    fun MapsListUI(mapsList: ArrayList<Customer>) {
        hideProgressDialog()

        for (i in 0..mapsList.size - 1) {
            val lat: Double = mapsList.get(i).lat.toDouble()
            val lng: Double = mapsList.get(i).lng.toDouble()
            val marker = LatLng(lat, lng)
            Log.i(
                javaClass.simpleName,
                "lat: ${mapsList.get(i).lat} , lng: ${mapsList.get(i).lng} , LatLng: ${marker}"
            )
            mMap.addMarker(
                MarkerOptions().position(marker)
                    .title(
                        "${mapsList.get(i).course} コース No:${mapsList.get(i).no} " +
                                "${mapsList.get(i).firstName} ${mapsList.get(i).lastName}"
                    )
            )
            Log.i(javaClass.simpleName, "firstName: ${mapsList.get(i).firstName}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    // マーカー追加（latLng表示）
    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.6f, Lng: %2$.6f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("位置情報")
                    .snippet(snippet)
            )
        }
    }
}