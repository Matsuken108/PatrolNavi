package com.patrolnavi.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivityEditGroupsBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.BelongingGroups
import com.patrolnavi.models.Groups
import com.patrolnavi.models.GroupsUsers
import com.patrolnavi.ui.adapters.EditGroupsUsersListAdapter
import com.patrolnavi.ui.adapters.GroupsUsersListAdapter
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_details_groups.*
import kotlinx.android.synthetic.main.activity_details_groups.toolbar_details_groups_activity
import kotlinx.android.synthetic.main.activity_edit_customer.*
import kotlinx.android.synthetic.main.activity_edit_groups.*

class EditGroupsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityEditGroupsBinding

    private var mGroupsId: String = ""
    private var mGroupsName: String = ""
    private var mGroupsPass: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""
    private lateinit var mGroupsUsersList: ArrayList<GroupsUsers>
    private lateinit var mGroupsDetails: Groups

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.etEditGroupsName.setText(mGroupsName)
        binding.etEditGroupsPass.setText(mGroupsPass)
        binding.etEditGroupsId.isEnabled = false
        binding.etEditGroupsId.setText(mGroupsId)
        binding.etEditGroupsLat.isEnabled = false
        binding.etEditGroupsLat.setText(mGroupsLat)
        binding.etEditGroupsLng.isEnabled = false
        binding.etEditGroupsLng.setText(mGroupsLng)

        setupActionBar()

        binding.btnEditGroupsUpdate.setOnClickListener(this)
        binding.btnEditGroupsLocation.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarEditGroupsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_home)
        }

        binding.toolbarEditGroupsActivity.setOnClickListener {
            val intent = Intent(this@EditGroupsActivity,SettingCourseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater

        inflater.inflate(R.menu.add_groups_users_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.navigation_invitation_groups -> {
                val intent = Intent(this@EditGroupsActivity, InvitationGroupsActivity::class.java)
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

    private fun getEditGroupsUsersList() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getEditGroupsUsersList(this@EditGroupsActivity, mGroupsId)
    }

    fun successEditGroupsUsersList(GroupsUsersList: ArrayList<GroupsUsers>) {
        hideProgressDialog()

        mGroupsUsersList = GroupsUsersList

        if (GroupsUsersList.size > 0) {
            binding.rvEditGroupsUsersList.visibility = View.VISIBLE
            binding.tvEditGroupsFound.visibility = View.GONE

            binding.rvEditGroupsUsersList.layoutManager =
                LinearLayoutManager(this@EditGroupsActivity)
            binding.rvEditGroupsUsersList.setHasFixedSize(true)

            val editGroupsUsersUsersAdapter =
                EditGroupsUsersListAdapter(this@EditGroupsActivity, mGroupsUsersList)
            binding.rvEditGroupsUsersList.adapter = editGroupsUsersUsersAdapter

        } else {
            binding.rvEditGroupsUsersList.visibility = View.GONE
            binding.tvEditGroupsFound.visibility = View.VISIBLE
        }
    }

    private fun updateGroupsDetails() {
        val groupsHashMap = HashMap<String, Any>()

        val name = binding.etEditGroupsName.text.toString().trim { it <= ' ' }
        if (name != mGroupsDetails.name) {
            groupsHashMap[Constants.EXTRA_GROUPS_NAME] = name
        }
        val pass = binding.etEditGroupsPass.text.toString().trim { it <= ' ' }
        if (name != mGroupsDetails.password) {
            groupsHashMap[Constants.EXTRA_GROUPS_PASS] = pass
        }
        val groupsLat = binding.etEditGroupsLat.text.toString().trim { it <= ' ' }
        if (groupsLat != mGroupsDetails.groups_lat) {
            groupsHashMap[Constants.EXTRA_GROUPS_LAT] = groupsLat
        }
        val groupsLng = binding.etEditGroupsLng.text.toString().trim { it <= ' ' }
        if (groupsLng != mGroupsDetails.groups_lng) {
            groupsHashMap[Constants.EXTRA_GROUPS_LNG] = groupsLng
        }

        FirestoreClass().updateGroupsDetails(
            this@EditGroupsActivity,
            mGroupsId,
            groupsHashMap
        )
    }

    fun groupsDetailsUpdateSuccess() {
        hideProgressDialog()

        Toast.makeText(
            this@EditGroupsActivity,
            resources.getString(R.string.msg_customer_details_update_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@EditGroupsActivity, DetailsGroupsActivity::class.java))
        finish()
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btn_edit_groups_update -> {
                    if (validateGroupsDetails()) {
                        updateGroupsDetails()
                    }
                }
                R.id.btn_edit_groups_location -> {
                    val intent = Intent(this@EditGroupsActivity, EditGroupsMapsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_GROUPS_NAME, mGroupsName)
                    intent.putExtra(Constants.EXTRA_GROUPS_PASS, mGroupsPass)
                    intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
                }
            }
        }
    }

    private fun validateGroupsDetails(): Boolean {
        return when {

            TextUtils.isEmpty(binding.etEditGroupsName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_groups_name), true)
                false
            }
            TextUtils.isEmpty(binding.etEditGroupsPass.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_groups_pass), true)
                false
            }
            TextUtils.isEmpty(binding.etEditGroupsLat.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_groups_lat), true)
                false
            }
            TextUtils.isEmpty(binding.etEditGroupsLng.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_groups_lng), true)
                false
            }
            else -> {
                true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getEditGroupsUsersList()
    }


}