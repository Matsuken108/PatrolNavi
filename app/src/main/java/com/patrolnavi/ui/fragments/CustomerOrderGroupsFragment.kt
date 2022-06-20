package com.patrolnavi.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.patrolnavi.R
import com.patrolnavi.databinding.FragmentCustomerGroupsBinding
import com.patrolnavi.databinding.FragmentCustomerOrderGroupsBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Customer

class CustomerOrderGroupsFragment : BaseFragment() {

    private var _binding: FragmentCustomerOrderGroupsBinding? = null
    private val binding get() = _binding!!

    private val args: CustomerOrderGroupsFragmentArgs by navArgs()
    private var mGroupsId: String = ""
    private var mDateSelect: String = ""
    private var mCourseSelect: String = ""
    private lateinit var mCustomerList: ArrayList<Customer>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerOrderGroupsBinding.inflate(inflater, container, false)

        val extras: Bundle?
        val intent = activity?.intent

        extras = intent?.extras

        mDateSelect = extras?.getString("dateSelect").toString()
        mCourseSelect = extras?.getString("courseSelect").toString()
        mGroupsId = extras?.getString("groupsId").toString()

        binding.btnStartNaviOrderGroup1.setOnClickListener {
            btnNaviStartGroup1()
        }
        binding.btnStartNaviOrderGroup2.setOnClickListener {
            btnNaviStartGroup2()
        }
        binding.btnStartNaviOrderGroup3.setOnClickListener {
            btnNaviStartGroup3()
        }
        binding.btnStartNaviOrderGroup4.setOnClickListener {
            btnNaviStartGroup4()
        }
        binding.btnStartNaviOrderGroup5.setOnClickListener {
            btnNaviStartGroup5()
        }

        return binding.root
    }

    private fun getCourseAllSetList() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCourseAllSetList(
            this@CustomerOrderGroupsFragment,
            mGroupsId,
            mDateSelect,
            mCourseSelect
        )
    }

    fun courseAllSetListUI(customerList: ArrayList<Customer>) {
        hideProgressDialog()

        mCustomerList = customerList

        val customer = args.customer
        val customerNumber: Int = mCustomerList.indexOf(customer)

        when (customerList.size) {
            in 1..20 -> {
                binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
            }

            in 21..40 -> {
                when (customerNumber) {
                    in 1..20 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup2.visibility = View.VISIBLE
                    }
                    in 21..40 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                    }
                }

            }

            in 41..60 -> {
                when (customerNumber) {
                    in 1..20 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup2.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup3.visibility = View.VISIBLE
                    }
                    in 21..40 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup2.visibility = View.VISIBLE
                    }
                    in 41..60 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                    }
                }
            }

            in 61..80 -> {
                when (customerNumber) {
                    in 1..20 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup2.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup3.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup4.visibility = View.VISIBLE
                    }
                    in 21..40 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup2.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup3.visibility = View.VISIBLE
                    }
                    in 41..60 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup2.visibility = View.VISIBLE
                    }
                    in 61..80 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                    }
                }
            }

            in 81..100 -> {
                when (customerNumber) {
                    in 1..20 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup2.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup3.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup4.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup5.visibility = View.VISIBLE
                    }
                    in 21..40 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup2.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup3.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup4.visibility = View.VISIBLE
                    }
                    in 41..60 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup2.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup3.visibility = View.VISIBLE
                    }
                    in 61..80 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                        binding.btnStartNaviOrderGroup2.visibility = View.VISIBLE
                    }
                    in 81..99 -> {
                        binding.btnStartNaviOrderGroup1.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun btnNaviStartGroup1() {
        var latLngStr: String = ""

        val customer = args.customer

        val customerNumber: Int = mCustomerList.indexOf(customer)

        Log.i(javaClass.simpleName, "index : ${customerNumber}")

            val lat1: Double = mCustomerList.get(customerNumber).customer_lat.toDouble()
            val lng1: Double = mCustomerList.get(customerNumber).customer_lng.toDouble()

            if(mCustomerList.size > 19) {
                for (i in customerNumber + 1..customerNumber + 19) {
//                for (i in customerNumber..mCustomerList.size - 1) {

                    val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
                    val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

                    latLngStr = "${latLngStr}+to:${latx},${lngx}"
                }
            }else{
                for (i in customerNumber + 1..mCustomerList.size) {

                    val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
                    val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

                    latLngStr = "${latLngStr}+to:${latx},${lngx}"
                }
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
            var latLngStr: String = ""

            val customer = args.customer

            val customerNumber: Int = mCustomerList.indexOf(customer)

            Log.i(javaClass.simpleName, "index : ${customerNumber}")

                val lat1: Double = mCustomerList.get(customerNumber + 20).customer_lat.toDouble()
                val lng1: Double = mCustomerList.get(customerNumber + 20).customer_lng.toDouble()

                if(mCustomerList.size > 39) {
                    for (i in customerNumber + 21..customerNumber + 39) {

                        val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
                        val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

                        latLngStr = "${latLngStr}+to:${latx},${lngx}"
                    }
                }else{
                        for (i in customerNumber + 21..mCustomerList.size) {

                            val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
                            val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

                            latLngStr = "${latLngStr}+to:${latx},${lngx}"
                        }
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
            var latLngStr: String = ""

            val customer = args.customer

            val customerNumber: Int = mCustomerList.indexOf(customer)

            Log.i(javaClass.simpleName, "index : ${customerNumber}")

                val lat1: Double = mCustomerList.get(customerNumber + 40).customer_lat.toDouble()
                val lng1: Double = mCustomerList.get(customerNumber + 40).customer_lng.toDouble()

            if(mCustomerList.size > 59) {
                for (i in customerNumber + 41..customerNumber + 59) {

                    val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
                    val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

                    latLngStr = "${latLngStr}+to:${latx},${lngx}"
                }
            }else{
                for (i in customerNumber + 41..mCustomerList.size) {

                    val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
                    val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

                    latLngStr = "${latLngStr}+to:${latx},${lngx}"
                }
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
            var latLngStr: String = ""

            val customer = args.customer

            val customerNumber: Int = mCustomerList.indexOf(customer)

            Log.i(javaClass.simpleName, "index : ${customerNumber}")

                val lat1: Double = mCustomerList.get(customerNumber + 60).customer_lat.toDouble()
                val lng1: Double = mCustomerList.get(customerNumber + 60).customer_lng.toDouble()

            if(mCustomerList.size > 79) {
                for (i in customerNumber + 61..customerNumber + 79) {

                    val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
                    val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

                    latLngStr = "${latLngStr}+to:${latx},${lngx}"
                }
            }else{
                for (i in customerNumber + 61..mCustomerList.size) {

                    val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
                    val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

                    latLngStr = "${latLngStr}+to:${latx},${lngx}"
                }
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

            var latLngStr: String = ""

            val customer = args.customer

            val customerNumber: Int = mCustomerList.indexOf(customer)

            Log.i(javaClass.simpleName, "index : ${customerNumber}")

                val lat1: Double = mCustomerList.get(customerNumber + 80).customer_lat.toDouble()
                val lng1: Double = mCustomerList.get(customerNumber + 80).customer_lng.toDouble()

            if(mCustomerList.size > 99) {
                for (i in customerNumber + 81..customerNumber + 99) {

                    val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
                    val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

                    latLngStr = "${latLngStr}+to:${latx},${lngx}"
                }
            }else{
                for (i in customerNumber + 81..mCustomerList.size) {

                    val latx: Double = mCustomerList.get(i).customer_lat.toDouble()
                    val lngx: Double = mCustomerList.get(i).customer_lng.toDouble()

                    latLngStr = "${latLngStr}+to:${latx},${lngx}"
                }
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
