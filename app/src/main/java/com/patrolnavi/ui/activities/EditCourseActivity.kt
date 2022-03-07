package com.patrolnavi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.patrolnavi.R
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Customer
import com.patrolnavi.ui.adapters.CustomerEditListAdapter
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_edit_course.*
import kotlinx.android.synthetic.main.activity_edit_customer.*

class EditCourseActivity : BaseActivity() {

    private lateinit var mCustomerList: ArrayList<Customer>
    private var mDateSelect: String = ""
    private var mCourseSelect: String = ""
    private var mGroupsId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_course)

        if (intent.hasExtra(Constants.EXTRA_DATE_SELECT)) {
            mDateSelect = intent.getStringExtra(Constants.EXTRA_DATE_SELECT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_COURSE_SELECT)) {
            mCourseSelect = intent.getStringExtra(Constants.EXTRA_COURSE_SELECT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }

        Log.i(javaClass.simpleName, "groupsId: ${mGroupsId}")

        setupActionBar()

    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_course_edit_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        toolbar_course_edit_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater

        inflater.inflate(R.menu.add_customer_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

            R.id.navigation_add_customer -> {
                val intent = Intent(this@EditCourseActivity, AddCustomerActivity::class.java)
                intent.putExtra(Constants.EXTRA_DATE_SELECT, mDateSelect)
                intent.putExtra(Constants.EXTRA_COURSE_SELECT, mCourseSelect)
                intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getCustomerList() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCustomerList(
            this@EditCourseActivity,
            mGroupsId,
            mDateSelect,
            mCourseSelect
        )
    }

    fun successCustomerList(customerList: ArrayList<Customer>) {
        hideProgressDialog()

        mCustomerList = customerList

        if (customerList.size > 0) {
            rv_course_edit.visibility = View.VISIBLE
            tv_course_edit_found.visibility = View.GONE

            rv_course_edit.layoutManager = LinearLayoutManager(this@EditCourseActivity)
            rv_course_edit.setHasFixedSize(true)

            val customerEditAdapter =
                CustomerEditListAdapter(this@EditCourseActivity, mCustomerList)
            rv_course_edit.adapter = customerEditAdapter

        } else {
            rv_course_edit.visibility = View.GONE
            tv_course_edit_found.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getCustomerList()
    }
}