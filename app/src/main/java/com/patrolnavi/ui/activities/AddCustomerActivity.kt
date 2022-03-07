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
import kotlinx.android.synthetic.main.activity_add_customer.*
import kotlinx.android.synthetic.main.activity_edit_customer.*

class AddCustomerActivity : BaseActivity(), View.OnClickListener {

//    private var mSelectedImageFileURI: Uri? = null
//    private var mCustomerImageURL:String =""

    private var mGroupsId :String =""
    private var mDateSelect: String = ""
    private var mCourseSelect: String = ""
    private var mNo: String = ""
    private var mFirstName: String = ""
    private var mLastName: String = ""
    private var mlat: String = ""
    private var mlng: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_DATE_SELECT)) {
            mDateSelect = intent.getStringExtra(Constants.EXTRA_DATE_SELECT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_COURSE_SELECT)) {
            mCourseSelect = intent.getStringExtra(Constants.EXTRA_COURSE_SELECT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_NO_SELECT)) {
            mNo = intent.getStringExtra(Constants.EXTRA_NO_SELECT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_FIRST_NAME)) {
            mFirstName = intent.getStringExtra(Constants.EXTRA_FIRST_NAME)!!
        }
        if (intent.hasExtra(Constants.EXTRA_LAST_NAME)) {
            mLastName = intent.getStringExtra(Constants.EXTRA_LAST_NAME)!!
        }
        if (intent.hasExtra(Constants.EXTRA_LAT_SELECT)) {
            mlat = intent.getStringExtra(Constants.EXTRA_LAT_SELECT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_LNG_SELECT)) {
            mlng = intent.getStringExtra(Constants.EXTRA_LNG_SELECT)!!
        }

        Log.i(javaClass.simpleName,"Add groupsId: ${mGroupsId}")


//        et_add_customer_date.isEnabled = false
        et_add_customer_date.setText(mDateSelect)
//        et_add_customer_course.isEnabled = false
        et_add_customer_course.setText(mCourseSelect)

        et_add_customer_lat.setText(mlat)
        et_add_customer_lng.setText(mlng)
        et_add_customer_no.setText(mNo)
        et_add_customer_first_name.setText(mFirstName)
        et_add_customer_last_name.setText(mLastName)

        setupActionBar()

        btn_add_customer_save.setOnClickListener(this)
        btn_add_customer_location.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_customer_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        toolbar_add_customer_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btn_add_customer_save -> {
                    if (validateCustomerProfileDetails()) {
                        uploadCustomerDetails()
                    }
                }
                R.id.btn_add_customer_location -> {

                    mNo = et_add_customer_no.text.toString()
                    mFirstName = et_add_customer_first_name.text.toString()
                    mLastName = et_add_customer_last_name.text.toString()

                    val intent =
                        Intent(this@AddCustomerActivity, AddCustomerMapsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_GROUPS_ID,mGroupsId)
                    intent.putExtra(Constants.EXTRA_DATE_SELECT, mDateSelect)
                    intent.putExtra(Constants.EXTRA_COURSE_SELECT, mCourseSelect)
                    intent.putExtra(Constants.EXTRA_NO_SELECT,mNo)
                    intent.putExtra(Constants.EXTRA_FIRST_NAME,mFirstName)
                    intent.putExtra(Constants.EXTRA_LAST_NAME,mLastName)

                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun validateCustomerProfileDetails(): Boolean {
        return when {


            TextUtils.isEmpty(et_add_customer_no.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_no), true)
                false
            }

            TextUtils.isEmpty(et_add_customer_first_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_customer_first_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_add_customer_last_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_customer_last_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_add_customer_lat.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_lat), true)
                false
            }

            TextUtils.isEmpty(et_add_customer_lng.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_lng), true)
                false
            }

            else -> {
                true
            }
        }
    }

    private fun uploadCustomerDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        val customer = Customer(
            et_add_customer_date.text.toString().trim { it <= ' ' },
            et_add_customer_course.text.toString().trim { it <= ' ' },
            et_add_customer_no.text.toString().toInt(),
            et_add_customer_first_name.text.toString().trim { it <= ' ' },
            et_add_customer_last_name.text.toString().trim { it <= ' ' },
            et_add_customer_lat.text.toString().trim { it <= ' ' },
            et_add_customer_lng.text.toString().trim { it <= ' ' },
            et_add_customer_memo.text.toString().trim { it <= ' ' }
        )
        FirestoreClass().uploadCustomerDetails(this,mGroupsId,customer)
    }

    fun customerUploadSuccess() {
        hideProgressDialog()

        Toast.makeText(
            this@AddCustomerActivity,
            "お客様情報の登録が完了しました",
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@AddCustomerActivity, EditCourseActivity::class.java))
        finish()
    }

}