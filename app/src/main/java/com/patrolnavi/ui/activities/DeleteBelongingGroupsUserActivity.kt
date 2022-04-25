package com.patrolnavi.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivityDeleteBelongingGroupsUserBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.utils.Constants

class DeleteBelongingGroupsUserActivity : BaseActivity() {

    private lateinit var binding: ActivityDeleteBelongingGroupsUserBinding

    private var mGroupsId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBelongingGroupsUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!

            deleteBelongingGroupsUsers()
        }
    }

    fun deleteBelongingGroupsUsers() {
        showAlertToDeleteBelongingGroupsUsers()
    }

    private fun showAlertToDeleteBelongingGroupsUsers() {
        val builder = AlertDialog.Builder(this@DeleteBelongingGroupsUserActivity)

        builder.setTitle(resources.getString(R.string.delete_dialog_belonging_groups_users_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_belonging_groups_users_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            showProgressDialog(resources.getString(R.string.please_wait))

            Log.i(javaClass.simpleName, "EditGroupsUsersActivity FireStoreClass send")

            FirestoreClass().deleteBelongingGroupsUsers(this@DeleteBelongingGroupsUserActivity, mGroupsId)

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
            this@DeleteBelongingGroupsUserActivity,
            mGroupsId
        )

    }

    fun deleteGroupsUsersSuccess() {
        hideProgressDialog()

        Toast.makeText(this, "グループを削除しました", Toast.LENGTH_SHORT).show()

        startActivity(
            Intent(
                this@DeleteBelongingGroupsUserActivity,
                DetailsUserProfileActivity::class.java
            )
        )
        finish()
    }
    
}