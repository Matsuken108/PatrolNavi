package com.patrolnavi.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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

    //TODO 経由地の数に上限あり！分割して遷移すること！

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
                findNavController().navigate(R.id.action_navigation_customer_list_to_customerGroupsFragment)
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
