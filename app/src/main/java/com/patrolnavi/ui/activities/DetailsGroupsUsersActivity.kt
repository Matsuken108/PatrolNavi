package com.patrolnavi.ui.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivityDetailsGroupsUsersBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_details_customer.*
import kotlinx.android.synthetic.main.activity_details_customer.toolbar_customer_details_activity
import kotlinx.android.synthetic.main.activity_details_groups_users.*

class DetailsGroupsUsersActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailsGroupsUsersBinding

    private var mGroupsId: String = ""
    private var mGroupsUsersName: String = ""
    private var mBelongingGroupsId:String=""
    private var mGroupsUserId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsGroupsUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_USER_NAME)) {
            mGroupsUsersName = intent.getStringExtra(Constants.EXTRA_GROUPS_USER_NAME)!!
        }
        if (intent.hasExtra(Constants.BELONGING_GROUPS_ID)) {
            mBelongingGroupsId = intent.getStringExtra(Constants.BELONGING_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_USER_ID)) {
            mGroupsUserId = intent.getStringExtra(Constants.EXTRA_GROUPS_USER_ID)!!
        }
        et_details_groups_users_name.isEnabled = false
        et_details_groups_users_name.setText(mGroupsUsersName)

        Log.i(javaClass.simpleName, "groupsId : ${mGroupsId} groupsUserId : ${mGroupsUserId} neme: ${mGroupsUsersName}")

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
                deleteGroupsUsers()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun deleteGroupsUsers() {
        showAlertToDeleteBelongingGroupsUsers()
    }

    private fun showAlertToDeleteBelongingGroupsUsers() {
        val builder = AlertDialog.Builder(this@DetailsGroupsUsersActivity)

        builder.setTitle(resources.getString(R.string.delete_dialog_belonging_groups_users_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_belonging_groups_users_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            showProgressDialog(resources.getString(R.string.please_wait))

            FirestoreClass().deleteGroupsUsers(
                this@DetailsGroupsUsersActivity,
                mGroupsId,
                mGroupsUserId
            )

            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    fun groupsUsersDeleteSuccess() {

        FirestoreClass().deleteBelongingGroupsUsers(this@DetailsGroupsUsersActivity,mBelongingGroupsId)
        
    }

    fun belongingGroupsUsersDeleteSuccess(){
        hideProgressDialog()

        Toast.makeText(this, "グループを削除しました", Toast.LENGTH_SHORT).show()

        finish()
    }

}