package com.patrolnavi.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivityAddCustomerBinding
import com.patrolnavi.databinding.ActivityAddGroupsBinding
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.BelongingGroups
import com.patrolnavi.models.Groups
import com.patrolnavi.models.GroupsUsers
import com.patrolnavi.models.User
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_add_customer.*
import kotlinx.android.synthetic.main.activity_add_customer.et_add_customer_no
import kotlinx.android.synthetic.main.activity_add_groups.*

class AddGroupsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddGroupsBinding

    private lateinit var mUserDetails: User
    private var mGroupsName: String = ""
    private var mGroupsPass: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""
    private var mGroupsId: String = ""
    private var mUserFullName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

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

        binding.etAddGroupsName.setText(mGroupsName)
        binding.etAddGroupsPass.setText(mGroupsPass)
        binding.etAddGroupsLat.isEnabled = false
        binding.etAddGroupsLat.setText(mGroupsLat)
        binding.etAddGroupsLng.isEnabled = false
        binding.etAddGroupsLng.setText(mGroupsLng)

        binding.btnAddGroupsSave.setOnClickListener(this)
        binding.btnAddGroupsLocation.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddGroupsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        binding.toolbarAddGroupsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btn_add_groups_save -> {
                    if (validateAddGroups()) {
                        uploadGroupsDetails()
                    }
                }
                R.id.btn_add_groups_location -> {

                    mGroupsName = binding.etAddGroupsName.text.toString()
                    mGroupsPass = binding.etAddGroupsPass.text.toString()

                    val intent = Intent(this@AddGroupsActivity, AddGroupsMapsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_GROUPS_NAME, mGroupsName)
                    intent.putExtra(Constants.EXTRA_GROUPS_PASS, mGroupsPass)

                    Log.i(javaClass.simpleName, "AddGroups name:${mGroupsName} pass:${mGroupsPass}")

                    startActivity(intent)
                }
            }
        }
    }

    private fun validateAddGroups(): Boolean {
        return when {

            TextUtils.isEmpty(binding.etAddGroupsName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_groups_name), true)
                false
            }

            TextUtils.isEmpty(binding.etAddGroupsPass.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_customer_groups_pass),
                    true
                )
                false
            }
            TextUtils.isEmpty(binding.etAddGroupsLat.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_customer_lat),
                    true
                )
                false
            }
            TextUtils.isEmpty(binding.etAddGroupsLng.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_customer_lng),
                    true
                )
                false
            }

            else -> {
                true
            }
        }
    }

    private fun uploadGroupsDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        val db = FirebaseFirestore.getInstance()
        val collection = db.collection(Constants.GROUPS)
        mGroupsId = collection.document().id

        val groupsUserId :ArrayList<String> = ArrayList()
        groupsUserId.add(FirestoreClass().getCurrentUserID())

        val groups = Groups(
            binding.etAddGroupsName.text.toString().trim { it <= ' ' },
            binding.etAddGroupsPass.text.toString().trim { it <= ' ' },
            binding.etAddGroupsLat.text.toString().trim { it <= ' ' },
            binding.etAddGroupsLng.text.toString().trim { it <= ' ' },
            groupsUserId,
            FirestoreClass().getCurrentUserID(),
            mGroupsId
        )

        FirestoreClass().uploadGroupsDetails(this@AddGroupsActivity, mGroupsId, groups)
    }

    fun groupsUploadSuccess() {

        getUserDetails()
    }

    private fun getUserDetails() {

        FirestoreClass().getUserProfileDetails(this@AddGroupsActivity)
    }

    fun userProfileGetSuccess(user: User) {

        mUserDetails = user

        val firstName = mUserDetails.firstName
        val lastName = mUserDetails.lastName

        mUserFullName = "${firstName} ${lastName}"

        Log.i(javaClass.simpleName,"fullName ${mUserFullName}")

        uploadGroupsUsers()
    }

    private fun uploadGroupsUsers() {

        val groupsUsers = GroupsUsers(
            mGroupsId,
            mGroupsName,
            FirestoreClass().getCurrentUserID(),
            mUserFullName
            )

        Log.i(javaClass.simpleName, "AddGroupsUsers : send data")

        FirestoreClass().uploadGroupsUsers(
            this@AddGroupsActivity,
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

        Log.i(javaClass.simpleName, "AddGroupsUsers : ${mGroupsId}")

        FirestoreClass().uploadBelongingGroups(
            this@AddGroupsActivity,
            belongingGroups,
            FirestoreClass().getCurrentUserID(),
            mGroupsId
        )
    }

    fun belongingGroupsUploadSuccess(){
        hideProgressDialog()
        
        //TODO 登録後はセッティング画面に！

        val intent = Intent(this@AddGroupsActivity, DetailsGroupsActivity::class.java)
        intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
        intent.putExtra(Constants.EXTRA_GROUPS_NAME, mGroupsName)
        intent.putExtra(Constants.EXTRA_GROUPS_PASS, mGroupsPass)
        intent.putExtra(Constants.EXTRA_GROUPS_LAT, mGroupsLat)
        intent.putExtra(Constants.EXTRA_GROUPS_LNG, mGroupsLng)
        startActivity(intent)
        finish()

    }

}