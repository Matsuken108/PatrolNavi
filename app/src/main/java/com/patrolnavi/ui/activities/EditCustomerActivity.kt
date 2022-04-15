package com.patrolnavi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivityEditCustomerBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Customer
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_edit_customer.*

class EditCustomerActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityEditCustomerBinding

    private lateinit var mCustomerDetails: Customer
    private var mCustomerId: String = ""
    private var mGroupsId: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.etEditCustomerDate.setText(mCustomerDetails.date)
        binding.etEditCustomerCourse.setText(mCustomerDetails.course)
        binding.etEditCustomerNo.setText(editCustomerNo)
        binding.etEditCustomerFirstName.setText(mCustomerDetails.firstName)
        binding.etEditCustomerLastName.setText(mCustomerDetails.lastName)
        binding.etEditCustomerLat.isEnabled = false
        binding.etEditCustomerLat.setText(mCustomerDetails.customer_lat)
        binding.etEditCustomerLng.isEnabled = false
        binding.etEditCustomerLng.setText(mCustomerDetails.customer_lng)
//        GlideLoader(context).loadProductPicture(mCustomerDetails.installationImage1,iv_installation_image1)
//        GlideLoader(context).loadProductPicture(mCustomerDetails.installationImage2,iv_installation_image2)
        binding.etEditCustomerMemo.setText(mCustomerDetails.memo)

        binding.btnEditCustomerUpdate.setOnClickListener(this@EditCustomerActivity)

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarEditCustomerActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        binding.toolbarEditCustomerActivity.setNavigationOnClickListener { onBackPressed() }
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

        val date = binding.etEditCustomerDate.text.toString().trim { it <= ' ' }
        if (date != mCustomerDetails.date) {
            customerHashMap[Constants.DATE] = date
        }

        val course = binding.etEditCustomerCourse.text.toString().trim { it <= ' ' }
        if (course != mCustomerDetails.course) {
            customerHashMap[Constants.COURSE] = course
        }

        val no = binding.etEditCustomerNo.text.toString().trim { it <= ' ' }
        if (no != mCustomerDetails.no.toString()) {
            customerHashMap[Constants.NO] = no.toInt()
        }

        val firstName = binding.etEditCustomerFirstName.text.toString().trim { it <= ' ' }
        if (firstName != mCustomerDetails.firstName) {
            customerHashMap[Constants.FIRST_NAME] = firstName
        }

        val lastName = binding.etEditCustomerLastName.text.toString().trim { it <= ' ' }
        if (lastName != mCustomerDetails.lastName) {
            customerHashMap[Constants.LAST_NAME] = lastName
        }

        val lat = binding.etEditCustomerLat.text.toString().trim { it <= ' ' }
        if (lat != mCustomerDetails.customer_lat) {
            customerHashMap[Constants.LATLNG] = lat
        }

        val lng = binding.etEditCustomerLng.text.toString().trim { it <= ' ' }
        if (lng != mCustomerDetails.customer_lng) {
            customerHashMap[Constants.LATLNG] = lng
        }

        val memo = binding.etEditCustomerLng.text.toString().trim { it <= ' ' }
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

            TextUtils.isEmpty(binding.etEditCustomerDate.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_date), true)
                false
            }

            TextUtils.isEmpty(binding.etEditCustomerCourse.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_course), true)
                false
            }

            TextUtils.isEmpty(binding.etEditCustomerNo.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_no), true)
                false
            }

            TextUtils.isEmpty(binding.etEditCustomerFirstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_customer_first_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.etEditCustomerLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_customer_last_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.etEditCustomerLat.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_lat), true)
                false
            }

            TextUtils.isEmpty(binding.etEditCustomerLng.text.toString().trim { it <= ' ' }) -> {
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