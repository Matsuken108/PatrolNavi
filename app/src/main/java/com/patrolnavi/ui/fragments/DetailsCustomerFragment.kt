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

    private var _binding: FragmentDetailsCustomerBinding? = null
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
        _binding = FragmentDetailsCustomerBinding.inflate(inflater, container, false)

        val customer = args.customer
        Log.i(javaClass.simpleName, "customer ${customer}")

        binding.tvCustomerDetailsFragmentFirstName.isEnabled = false
        binding.tvCustomerDetailsFragmentFirstName.setText(customer.firstName)
        binding.tvCustomerDetailsFragmentLastName.isEnabled = false
        binding.tvCustomerDetailsFragmentLastName.setText(customer.lastName)
        binding.tvCustomerDetailsFragmentMemo.isEnabled = false
        binding.tvCustomerDetailsFragmentMemo.setText(customer.memo)

        val extras: Bundle?
        val intent = activity?.intent

        extras = intent?.extras

        mGroupsId = extras?.getString("groupsId").toString()
        mDateSelect = extras?.getString("dateSelect").toString()
        mCourseSelect = extras?.getString("courseSelect").toString()

        binding.btnStartSingleNavi.setOnClickListener { singleNavigation() }
        binding.btnStartOrderNavi.setOnClickListener { orderNavigation() }
        binding.btnIntentMap.setOnClickListener { singleCustomerMap() }

        return binding.root
    }

    private fun singleCustomerMap() {

        val customer = args.customer

        val action =
            DetailsCustomerFragmentDirections.actionDetailsCustomerFragmentToSingleCustomerMapFragment(
                customer
            )
        findNavController().navigate(action)

    }

    private fun singleNavigation() {

        val lat: Double = args.customer.customer_lat.toDouble()
        val lng: Double = args.customer.customer_lng.toDouble()

        Log.i(javaClass.simpleName, "Lat : ${lat} Lng : ${lng}")

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

    private fun orderNavigation() {
        val customer = args.customer

        val action =
            DetailsCustomerFragmentDirections.actionDetailsCustomerFragmentToCustomerOrderGroupsFragment(
                customer
            )
        findNavController().navigate(action)
    }

}

