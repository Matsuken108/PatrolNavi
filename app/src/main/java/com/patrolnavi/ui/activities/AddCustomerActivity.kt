package com.patrolnavi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivityAddCustomerBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Customer
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_add_customer.*

class AddCustomerActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding:ActivityAddCustomerBinding

//    private var mSelectedImageFileURI: Uri? = null
//    private var mCustomerImageURL:String =""

    private var mGroupsId: String = ""
    private var mDateSelect: String = ""
    private var mCourseSelect: String = ""
    private var mNo: String = ""
    private var mFirstName: String = ""
    private var mLastName: String = ""
    private var mCustomerLat: String = ""
    private var mCustomerLng: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_DATE_SELECT)) {
            mDateSelect = intent.getStringExtra(Constants.EXTRA_DATE_SELECT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_COURSE_SELECT)) {
            mCourseSelect = intent.getStringExtra(Constants.EXTRA_COURSE_SELECT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_LAT)) {
            mGroupsLat = intent.getStringExtra(Constants.EXTRA_GROUPS_LAT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_LNG)) {
            mGroupsLng = intent.getStringExtra(Constants.EXTRA_GROUPS_LNG)!!
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
        if (intent.hasExtra(Constants.EXTRA_CUSTOMER_LAT)) {
            mCustomerLat = intent.getStringExtra(Constants.EXTRA_CUSTOMER_LAT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_CUSTOMER_LNG)) {
            mCustomerLng = intent.getStringExtra(Constants.EXTRA_CUSTOMER_LNG)!!
        }

        Log.i(javaClass.simpleName, "Add groupsId: ${mGroupsId}")


//        et_add_customer_date.isEnabled = false
        binding.etAddCustomerDate.setText(mDateSelect)
//        et_add_customer_course.isEnabled = false
        binding.etAddCustomerCourse.setText(mCourseSelect)
        binding.etAddCustomerLat.isEnabled = false
        binding.etAddCustomerLat.setText(mCustomerLat)
        binding.etAddCustomerLng.isEnabled = false
        binding.etAddCustomerLng.setText(mCustomerLng)
        binding.etAddCustomerNo.setText(mNo)
        binding.etAddCustomerFirstName.setText(mFirstName)
        binding.etAddCustomerLastName.setText(mLastName)

        setupActionBar()

        binding.btnAddCustomerSave.setOnClickListener(this)
        binding.btnAddCustomerLocation.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddCustomerActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        binding.toolbarAddCustomerActivity.setNavigationOnClickListener { onBackPressed() }
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

                    mNo = binding.etAddCustomerNo.text.toString()
                    mFirstName = binding.etAddCustomerFirstName.text.toString()
                    mLastName = binding.etAddCustomerLastName.text.toString()

                    val intent =
                        Intent(this@AddCustomerActivity, AddCustomerMapsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
                    intent.putExtra(Constants.EXTRA_DATE_SELECT, mDateSelect)
                    intent.putExtra(Constants.EXTRA_COURSE_SELECT, mCourseSelect)
                    intent.putExtra(Constants.EXTRA_NO_SELECT, mNo)
                    intent.putExtra(Constants.EXTRA_FIRST_NAME, mFirstName)
                    intent.putExtra(Constants.EXTRA_LAST_NAME, mLastName)
                    intent.putExtra(Constants.EXTRA_GROUPS_LAT, mGroupsLat)
                    intent.putExtra(Constants.EXTRA_GROUPS_LNG, mGroupsLng)

                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun validateCustomerProfileDetails(): Boolean {
        return when {


            TextUtils.isEmpty(binding.etAddCustomerNo.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_no), true)
                false
            }                                       

            TextUtils.isEmpty(binding.etAddCustomerFirstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_customer_first_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.etAddCustomerLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_customer_last_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.etAddCustomerLat.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_lat), true)
                false
            }

            TextUtils.isEmpty(binding.etAddCustomerLng.text.toString().trim { it <= ' ' }) -> {
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

        val db = FirebaseFirestore.getInstance()
        val collection = db.collection(Constants.GROUPS).document(mGroupsId).collection(Constants.CUSTOMER)
        val customerId = collection.document().id

        val customer = Customer(
            binding.etAddCustomerDate.text.toString().trim { it <= ' ' },
            binding.etAddCustomerCourse.text.toString().trim { it <= ' ' },
            binding.etAddCustomerNo.text.toString().toInt(),
            binding.etAddCustomerFirstName.text.toString().trim { it <= ' ' },
            binding.etAddCustomerLastName.text.toString().trim { it <= ' ' },
            binding.etAddCustomerLat.text.toString().trim { it <= ' ' },
            binding.etAddCustomerLng.text.toString().trim { it <= ' ' },
            binding.etAddCustomerMemo.text.toString().trim { it <= ' ' },
            customerId,
            mGroupsId
        )
        FirestoreClass().uploadCustomerDetails(this, mGroupsId, customerId, customer)
    }

    fun customerUploadSuccess() {
        hideProgressDialog()

        Toast.makeText(
            this@AddCustomerActivity,
            "お客様情報の登録が完了しました",
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(this@AddCustomerActivity, EditCourseActivity::class.java)
        intent.putExtra(Constants.EXTRA_DATE_SELECT,mDateSelect)
        intent.putExtra(Constants.EXTRA_COURSE_SELECT,mCourseSelect)
        intent.putExtra(Constants.EXTRA_GROUPS_ID,mGroupsId)
        intent.putExtra(Constants.EXTRA_GROUPS_LAT,mGroupsLat)
        intent.putExtra(Constants.EXTRA_GROUPS_LNG,mGroupsLng)

        startActivity(intent)
        
        finish()
    }

}