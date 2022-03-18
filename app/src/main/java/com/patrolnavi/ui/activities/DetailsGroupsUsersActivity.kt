package com.patrolnavi.ui.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.patrolnavi.R
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_details_customer.*
import kotlinx.android.synthetic.main.activity_details_customer.toolbar_customer_details_activity
import kotlinx.android.synthetic.main.activity_details_groups_users.*

class DetailsGroupsUsersActivity : BaseActivity() {

    private var mUserId: String = ""
    private var mGroupsId: String = ""
    private var mGroupsUsersName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_groups_users)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }

        if (intent.hasExtra(Constants.EXTRA_GROUPS_USER_NAME)) {
            mGroupsUsersName = intent.getStringExtra(Constants.EXTRA_GROUPS_USER_NAME)!!
        }

        et_details_groups_users_name.setText(mGroupsUsersName)

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_details_groups_users_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        toolbar_details_groups_users_activity.setNavigationOnClickListener { onBackPressed() }
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

                deleteGroupsUsers(mUserId)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun deleteGroupsUsers(userId: String) {
        showAlertToDeleteBelongingGroupsUsers(userId)
    }

    private fun showAlertToDeleteBelongingGroupsUsers(userId: String) {
        val builder = AlertDialog.Builder(this@DetailsGroupsUsersActivity)

        builder.setTitle(resources.getString(R.string.delete_dialog_belonging_groups_users_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_belonging_groups_users_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            showProgressDialog(resources.getString(R.string.please_wait))

            FirestoreClass().deleteGroupsUsers(this, mGroupsId, userId)

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
        hideProgressDialog()

        Toast.makeText(this, "グループを削除しました", Toast.LENGTH_SHORT).show()

        finish()
    }

}