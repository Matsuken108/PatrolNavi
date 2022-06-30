package com.patrolnavi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivitySettingGroupsBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Groups
import com.patrolnavi.ui.adapters.SettingGroupsListAdapter

class SettingGroupsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingGroupsBinding

    private lateinit var mGroupsList: ArrayList<Groups>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivSettingGroupsHome.setOnClickListener{
            val intent = Intent(this@SettingGroupsActivity,SettingCourseActivity::class.java)
            startActivity(intent)
        }

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarSettingGroupsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }
        binding.toolbarSettingGroupsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getGroupsList() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getGroupsList(this@SettingGroupsActivity)
    }

    fun successGroupsList(groupsList: ArrayList<Groups>) {
        hideProgressDialog()

        mGroupsList = groupsList

        if (groupsList.size > 0) {
            binding.rvSettingGroups.visibility = View.VISIBLE
            binding.tvSettingGroupsFound.visibility = View.GONE

            binding.rvSettingGroups.layoutManager = LinearLayoutManager(this@SettingGroupsActivity)
            binding.rvSettingGroups.setHasFixedSize(true)

            val settingGroupsAdapter =
                SettingGroupsListAdapter(this@SettingGroupsActivity, mGroupsList)
            binding.rvSettingGroups.adapter = settingGroupsAdapter

        } else {
            binding.rvSettingGroups.visibility = View.GONE
            binding.tvSettingGroupsFound.visibility = View.VISIBLE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater

        inflater.inflate(R.menu.add_groups_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

            R.id.navigation_add_groups -> {
                val intent = Intent(this@SettingGroupsActivity, AddGroupsActivity::class.java)
                startActivity(intent)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getGroupsList()
    }
}