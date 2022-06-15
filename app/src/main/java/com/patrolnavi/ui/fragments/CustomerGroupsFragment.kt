package com.patrolnavi.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.patrolnavi.R
import com.patrolnavi.databinding.FragmentCustomerGroupsBinding
import com.patrolnavi.databinding.FragmentCustomerListBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Customer
import com.patrolnavi.ui.adapters.CustomerListAdapter


class CustomerGroupsFragment : BaseFragment() {

    private var _binding: FragmentCustomerGroupsBinding? = null
    private val binding get() = _binding!!

    private var mGroupsId: String = ""
    private var mDateSelect: String = ""
    private var mCourseSelect: String = ""
    private lateinit var mCustomerList: ArrayList<Customer>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerGroupsBinding.inflate(inflater, container, false)

        val extras: Bundle?
        val intent = activity?.intent

        extras = intent?.extras

        mDateSelect = extras?.getString("dateSelect").toString()
        mCourseSelect = extras?.getString("courseSelect").toString()
        mGroupsId = extras?.getString("groupsId").toString()

        return binding.root
    }

    private fun getCourseAllSetList() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCourseAllSetList(
            this@CustomerGroupsFragment,
            mGroupsId,
            mDateSelect,
            mCourseSelect
        )
    }

    fun courseAllSetListUI(customerList: ArrayList<Customer>) {
        hideProgressDialog()

        mCustomerList = customerList

        binding.btnStartNaviGroup1.setOnClickListener {
            btnNaviStartGroup1()
        }
        binding.btnStartNaviGroup2.setOnClickListener {
            btnNaviStartGroup2()
        }
        binding.btnStartNaviGroup3.setOnClickListener {
            btnNaviStartGroup3()
        }
        binding.btnStartNaviGroup4.setOnClickListener {
            btnNaviStartGroup4()
        }
        binding.btnStartNaviGroup5.setOnClickListener {
            btnNaviStartGroup5()
        }

        when (customerList.size) {
            in 1..20 -> {
                binding.btnStartNaviGroup1.visibility = View.VISIBLE
            }

            in 21..40 -> {
                binding.btnStartNaviGroup1.visibility = View.VISIBLE
                binding.btnStartNaviGroup2.visibility = View.VISIBLE
            }

            in 41..60 -> {
                binding.btnStartNaviGroup1.visibility = View.VISIBLE
                binding.btnStartNaviGroup2.visibility = View.VISIBLE
                binding.btnStartNaviGroup3.visibility = View.VISIBLE
            }

            in 61..80 -> {
                binding.btnStartNaviGroup1.visibility = View.VISIBLE
                binding.btnStartNaviGroup2.visibility = View.VISIBLE
                binding.btnStartNaviGroup3.visibility = View.VISIBLE
                binding.btnStartNaviGroup4.visibility = View.VISIBLE
            }

            in 81..100 -> {
                binding.btnStartNaviGroup1.visibility = View.VISIBLE
                binding.btnStartNaviGroup2.visibility = View.VISIBLE
                binding.btnStartNaviGroup3.visibility = View.VISIBLE
                binding.btnStartNaviGroup4.visibility = View.VISIBLE
                binding.btnStartNaviGroup5.visibility = View.VISIBLE
            }
        }
    }

    private fun btnNaviStartGroup1() {
        var latLngStr = ""
        val lat1: Double = mCustomerList.get(0).customer_lat.toDouble()
        val lng1: Double = mCustomerList.get(0).customer_lng.toDouble()

        for (i in 1..19) {

            val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
            val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

            latLngStr = "${latLngStr}+to:${latx},${lngx}"
        }
        val str =
            "http://maps.google.com/maps?saddr=&daddr=${lat1},${lng1}${latLngStr}&dirflg=d"

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setClassName(
            "com.google.android.apps.maps",
            "com.google.android.maps.MapsActivity"
        )
        intent.setData(Uri.parse(str))
        startActivity(intent)
    }

    private fun btnNaviStartGroup2() {
        var latLngStr = ""
        val lat1: Double = mCustomerList.get(20).customer_lat.toDouble()
        val lng1: Double = mCustomerList.get(20).customer_lng.toDouble()

        for (i in 21..39) {

            val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
            val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

            latLngStr = "${latLngStr}+to:${latx},${lngx}"
        }
        val str =
            "http://maps.google.com/maps?saddr=&daddr=${lat1},${lng1}${latLngStr}&dirflg=d"

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setClassName(
            "com.google.android.apps.maps",
            "com.google.android.maps.MapsActivity"
        )
        intent.setData(Uri.parse(str))
        startActivity(intent)
    }

    private fun btnNaviStartGroup3() {
        var latLngStr = ""
        val lat1: Double = mCustomerList.get(40).customer_lat.toDouble()
        val lng1: Double = mCustomerList.get(40).customer_lng.toDouble()

        for (i in 41..59) {

            val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
            val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

            latLngStr = "${latLngStr}+to:${latx},${lngx}"
        }
        val str =
            "http://maps.google.com/maps?saddr=&daddr=${lat1},${lng1}${latLngStr}&dirflg=d"

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setClassName(
            "com.google.android.apps.maps",
            "com.google.android.maps.MapsActivity"
        )
        intent.setData(Uri.parse(str))
        startActivity(intent)
    }

    private fun btnNaviStartGroup4() {
        var latLngStr = ""
        val lat1: Double = mCustomerList.get(60).customer_lat.toDouble()
        val lng1: Double = mCustomerList.get(60).customer_lng.toDouble()

        for (i in 61..79) {

            val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
            val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

            latLngStr = "${latLngStr}+to:${latx},${lngx}"
        }
        val str =
            "http://maps.google.com/maps?saddr=&daddr=${lat1},${lng1}${latLngStr}&dirflg=d"

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setClassName(
            "com.google.android.apps.maps",
            "com.google.android.maps.MapsActivity"
        )
        intent.setData(Uri.parse(str))
        startActivity(intent)
    }

    private fun btnNaviStartGroup5() {

        var latLngStr = ""
        val lat1: Double = mCustomerList.get(80).customer_lat.toDouble()
        val lng1: Double = mCustomerList.get(80).customer_lng.toDouble()

        for (i in 81..99) {

            val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
            val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

            latLngStr = "${latLngStr}+to:${latx},${lngx}"
        }
        val str =
            "http://maps.google.com/maps?saddr=&daddr=${lat1},${lng1}${latLngStr}&dirflg=d"

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setClassName(
            "com.google.android.apps.maps",
            "com.google.android.maps.MapsActivity"
        )
        intent.setData(Uri.parse(str))
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        getCourseAllSetList()
    }
}