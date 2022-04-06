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
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Customer
import com.patrolnavi.ui.adapters.CustomerListAdapter
import kotlinx.android.synthetic.main.fragment_customer_list.*

class DetailsCustomerFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_customer_list, container, false)


        return root
    }



}
