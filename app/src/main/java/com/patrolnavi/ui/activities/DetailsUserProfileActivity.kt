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
        setSupportActionBar(toolbar_user_profile_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }
        toolbar_user_profile_activity.setNavigationOnClickListener { onBackPressed() }
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

        et_user_profile_first_name.isEnabled = false
        et_user_profile_first_name.setText(mUserDetails.firstName)
        et_user_profile_last_name.isEnabled = false
        et_user_profile_last_name.setText(mUserDetails.lastName)
        et_user_profile_email.isEnabled = false
        et_user_profile_email.setText(mUserDetails.email)
        et_user_profile_mobile_number.isEnabled = false
        et_user_profile_mobile_number.setText(mUserDetails.mobile.toString())

        FirestoreClass().getBelongingGroupsList(this@DetailsUserProfileActivity)
    }


    fun getBelongingGroupsListSuccess(belongingList: ArrayList<BelongingGroups>) {

        hideProgressDialog()

        mBelongingList = belongingList

        if (belongingList.size > 0) {
            rv_user_profile_belonging_groups_list.visibility = View.VISIBLE
            tv_user_profile_belonging_groups_found.visibility = View.GONE

            rv_user_profile_belonging_groups_list.layoutManager =
                LinearLayoutManager(this@DetailsUserProfileActivity)
            rv_user_profile_belonging_groups_list.setHasFixedSize(true)

            val belongingAdapter =
                BelongingGroupsListAdapter(this@DetailsUserProfileActivity, mBelongingList)
            rv_user_profile_belonging_groups_list.adapter = belongingAdapter

        } else {
            rv_user_profile_belonging_groups_list.visibility = View.GONE
            tv_user_profile_belonging_groups_found.visibility = View.VISIBLE
        }
    }
}