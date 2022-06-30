package com.patrolnavi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivityInvitationGroupsBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.BelongingGroups
import com.patrolnavi.models.Groups
import com.patrolnavi.models.GroupsUsers
import com.patrolnavi.models.User
import com.patrolnavi.utils.Constants


class InvitationGroupsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityInvitationGroupsBinding

    private var mGroupsId: String = ""
    private var mGroupsName: String = ""
    private var mGroupsPass: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""
    private var mInvitationUserId: String = ""
    private var mInvitationUserFirstName: String = ""
    private var mInvitationUserLastName: String = ""
    private var mInvitationUserFullName: String = ""
    private lateinit var mGroupsList: Groups
    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvitationGroupsBinding.inflate(layoutInflater)
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

        binding.ivInvitationGroupsHome.setOnClickListener(this)
        binding.btnInvitationGroups.setOnClickListener(this)

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarInvitationGroupsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }
        binding.toolbarInvitationGroupsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.iv_invitation_groups_home -> {
                    val intent =
                        Intent(this@InvitationGroupsActivity, SettingCourseActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_invitation_groups -> {
                    if (validateJoinGroups()) {
                        inputInfo()
                        getInvitationUserDetails()
                    }
                }
            }
        }
    }

    private fun inputInfo() {

        mInvitationUserFirstName =
            binding.etInvitationGroupsUserFirstName.text.toString().trim { it <= ' ' }
        mInvitationUserLastName =
            binding.etInvitationGroupsUserLastName.text.toString().trim { it <= ' ' }
        mInvitationUserId = binding.etInvitationGroupsUserId.text.toString().trim { it <= ' ' }

        mInvitationUserFullName = "${mInvitationUserFirstName} ${mInvitationUserLastName}"
    }

    private fun validateJoinGroups(): Boolean {
        return when {

            TextUtils.isEmpty(
                binding.etInvitationGroupsUserFirstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(
                binding.etInvitationGroupsUserLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(
                binding.etInvitationGroupsUserId.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_user_id), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun getInvitationUserDetails() {

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getInvitationUserDetails(
            this@InvitationGroupsActivity,
            mInvitationUserId,
        )
    }

    fun userProfileGetSuccess(user: User) {

        mUserDetails = user

        Log.i(
            javaClass.simpleName,
            "et: ${mInvitationUserFirstName},db:${mUserDetails.firstName}"
        )
        Log.i(
            javaClass.simpleName,
            "et: ${mInvitationUserLastName},db:${mUserDetails.lastName}"
        )

        if (validateInvitationUser()) {
            uploadGroupsUsers()
        } else {
            hideProgressDialog()
        }
    }

    private fun validateInvitationUser(): Boolean {
        return when {

            mInvitationUserFirstName != mUserDetails.firstName
            -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_wrong_first_name), true)
                false
            }
            mInvitationUserLastName != mUserDetails.lastName
            -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_wrong_last_name), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun uploadGroupsUsers() {

        val groupsUsers = GroupsUsers(
            mGroupsId,
            mGroupsName,
            mInvitationUserId,
            mInvitationUserFullName
        )

        Log.i(javaClass.simpleName, "AddGroupsUsers : send data")

        FirestoreClass().uploadGroupsUsers(
            this@InvitationGroupsActivity,
            mGroupsId,
            groupsUsers,
            mInvitationUserId
        )
    }

    fun groupsUsersUploadSuccess() {

        addGroupsUsers()
    }

    private fun addGroupsUsers() {

        FirestoreClass().addGroupsUser(
            this@InvitationGroupsActivity,
            mInvitationUserId,
            mGroupsId
        )
    }

    fun addGroupsUserSuccess() {

        uploadBelongingGroups()
    }

    private fun uploadBelongingGroups() {

        val belongingGroups = BelongingGroups(
            mGroupsId,
            mGroupsName,
            mInvitationUserId,
        )

        Log.i(javaClass.simpleName, "AddGroupsUsers : SuccessUpload")

        FirestoreClass().uploadBelongingGroups(
            this@InvitationGroupsActivity,
            belongingGroups,
            mInvitationUserId,
            mGroupsId
        )
    }

    fun belongingGroupsUploadSuccess() {
        hideProgressDialog()

        val intent = Intent(this@InvitationGroupsActivity, DetailsGroupsActivity::class.java)
        intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
        intent.putExtra(Constants.EXTRA_GROUPS_NAME, mGroupsName)
        intent.putExtra(Constants.EXTRA_GROUPS_PASS, mGroupsPass)
        intent.putExtra(Constants.EXTRA_GROUPS_LAT, mGroupsLat)
        intent.putExtra(Constants.EXTRA_GROUPS_LAT, mGroupsLng)

        startActivity(intent)
    }
}