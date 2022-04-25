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
    private var mUserId :String =""
    private var mUserFullName: String = ""
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

        setupActionBar()

        binding.btnInvitationGroups.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarJoinGroupsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        binding.toolbarJoinGroupsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun uploadGroupsUsers() {

        val firstName = binding.etInvitationGroupsUserFirstName.text.toString().trim { it <= ' ' }
        val lastName = binding.etInvitationGroupsUserLastName.text.toString().trim { it <= ' ' }
        mUserId = binding.etInvitationGroupsUserId.text.toString().trim { it <= ' ' }

        mUserFullName = "${firstName} ${lastName}"

        val groupsUsers = GroupsUsers(
            mGroupsId,
            mGroupsName,
            mUserId,
            mUserFullName
        )

        Log.i(javaClass.simpleName, "AddGroupsUsers : send data")

        FirestoreClass().uploadGroupsUsers(
            this@InvitationGroupsActivity,
            mGroupsId,
            groupsUsers,
        )
    }

    fun groupsUsersUploadSuccess(){

        uploadBelongingGroups()
    }

    private fun uploadBelongingGroups() {

        val belongingGroups = BelongingGroups(
            mGroupsId,
            mGroupsName,
            FirestoreClass().getCurrentUserID(),
        )

        Log.i(javaClass.simpleName, "AddGroupsUsers : SuccessUpload")

        FirestoreClass().uploadBelongingGroups(
            this@InvitationGroupsActivity,
            belongingGroups,
            mGroupsId
        )
    }

    fun belongingGroupsUploadSuccess(){
        hideProgressDialog()

        val intent = Intent(this@InvitationGroupsActivity, DetailsGroupsActivity::class.java)
        intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
        intent.putExtra(Constants.EXTRA_GROUPS_NAME,mGroupsName)
        intent.putExtra(Constants.EXTRA_GROUPS_PASS,mGroupsPass)
        intent.putExtra(Constants.EXTRA_GROUPS_LAT,mGroupsLat)
        intent.putExtra(Constants.EXTRA_GROUPS_LAT,mGroupsLng)

        startActivity(intent)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btn_invitation_groups -> {
                    if (validateJoinGroups()) {
                        uploadGroupsUsers()
                    }
                }
            }
        }
    }

    private fun validateJoinGroups(): Boolean {
        return when {

            TextUtils.isEmpty(binding.etInvitationGroupsUserFirstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(binding.etInvitationGroupsUserLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(binding.etInvitationGroupsUserId.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_user_id), true)
                false
            }
            else -> {
                true
            }
        }
    }
}