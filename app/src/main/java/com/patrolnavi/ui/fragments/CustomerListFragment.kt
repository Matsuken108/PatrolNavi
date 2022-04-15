package com.patrolnavi.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.patrolnavi.R
import com.patrolnavi.databinding.FragmentCustomerListBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Customer
import com.patrolnavi.ui.adapters.CustomerListAdapter
import kotlinx.android.synthetic.main.fragment_customer_list.*
import java.text.Format
import java.util.*
import kotlin.collections.ArrayList


class CustomerListFragment : BaseFragment() {

    private var _binding : FragmentCustomerListBinding? = null
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
        _binding = FragmentCustomerListBinding.inflate(inflater,container,false)

        val extras: Bundle?
        val intent = activity?.intent

        extras = intent?.extras

        mDateSelect = extras?.getString("dateSelect").toString()
        mCourseSelect = extras?.getString("courseSelect").toString()
        mGroupsId = extras?.getString("groupsId").toString()

        return binding.root
    }

    fun courseAllSetListUI(customerList: ArrayList<Customer>) {
        hideProgressDialog()

        mCustomerList = customerList

        var latLngStr: String = ""

        if (customerList.size > 0) {
            binding.rvCourseList.visibility = View.VISIBLE
            binding.tvCourseListFound.visibility = View.GONE
            binding.btnStartNavi.visibility = View.VISIBLE

            binding.rvCourseList.layoutManager = LinearLayoutManager(activity)
            binding.rvCourseList.setHasFixedSize(true)

            val courseListAdapter = CustomerListAdapter(requireActivity(), customerList)
            binding.rvCourseList.adapter = courseListAdapter

            binding.btnStartNavi.setOnClickListener {

                val lat1: Double = mCustomerList.get(0).customer_lat.toDouble()
                val lng1: Double = mCustomerList.get(0).customer_lng.toDouble()

                for (i in 1..mCustomerList.size - 1) {

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

        } else {
            binding.rvCourseList.visibility = View.GONE
            binding.tvCourseListFound.visibility = View.VISIBLE
            binding.btnStartNavi.visibility = View.GONE
        }

    }


    private fun getCourseAllSetList() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCourseAllSetList(this@CustomerListFragment,mGroupsId, mDateSelect, mCourseSelect)
    }

    override fun onResume() {
        super.onResume()
        getCourseAllSetList()
    }
}
