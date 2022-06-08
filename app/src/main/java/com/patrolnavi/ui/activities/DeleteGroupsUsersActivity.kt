package com.patrolnavi.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivityDeleteGroupsUsersBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Groups
import com.patrolnavi.utils.Constants

class DeleteGroupsUsersActivity : BaseActivity() {

    private lateinit var binding: ActivityDeleteGroupsUsersBinding

    private var mGroupsId: String = ""
    private var mGroupsUserId: String = ""
    private var mGroupsOwnerId: String = ""
    private var mGroupsName: String = ""
    private var mGroupsPass: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""
    private lateinit var mGroups: Groups

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteGroupsUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_USER_ID)) {
            mGroupsUserId = intent.getStringExtra(Constants.EXTRA_GROUPS_USER_ID)!!
        }

        Log.i(javaClass.simpleName, "groupsId : ${mGroupsId} groupsUserId : ${mGroupsUserId} ")

        getGroupsDetail()

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarDetailsGroupsUsersActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        binding.toolbarDetailsGroupsUsersActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getGroupsDetail() {

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getGroupsDetails(this@DeleteGroupsUsersActivity, mGroupsId)

    }

    fun getGroupsDetailsSuccess(groups: Groups) {

        mGroups = groups

        mGroupsOwnerId = mGroups.owner
        mGroupsName = mGroups.name
        mGroupsPass = mGroups.password
        mGroupsLat = mGroups.groups_lat
        mGroupsLng = mGroups.groups_lng

        checkGroupsOwner()
    }

    private fun checkGroupsOwner() {

        if (mGroupsUserId == mGroupsOwnerId) {
            hideProgressDialog()

            Toast.makeText(this, "責任者の削除は出来ません", Toast.LENGTH_SHORT).show()
//            showErrorSnackBar("責任者の削除は出来ません",true)
            
            Log.i(javaClass.simpleName, "owner削除制限 ")
            finish()
        } else {
            Log.i(javaClass.simpleName, "userId: ${mGroupsUserId} ownerId : ${mGroupsOwnerId}")
            deleteGroupsUsers()

        }
    }


    fun deleteGroupsUsers() {
        showAlertToDeleteBelongingGroupsUsers()
    }

    private fun showAlertToDeleteBelongingGroupsUsers() {
        val builder = AlertDialog.Builder(this@DeleteGroupsUsersActivity)

        builder.setTitle(resources.getString(R.string.delete_dialog_belonging_groups_users_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_belonging_groups_users_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            showProgressDialog(resources.getString(R.string.please_wait))

            FirestoreClass().deleteGroupsUsers(
                this@DeleteGroupsUsersActivity,
                mGroupsId
            )

            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->
            hideProgressDialog()
            dialogInterface.dismiss()
            val intent = Intent(this@DeleteGroupsUsersActivity, EditGroupsActivity::class.java)
            intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
            intent.putExtra(Constants.EXTRA_GROUPS_NAME, mGroupsName)
            intent.putExtra(Constants.EXTRA_GROUPS_PASS, mGroupsPass)
            intent.putExtra(Constants.EXTRA_GROUPS_LAT, mGroupsLat)
            intent.putExtra(Constants.EXTRA_GROUPS_LNG, mGroupsLng)
            startActivity(intent)

        }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    fun groupsUsersDeleteSuccess() {

        FirestoreClass().deleteBelongingGroupsUsers(this@DeleteGroupsUsersActivity, mGroupsId)

    }

    fun belongingGroupsUsersDeleteSuccess() {
        hideProgressDialog()

        Toast.makeText(this, "グループを削除しました", Toast.LENGTH_SHORT).show()

        finish()
    }

}