package com.patrolnavi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.patrolnavi.R
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Customer
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_edit_customer.*

class EditCustomerActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mCustomerDetails: Customer
    private var mCustomerId: String = ""
    private var mGroupsId: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_customer)

        if (intent.hasExtra(Constants.EXTRA_CUSTOMER_DETAILS)) {
            mCustomerDetails = intent.getParcelableExtra(Constants.EXTRA_CUSTOMER_DETAILS)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_CUSTOMER_ID)) {
            mCustomerId = intent.getStringExtra(Constants.EXTRA_CUSTOMER_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_LAT)) {
            mGroupsLat = intent.getStringExtra(Constants.EXTRA_GROUPS_LAT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_LNG)) {
            mGroupsLng = intent.getStringExtra(Constants.EXTRA_GROUPS_LNG)!!
        }

        Log.i(javaClass.simpleName, "detail:${mCustomerDetails} customerId: ${mCustomerId}")

        val editCustomerNo = mCustomerDetails.no.toString()

        et_edit_customer_date.setText(mCustomerDetails.date)
        et_edit_customer_course.setText(mCustomerDetails.course)
        et_edit_customer_no.setText(editCustomerNo)
        et_edit_customer_first_name.setText(mCustomerDetails.firstName)
        et_edit_customer_last_name.setText(mCustomerDetails.lastName)
        et_edit_customer_lat.isEnabled = false
        et_edit_customer_lat.setText(mCustomerDetails.customer_lat)
        et_edit_customer_lng.isEnabled = false
        et_edit_customer_lng.setText(mCustomerDetails.customer_lng)
//        GlideLoader(context).loadProductPicture(mCustomerDetails.installationImage1,iv_installation_image1)
//        GlideLoader(context).loadProductPicture(mCustomerDetails.installationImage2,iv_installation_image2)
        et_edit_customer_memo.setText(mCustomerDetails.memo)


        btn_edit_customer_update.setOnClickListener(this@EditCustomerActivity)

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_edit_customer_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        toolbar_edit_customer_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btn_edit_customer_update -> {
                    if (validateCustomerProfileDetails()) {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        updateCustomerDetails()
                    }
                }
                R.id.btn_edit_customer_location -> {
                    val intent =
                        Intent(this@EditCustomerActivity, EditCustomerMapsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_GROUPS_ID,mGroupsId)
                    intent.putExtra(Constants.EXTRA_CUSTOMER_ID,mCustomerId)
                    intent.putExtra(Constants.EXTRA_CUSTOMER_DETAILS,mCustomerDetails)
                    intent.putExtra(Constants.EXTRA_GROUPS_LAT,mGroupsLat)
                    intent.putExtra(Constants.EXTRA_GROUPS_LNG,mGroupsLng)
                    startActivity(intent)
                }
            }
        }
    }

    private fun updateCustomerDetails() {
        val customerHashMap = HashMap<String, Any>()

        val date = et_edit_customer_date.text.toString().trim { it <= ' ' }
        if (date != mCustomerDetails.date) {
            customerHashMap[Constants.DATE] = date
        }

        val course = et_edit_customer_course.text.toString().trim { it <= ' ' }
        if (course != mCustomerDetails.course) {
            customerHashMap[Constants.COURSE] = course
        }

        val no = et_edit_customer_no.text.toString().trim { it <= ' ' }
        if (no != mCustomerDetails.no.toString()) {
            customerHashMap[Constants.NO] = no.toInt()
        }

        val firstName = et_edit_customer_first_name.text.toString().trim { it <= ' ' }
        if (firstName != mCustomerDetails.firstName) {
            customerHashMap[Constants.FIRST_NAME] = firstName
        }

        val lastName = et_edit_customer_last_name.text.toString().trim { it <= ' ' }
        if (lastName != mCustomerDetails.lastName) {
            customerHashMap[Constants.LAST_NAME] = lastName
        }

        val lat = et_edit_customer_lat.text.toString().trim { it <= ' ' }
        if (lat != mCustomerDetails.customer_lat) {
            customerHashMap[Constants.LATLNG] = lat
        }

        val lng = et_edit_customer_lng.text.toString().trim { it <= ' ' }
        if (lng != mCustomerDetails.customer_lng) {
            customerHashMap[Constants.LATLNG] = lng
        }

        val memo = et_edit_customer_memo.text.toString().trim { it <= ' ' }
        if (memo != mCustomerDetails.memo) {
            customerHashMap[Constants.MEMO] = memo
        }

        FirestoreClass().updateCustomerDetails(
            this@EditCustomerActivity,
            mGroupsId,
            customerHashMap, mCustomerId
        )
    }

    private fun validateCustomerProfileDetails(): Boolean {
        return when {

            TextUtils.isEmpty(et_edit_customer_date.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_date), true)
                false
            }

            TextUtils.isEmpty(et_edit_customer_course.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_course), true)
                false
            }

            TextUtils.isEmpty(et_edit_customer_no.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_no), true)
                false
            }

            TextUtils.isEmpty(et_edit_customer_first_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_customer_first_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_edit_customer_last_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_customer_last_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_edit_customer_lat.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_lat), true)
                false
            }

            TextUtils.isEmpty(et_edit_customer_lng.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_lng), true)
                false
            }

            else -> {
                true
            }
        }
    }

    fun customerDetailsUpdateSuccess() {
        hideProgressDialog()

        Toast.makeText(
            this@EditCustomerActivity,
            resources.getString(R.string.msg_customer_details_update_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@EditCustomerActivity, EditCourseActivity::class.java))
        finish()
    }
}