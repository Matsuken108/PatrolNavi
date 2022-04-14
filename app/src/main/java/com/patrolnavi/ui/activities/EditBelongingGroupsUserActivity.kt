package com.patrolnavi.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivityEditBelongingGroupsUserBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.GroupsUsers
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_edit_belonging_groups_user.*
import kotlinx.android.synthetic.main.activity_edit_customer.*

class EditBelongingGroupsUserActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityEditBelongingGroupsUserBinding

    private var mGroupsId: String = ""
    private var mGroupsUserName: String = ""
    private var mBelongingGroupsId: String = ""
    private var mGroupsUserId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBelongingGroupsUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_USER_NAME)) {
            mGroupsUserName = intent.getStringExtra(Constants.EXTRA_GROUPS_USER_NAME)!!
        }
        if (intent.hasExtra(Constants.BELONGING_GROUPS_ID)) {
            mBelongingGroupsId = intent.getStringExtra(Constants.BELONGING_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_USER_ID)) {
            mGroupsUserId = intent.getStringExtra(Constants.EXTRA_GROUPS_USER_ID)!!
        }

        Log.i(javaClass.simpleName, "groupsId : ${mGroupsId} groupsUserId : ${mGroupsUserId}")
        Log.i(javaClass.simpleName, "mBelongingGroupsUserId : ${mBelongingGroupsId}")

        et_edit_belonging_groups_user_name.setText(mGroupsUserName)

        setupActionBar()

        btn_edit_belonging_groups_user_save.setOnClickListener(this@EditBelongingGroupsUserActivity)

    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_edit_groups_users_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        toolbar_edit_groups_users_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater

        inflater.inflate(R.menu.edit_groups_users_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.navigation_delete_belonging_groups -> {
                deleteBelongingGroupsUsers()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btn_edit_belonging_groups_user_save -> {
                    if (validateBelongingGroupsUserDetails()) {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        updateBelongingGroupsUserDetails()
                    }
                }
            }
        }
    }

    private fun validateBelongingGroupsUserDetails(): Boolean {
        return when {
            TextUtils.isEmpty(
                et_edit_belonging_groups_user_name.text.toString().trim { it <= ' ' }) -> {
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

    private fun updateBelongingGroupsUserDetails() {
        val belongingGroupsUserHashMap = HashMap<String, Any>()

        val groupsUser = et_edit_belonging_groups_user_name.text.toString().trim { it <= ' ' }
        if (groupsUser != mGroupsUserName) {
            belongingGroupsUserHashMap[Constants.EXTRA_GROUPS_USER_NAME] = groupsUser
        }
        FirestoreClass().updateBelongingGroupsUserDetails(
            this@EditBelongingGroupsUserActivity,
            mBelongingGroupsId,
            belongingGroupsUserHashMap
        )
    }

    fun belongingGroupsUserDetailsUpdateSuccess() {
        hideProgressDialog()

        Toast.makeText(
            this@EditBelongingGroupsUserActivity,
            resources.getString(R.string.msg_customer_details_update_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(
            Intent(
                this@EditBelongingGroupsUserActivity,
                DetailsGroupsUsersActivity::class.java
            )
        )
        finish()
    }

    fun deleteBelongingGroupsUsers() {
        showAlertToDeleteBelongingGroupsUsers()
    }

    private fun showAlertToDeleteBelongingGroupsUsers() {
        val builder = AlertDialog.Builder(this@EditBelongingGroupsUserActivity)

        builder.setTitle(resources.getString(R.string.delete_dialog_belonging_groups_users_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_belonging_groups_users_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            showProgressDialog(resources.getString(R.string.please_wait))

            Log.i(javaClass.simpleName, "EditGroupsUsersActivity FireStoreClass send")

            FirestoreClass().deleteBelongingGroupsUsers(this, mBelongingGroupsId)

            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    fun belongingGroupsUsersDeleteSuccess() {
        FirestoreClass().deleteGroupsUsers(
            this@EditBelongingGroupsUserActivity,
            mGroupsId,
            mGroupsUserId
        )

    }

    fun deleteGroupsUsersSuccess() {
        hideProgressDialog()

        Toast.makeText(this, "グループを削除しました", Toast.LENGTH_SHORT).show()

        startActivity(
            Intent(
                this@EditBelongingGroupsUserActivity,
                DetailsUserProfileActivity::class.java
            )
        )
        finish()
    }
    
}