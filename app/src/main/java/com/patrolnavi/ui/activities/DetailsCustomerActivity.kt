package com.patrolnavi.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.patrolnavi.R
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Customer
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_details_customer.*

class DetailsCustomerActivity : BaseActivity() {

    private var mGroupsId: String = ""
    private var mCustomerId: String = ""
    private lateinit var mCustomerDetails: Customer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_customer)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }

        if (intent.hasExtra(Constants.EXTRA_CUSTOMER_ID)) {
            mCustomerId = intent.getStringExtra(Constants.EXTRA_CUSTOMER_ID)!!
        }
        getCustomerDetails()
    }

    private fun getCustomerDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCustomerDetails(this, mCustomerId, mGroupsId)
    }

    fun customerDetailsSuccess(customer: Customer) {
        mCustomerDetails = customer

        hideProgressDialog()

        tv_customer_details_date.isEnabled = false
        tv_customer_details_date.setText(customer.date)
        tv_customer_details_course.isEnabled = false
        tv_customer_details_course.setText(customer.course)
        tv_customer_details_no.isEnabled = false
        tv_customer_details_no.setText(customer.no.toString())
        tv_customer_details_first_name.isEnabled = false
        tv_customer_details_first_name.setText(customer.firstName)
        tv_customer_details_last_name.isEnabled = false
        tv_customer_details_last_name.setText(customer.lastName)
        tv_customer_details_lat.isEnabled = false
        tv_customer_details_lat.setText(customer.lat)
        tv_customer_details_lng.isEnabled = false
        tv_customer_details_lng.setText(customer.lng)
//         GlideLoader(context).loadProductPicture(mCustomerDetails.installationImage1,iv_installation_image1)
//         GlideLoader(context).loadProductPicture(mCustomerDetails.installationImage2,iv_installation_image2)
        tv_customer_details_memo.isEnabled = false
        tv_customer_details_memo.setText(customer.memo)
    }


    private fun setupActionBar() {

        setSupportActionBar(toolbar_customer_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        toolbar_customer_details_activity.setNavigationOnClickListener { onBackPressed() }
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

        finish()
    }

}