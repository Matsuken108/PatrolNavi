package com.patrolnavi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivityDetailsGroupsBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.GroupsUsers
import com.patrolnavi.ui.adapters.GroupsUsersListAdapter
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_add_groups.*
import kotlinx.android.synthetic.main.activity_details_groups.*
import kotlinx.android.synthetic.main.activity_setting_groups.*

class DetailsGroupsActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailsGroupsBinding

    private var mGroupsId: String = ""
    private var mGroupsName: String = ""
    private var mGroupsPass: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""
    private lateinit var mGroupsUsersList: ArrayList<GroupsUsers>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        Log.i(javaClass.simpleName,"DetailsGroups : ${mGroupsId}")

        binding.etDetailsGroupsName.isEnabled = false
        binding.etDetailsGroupsName.setText(mGroupsName)
        binding.etDetailsGroupsPass.isEnabled = false
        binding.etDetailsGroupsPass.setText(mGroupsPass)
        binding.etDetailsGroupsId.isEnabled = false
        binding.etDetailsGroupsId.setText(mGroupsId)
        binding.etDetailsGroupsLat.isEnabled = false
        binding.etDetailsGroupsLat.setText(mGroupsLat)
        binding.etDetailsGroupsLng.isEnabled = false
        binding.etDetailsGroupsLng.setText(mGroupsLng)

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarDetailsGroupsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_home)
        }

        binding.toolbarDetailsGroupsActivity.setOnClickListener {
            val intent = Intent(this@DetailsGroupsActivity,SettingCourseActivity::class.java)
            startActivity(intent)
        }
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
            binding.rvGroupsUsersList.visibility = View.VISIBLE
            binding.tvDetailsGroupsFound.visibility = View.GONE

            binding.rvGroupsUsersList.layoutManager = LinearLayoutManager(this@DetailsGroupsActivity)
            binding.rvGroupsUsersList.setHasFixedSize(true)

            val groupsUsersUsersAdapter =
                GroupsUsersListAdapter(this@DetailsGroupsActivity, mGroupsUsersList)
            binding.rvGroupsUsersList.adapter = groupsUsersUsersAdapter

        } else {
            binding.rvGroupsUsersList.visibility = View.GONE
            binding.tvDetailsGroupsFound.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getGroupsUsersList()
    }
}