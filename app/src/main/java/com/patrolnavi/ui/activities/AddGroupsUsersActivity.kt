package com.patrolnavi.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.patrolnavi.R
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.BelongingGroups
import com.patrolnavi.models.Groups
import com.patrolnavi.models.GroupsUsers
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_add_groups.*
import kotlinx.android.synthetic.main.activity_add_groups_users.*
import kotlinx.android.synthetic.main.activity_details_groups.*

class AddGroupsUsersActivity : BaseActivity(), View.OnClickListener {

    private var mGroupsId: String = ""
    private var mGroupsName: String = ""
    private var mGroupsPass: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_groups_users)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }
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

        Log.i(
            javaClass.simpleName,
            "AddGroupsUsers : groupsId:${mGroupsId} groupsName: ${mGroupsName}"
        )

        setupActionBar()

        btn_add_groups_user_save.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_groups_users_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        toolbar_add_groups_users_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btn_add_groups_user_save -> {
                    if (validateAddGroupsUsers()) {
                        uploadGroupsUsers()
                    }
                }
            }
        }
    }

    private fun validateAddGroupsUsers(): Boolean {
        return when {
            TextUtils.isEmpty(et_add_groups_users_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_groups_user_name),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }

    private fun uploadGroupsUsers() {
        showProgressDialog(resources.getString(R.string.please_wait))

        val groupsUsers = GroupsUsers(
            et_add_groups_users_name.text.toString().trim { it <= ' ' },
            FirestoreClass().getCurrentUserID()
        )

        Log.i(javaClass.simpleName, "AddGroupsUsers : send data")

        FirestoreClass().uploadGroupsUsers(this@AddGroupsUsersActivity, mGroupsId, groupsUsers)
    }

    fun groupsUsersUploadSuccess() {

        Log.i(javaClass.simpleName, "AddGroupsUsers : SuccessUpload")

        uploadBelongingGroups()
//        hideProgressDialog()
//
//        val intent = Intent(this@AddGroupsUsersActivity, DetailsGroupsActivity::class.java)
//        intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
//        intent.putExtra(Constants.EXTRA_GROUPS_NAME, mGroupsName)
//        intent.putExtra(Constants.EXTRA_GROUPS_PASS, mGroupsPass)
//        intent.putExtra(Constants.EXTRA_GROUPS_LAT, mGroupsLat)
//        intent.putExtra(Constants.EXTRA_GROUPS_LNG, mGroupsLng)
//        startActivity(intent)
//        finish()
    }

    private fun uploadBelongingGroups() {

        val belongingGroups = BelongingGroups(
            mGroupsName,
            mGroupsId
        )

        Log.i(javaClass.simpleName, "AddGroupsUsers : SuccessUpload")

        FirestoreClass().uploadBelongingGroups(
            this@AddGroupsUsersActivity,
            mGroupsId,
            belongingGroups
        )
    }

    fun belongingGroupsUploadSuccess() {
        hideProgressDialog()

        val intent = Intent(this@AddGroupsUsersActivity, DetailsGroupsActivity::class.java)
        intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
        intent.putExtra(Constants.EXTRA_GROUPS_NAME, mGroupsName)
        intent.putExtra(Constants.EXTRA_GROUPS_PASS, mGroupsPass)
        intent.putExtra(Constants.EXTRA_GROUPS_LAT, mGroupsLat)
        intent.putExtra(Constants.EXTRA_GROUPS_LNG, mGroupsLng)
        startActivity(intent)
        finish()
    }

}