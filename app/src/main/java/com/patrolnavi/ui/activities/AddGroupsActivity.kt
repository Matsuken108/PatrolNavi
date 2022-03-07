package com.patrolnavi.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.patrolnavi.R
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Groups
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_add_customer.*
import kotlinx.android.synthetic.main.activity_add_customer.et_add_customer_no
import kotlinx.android.synthetic.main.activity_add_groups.*

class AddGroupsActivity : BaseActivity(), View.OnClickListener {

    private var mGroupsName: String = ""
    private var mGroupsPass: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_groups)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_GROUPS_NAME)) {
            mGroupsName = intent.getStringExtra(Constants.EXTRA_GROUPS_NAME)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_PASS)) {
            mGroupsPass = intent.getStringExtra(Constants.EXTRA_GROUPS_PASS)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_LAT)) {
            mGroupsLat = intent.getStringExtra(Constants.EXTRA_GROUPS_LAT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_LNG)) {
            mGroupsLng = intent.getStringExtra(Constants.EXTRA_GROUPS_LNG)!!
        }

        et_add_groups_name.setText(mGroupsName)
        et_add_groups_pass.setText(mGroupsPass)
        et_add_groups_lat.isEnabled = false
        et_add_groups_lat.setText(mGroupsLat)
        et_add_groups_lng.isEnabled = false
        et_add_groups_lng.setText(mGroupsLng)

        btn_add_groups_save.setOnClickListener(this)
        btn_add_groups_location.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_groups_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        toolbar_add_groups_activity.setNavigationOnClickListener { onBackPressed() }
    }
    
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btn_add_groups_save -> {
                    if (validateAddGroups()) {
                        uploadGroupsDetails()
                    }
                }
                R.id.btn_add_groups_location -> {

                    mGroupsName = et_add_groups_name.text.toString()
                    mGroupsPass = et_add_groups_pass.text.toString()

                    val intent = Intent(this@AddGroupsActivity, AddGroupsMapsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_GROUPS_NAME, mGroupsName)
                    intent.putExtra(Constants.EXTRA_GROUPS_PASS, mGroupsPass)

                    Log.i(javaClass.simpleName,"AddGroups name:${mGroupsName} pass:${mGroupsPass}")

                    startActivity(intent)
                }
            }
        }
    }

    private fun validateAddGroups(): Boolean {
        return when {

            TextUtils.isEmpty(et_add_groups_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_groups_name), true)
                false
            }

            TextUtils.isEmpty(et_add_groups_pass.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_customer_groups_pass),
                    true
                )
                false

            }
            else -> {
                true
            }
        }
    }

    private fun uploadGroupsDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        val groups = Groups(
            et_add_groups_name.text.toString().trim { it <= ' ' },
            et_add_groups_pass.text.toString().trim { it <= ' ' },
            mGroupsLat,
            mGroupsLng,
            FirestoreClass().getCurrentUserID()
        )

        FirestoreClass().uploadGroupsDetails(this@AddGroupsActivity, groups)
    }

    fun groupsUploadSuccess() {
        hideProgressDialog()

        startActivity(Intent(this@AddGroupsActivity, SettingGroupsActivity::class.java))
        finish()
    }

}