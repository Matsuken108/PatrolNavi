package com.patrolnavi.ui.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.patrolnavi.R
import com.patrolnavi.databinding.FragmentCustomerListBinding
import com.patrolnavi.databinding.FragmentSingleCustomerMapBinding

class SingleCustomerMapFragment : Fragment() {

    private var _binding : FragmentSingleCustomerMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private val args : SingleCustomerMapFragmentArgs by navArgs()

    private val callback = OnMapReadyCallback { googleMap ->

        mMap = googleMap

        val lat : Double = args.customer.customer_lat.toDouble()
        val lng : Double = args.customer.customer_lng.toDouble()

        val singleCustomer = LatLng(lat, lng)
        mMap.addMarker(MarkerOptions().position(singleCustomer).title("Here!!!"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singleCustomer,15f))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSingleCustomerMapBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}