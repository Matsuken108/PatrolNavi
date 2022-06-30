package com.patrolnavi.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivityDetailsCustomerBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Customer
import com.patrolnavi.models.Groups
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_details_customer.*

class DetailsCustomerActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailsCustomerBinding

    private var mGroupsId: String = ""
    private var mCustomerId: String = ""
    private lateinit var mCustomerDetails: Customer
    private lateinit var mGroups: Groups
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""
    private var mDateSelect: String = ""
    private var mCourseSelect: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_CUSTOMER_ID)) {
            mCustomerId = intent.getStringExtra(Constants.EXTRA_CUSTOMER_ID)!!
        }

        Log.i(javaClass.simpleName, "DetailsCustomer Top groupsId : ${mGroupsId}")

        getGroupsCenter()

        binding.ivDetailsCustomerHome.setOnClickListener(this)

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCustomerDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }
        binding.toolbarCustomerDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.iv_details_customer_home -> {
                    val intent =
                        Intent(this@DetailsCustomerActivity, SettingCourseActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun getGroupsCenter() {

        FirestoreClass().getGroupsDetails(this@DetailsCustomerActivity, mGroupsId)
    }

    fun getGroupsDetailsSuccess(groups: Groups) {

        mGroups = groups

        mGroupsLat = mGroups.groups_lat
        mGroupsLng = mGroups.groups_lng

        getCustomerDetails()
    }

    private fun getCustomerDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCustomerDetails(this@DetailsCustomerActivity, mGroupsId, mCustomerId)
    }

    fun customerDetailsSuccess(customer: Customer) {
        mCustomerDetails = customer

        hideProgressDialog()

        mDateSelect = customer.date
        mCourseSelect = customer.course

        binding.tvCustomerDetailsDate.isEnabled = false
        binding.tvCustomerDetailsDate.setText(customer.date)
        binding.tvCustomerDetailsCourse.isEnabled = false
        binding.tvCustomerDetailsCourse.setText(customer.course)
        binding.tvCustomerDetailsNo.isEnabled = false
        binding.tvCustomerDetailsNo.setText(customer.no.toString())
        binding.tvCustomerDetailsFirstName.isEnabled = false
        binding.tvCustomerDetailsFirstName.setText(customer.firstName)
        binding.tvCustomerDetailsLastName.isEnabled = false
        binding.tvCustomerDetailsLastName.setText(customer.lastName)
        binding.tvCustomerDetailsLat.isEnabled = false
        binding.tvCustomerDetailsLat.setText(customer.customer_lat)
        binding.tvCustomerDetailsLng.isEnabled = false
        binding.tvCustomerDetailsLng.setText(customer.customer_lng)
//         GlideLoader(context).loadProductPicture(mCustomerDetails.installationImage1,iv_installation_image1)
//         GlideLoader(context).loadProductPicture(mCustomerDetails.installationImage2,iv_installation_image2)
        binding.tvCustomerDetailsMemo.isEnabled = false
        binding.tvCustomerDetailsMemo.setText(customer.memo)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater

        inflater.inflate(R.menu.edit_customer_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

            R.id.navigation_edit_customer -> {
                val intent = Intent(this@DetailsCustomerActivity, EditCustomerActivity::class.java)
                intent.putExtra(Constants.EXTRA_CUSTOMER_DETAILS, mCustomerDetails)
                intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
                intent.putExtra(Constants.EXTRA_CUSTOMER_ID, mCustomerId)
                intent.putExtra(Constants.EXTRA_GROUPS_LAT, mGroupsLat)
                intent.putExtra(Constants.EXTRA_GROUPS_LNG, mGroupsLng)
                startActivity(intent)
                return true
            }

            R.id.navigation_delete_customer -> {
                deleteCustomer(mCustomerId)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun deleteCustomer(customerID: String) {
        showAlertToDeleteCustomer(customerID)
    }

    private fun showAlertToDeleteCustomer(customerID: String) {
        val builder = AlertDialog.Builder(this@DetailsCustomerActivity)

        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            showProgressDialog(resources.getString(R.string.please_wait))

            FirestoreClass().deleteCustomer(this, mGroupsId, customerID)

            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    fun customerDeleteSuccess() {
        hideProgressDialog()

        Toast.makeText(this, "お客様情報を削除しました", Toast.LENGTH_SHORT).show()

        val intent = Intent(this@DetailsCustomerActivity, EditCourseActivity::class.java)
        intent.putExtra(Constants.EXTRA_DATE_SELECT, mDateSelect)
        intent.putExtra(Constants.EXTRA_COURSE_SELECT, mCourseSelect)
        intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
        intent.putExtra(Constants.EXTRA_GROUPS_LAT, mGroupsLat)
        intent.putExtra(Constants.EXTRA_GROUPS_LNG, mGroupsLng)
        startActivity(intent)

        finish()
    }


}