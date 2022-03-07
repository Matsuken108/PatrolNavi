package com.patrolnavi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.patrolnavi.R
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.GroupsUsers
import com.patrolnavi.ui.adapters.GroupsUsersListAdapter
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_add_groups.*
import kotlinx.android.synthetic.main.activity_details_groups.*
import kotlinx.android.synthetic.main.activity_setting_groups.*

class DetailsGroupsActivity : BaseActivity() {

    private var mGroupsId: String = ""
    private var mGroupsName: String = ""
    private var mGroupsPass: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""
    private lateinit var mGroupsUsersList: ArrayList<GroupsUsers>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_groups)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_NAME)) {
            mGroupsName = intent.getStringExtra(Constants.EXTRA_GROUPS_NAME)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
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

        et_details_groups_name.isEnabled = false
        et_details_groups_name.setText(mGroupsName)
        et_details_groups_pass.isEnabled = false
        et_details_groups_pass.setText(mGroupsPass)
        et_details_groups_id.isEnabled = false
        et_details_groups_id.setText(mGroupsId)
        et_details_groups_lat.isEnabled = false
        et_details_groups_lat.setText(mGroupsLat)
        et_details_groups_lng.isEnabled = false
        et_details_groups_lng.setText(mGroupsLng)

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_details_groups_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        toolbar_details_groups_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater

        inflater.inflate(R.menu.edit_groups_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.navigation_edit_groups -> {
                val intent =
                    Intent(this@DetailsGroupsActivity, EditGroupsActivity::class.java)
                intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
                intent.putExtra(Constants.EXTRA_GROUPS_NAME,mGroupsName)
                intent.putExtra(Constants.EXTRA_GROUPS_PASS,mGroupsPass)
                intent.putExtra(Constants.EXTRA_GROUPS_LAT,mGroupsLat)
                intent.putExtra(Constants.EXTRA_GROUPS_LNG,mGroupsLng)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getGroupsUsersList() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getGroupsUsersList(this@DetailsGroupsActivity, mGroupsId)
    }

    fun successGroupsUsersList(groupsUsersList: ArrayList<GroupsUsers>) {
        hideProgressDialog()

        mGroupsUsersList = groupsUsersList

        if (groupsUsersList.size > 0) {
            rv_groups_users_list.visibility = View.VISIBLE
            tv_details_groups_found.visibility = View.GONE

            rv_groups_users_list.layoutManager = LinearLayoutManager(this@DetailsGroupsActivity)
            rv_groups_users_list.setHasFixedSize(true)

            val groupsUsersUsersAdapter =
                GroupsUsersListAdapter(this@DetailsGroupsActivity, mGroupsUsersList)
            rv_groups_users_list.adapter = groupsUsersUsersAdapter

        } else {
            rv_groups_users_list.visibility = View.GONE
            tv_details_groups_found.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getGroupsUsersList()
    }
}