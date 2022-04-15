package com.patrolnavi.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.patrolnavi.R
import com.patrolnavi.databinding.FragmentCustomerListBinding
import com.patrolnavi.databinding.FragmentDetailsCustomerBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Customer
import com.patrolnavi.ui.adapters.CustomerListAdapter
import kotlinx.android.synthetic.main.fragment_customer_list.*
import kotlinx.android.synthetic.main.fragment_details_customer.*

class DetailsCustomerFragment : BaseFragment() {

    private var _binding : FragmentDetailsCustomerBinding? = null
    private val binding get() = _binding!!

    private val args: DetailsCustomerFragmentArgs by navArgs()
    private var mGroupsId: String = ""
    private var mDateSelect: String = ""
    private var mCourseSelect: String = ""
    private lateinit var mCustomerList: ArrayList<Customer>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsCustomerBinding.inflate(inflater,container,false)

        val customer = args.customer
        Log.i(javaClass.simpleName,"customer ${customer}")

        binding.tvCustomerDetailsFragmentFirstName.setText(customer.firstName)  //TODO NullPoint
        binding.tvCustomerDetailsFragmentLastName.setText(customer.lastName)
        binding.tvCustomerDetailsFragmentMemo.setText(customer.memo)

        val extras: Bundle?
        val intent = activity?.intent

        extras = intent?.extras

        mGroupsId = extras?.getString("groupsId").toString()
        mDateSelect = extras?.getString("dateSelect").toString()
        mCourseSelect = extras?.getString("courseSelect").toString()

        binding.btnStartSingleNavi.setOnClickListener { singleNavigation() }
        binding.btnStartOrderNavi.setOnClickListener { getCourseSetList() }
        binding.btnIntentMap.setOnClickListener { singleCustomerMap() }

        return binding.root
    }

    private fun singleCustomerMap(){

        findNavController().navigate(R.id.action_detailsCustomerFragment_to_singleCustomerMapFragment)

    }

    private fun singleNavigation(){

        val lat: Double = args.customer.customer_lat.toDouble()
        val lng: Double = args.customer.customer_lng.toDouble()

        val str =
            "http://maps.google.com/maps?saddr=&daddr=${lat},${lng}&dirflg=d"

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setClassName(
            "com.google.android.apps.maps",
            "com.google.android.maps.MapsActivity"
        )
        intent.setData(Uri.parse(str))
        startActivity(intent)

    }

    fun courseSetListUI(customerList: ArrayList<Customer>) {
        hideProgressDialog()

        mCustomerList = customerList

        var latLngStr: String = ""

        //TODO for文で繰り返すために何番目かを指定する必要あり

//        val customerId : String = args.customerId

        val customerNumber : Int = 2
//        mCustomerList.indexOf()

        Log.i(javaClass.simpleName,"index : ${customerNumber}")

        if (customerList.size > 0) {

            val lat1: Double = mCustomerList.get(customerNumber).customer_lat.toDouble()
            val lng1: Double = mCustomerList.get(customerNumber).customer_lng.toDouble()

            for (i in customerNumber..mCustomerList.size - 1) {

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
    }


    private fun getCourseSetList() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCourseSetList(
            this@DetailsCustomerFragment,
            mGroupsId,
            mDateSelect,
            mCourseSelect
        )
    }

}
