package com.patrolnavi.ui.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.patrolnavi.R
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.GroupsUsers
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_details_customer.*
import kotlinx.android.synthetic.main.activity_edit_groups.*
import kotlinx.android.synthetic.main.activity_edit_groups_users.*

class EditGroupsUsersActivity : BaseActivity() {

    private var mGroupsId: String = ""
    private var mGroupsName: String = ""
    private var mUserId: String = ""
    private lateinit var mGroupsUsrsDetails: GroupsUsers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_groups_users)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_NAME)) {
            mGroupsName = intent.getStringExtra(Constants.EXTRA_GROUPS_NAME)!!
        }

        mUserId = FirestoreClass().getCurrentUserID()

        setupActionBar()

        getEditGroupsUsersDetails()

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

                deleteBelongingGroupsUsers(mUserId)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getEditGroupsUsersDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getEditGroupsUsersDetails(this@EditGroupsUsersActivity, mGroupsId,mUserId)
    }

    fun groupsUsersDetailsSuccess(groupsUsers:GroupsUsers){
        mGroupsUsrsDetails = groupsUsers

        hideProgressDialog()

        et_edit_groups_users_name.setText(groupsUsers.user_name)

    }

    fun deleteBelongingGroupsUsers(userId: String) {
        showAlertToDeleteBelongingGroupsUsers(userId)
    }

    private fun showAlertToDeleteBelongingGroupsUsers(userId: String) {
        val builder = AlertDialog.Builder(this@EditGroupsUsersActivity)

        builder.setTitle(resources.getString(R.string.delete_dialog_belonging_groups_users_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_belonging_groups_users_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            showProgressDialog(resources.getString(R.string.please_wait))

            FirestoreClass().deleteBelongingGroupsUsers(this, mGroupsId, userId)

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
        hideProgressDialog()

        Toast.makeText(this, "グループを削除しました", Toast.LENGTH_SHORT).show()

        finish()
    }
}