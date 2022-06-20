package com.patrolnavi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivityDetailsUserProfileBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.BelongingGroups
import com.patrolnavi.models.User
import com.patrolnavi.ui.adapters.BelongingGroupsListAdapter
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_details_user_profile.*

class DetailsUserProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailsUserProfileBinding

    private lateinit var mUserDetails: User
    private lateinit var mBelongingList: ArrayList<BelongingGroups>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        getUserProfileDetails()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarUserProfileActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_home)
        }
        binding.toolbarUserProfileActivity.setOnClickListener {
            val intent = Intent(this@DetailsUserProfileActivity,SettingCourseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater

        inflater.inflate(R.menu.edit_user_profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

            R.id.navigation_edit_user_profile -> {
                val intent = Intent(this@DetailsUserProfileActivity, EditUserProfileActivity::class.java)
                intent.putExtra(Constants.EXTRA_USER_DETAILS,mUserDetails)
                startActivity(intent)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUserProfileDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getUserProfileDetails(this@DetailsUserProfileActivity)
    }

    fun userProfileGetSuccess(user: User) {

        mUserDetails = user

        binding.etUserProfileFirstName.isEnabled = false
        binding.etUserProfileFirstName.setText(mUserDetails.firstName)
        binding.etUserProfileLastName.isEnabled = false
        binding.etUserProfileLastName.setText(mUserDetails.lastName)
        binding.etUserProfileUserId.setText(mUserDetails.user_id)
        binding.etUserProfileEmail.isEnabled = false
        binding.etUserProfileEmail.setText(mUserDetails.email)
        binding.etUserProfileMobileNumber.isEnabled = false
        binding.etUserProfileMobileNumber.setText(mUserDetails.mobile.toString())

        FirestoreClass().getBelongingGroupsList(this@DetailsUserProfileActivity)
    }


    fun getBelongingGroupsListSuccess(belongingList: ArrayList<BelongingGroups>) {

        hideProgressDialog()

        mBelongingList = belongingList

        if (belongingList.size > 0) {
            binding.rvUserProfileBelongingGroupsList.visibility = View.VISIBLE
            binding.tvUserProfileBelongingGroupsFound.visibility = View.GONE

            binding.rvUserProfileBelongingGroupsList.layoutManager =
                LinearLayoutManager(this@DetailsUserProfileActivity)
            binding.rvUserProfileBelongingGroupsList.setHasFixedSize(true)

            val belongingAdapter =
                BelongingGroupsListAdapter(this@DetailsUserProfileActivity, mBelongingList)
            binding.rvUserProfileBelongingGroupsList.adapter = belongingAdapter

        } else {
            binding.rvUserProfileBelongingGroupsList.visibility = View.GONE
            binding.tvUserProfileBelongingGroupsFound.visibility = View.VISIBLE
        }
    }
}