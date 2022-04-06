package com.patrolnavi.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.patrolnavi.R
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.User
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_edit_user_profile.*

class EditUserProfileActivity : BaseActivity(),View.OnClickListener {

    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_profile)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_edit_user_profile_first_name.setText(mUserDetails.firstName)
        et_edit_user_profile_last_name.setText(mUserDetails.lastName)
        et_edit_user_profile_email.isEnabled = false
        et_edit_user_profile_email.setText(mUserDetails.email)
        et_edit_user_profile_mobile_number.setText(mUserDetails.mobile.toString())

        btn_edit_user_profile_submit.setOnClickListener(this)

        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_edit_user_profile_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }
        toolbar_edit_user_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_edit_user_profile_submit -> {
                    if (validateUserProfileDetails()) {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        updateUserProfileDetails()
                    }
                }
            }
        }
    }


    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()

        val firstName = et_edit_user_profile_first_name.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstName) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }

        val lastName = et_edit_user_profile_last_name.text.toString().trim { it <= ' ' }
        if (lastName != mUserDetails.lastName) {
            userHashMap[Constants.LAST_NAME] = lastName
        }

        val mobileNumber = et_edit_user_profile_mobile_number.text.toString().trim { it <= ' ' }

        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        FirestoreClass().updateUserProfileData(
            this@EditUserProfileActivity,
            userHashMap
        )
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()

        Toast.makeText(
            this@EditUserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@EditUserProfileActivity, SettingCourseActivity::class.java))
        finish()
    }


    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_edit_user_profile_mobile_number.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter mobile number", true)
                false
            }
            else -> {
                true
            }
        }
    }
}