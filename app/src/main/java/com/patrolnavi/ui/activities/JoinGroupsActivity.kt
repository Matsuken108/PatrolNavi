package com.patrolnavi.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.patrolnavi.R
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Groups
import com.patrolnavi.models.GroupsUsers
import com.patrolnavi.models.User
import com.patrolnavi.ui.adapters.GroupsUsersListAdapter
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_add_groups_users.*
import kotlinx.android.synthetic.main.activity_add_groups_users.toolbar_add_groups_users_activity
import kotlinx.android.synthetic.main.activity_details_groups.*
import kotlinx.android.synthetic.main.activity_edit_customer.*
import kotlinx.android.synthetic.main.activity_join_groups.*

class JoinGroupsActivity : BaseActivity(), View.OnClickListener {

    private var mGroupsId: String = ""
    private var mGroupsName: String = ""
    private var mGroupsPass: String = ""
    private lateinit var mGroupsList: Groups

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_groups)

        setupActionBar()

        btn_join_groups.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_join_groups_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        toolbar_join_groups_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getJoinGroupsDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        mGroupsName = et_join_groups_name.text.toString().trim { it <= ' ' }
        mGroupsPass = et_join_groups_pass.text.toString().trim { it <= ' ' }
        mGroupsId = et_join_groups_id.text.toString().trim { it <= ' ' }

        FirestoreClass().getJoinGroupsDetails(this@JoinGroupsActivity, mGroupsId)
    }

    fun successGroupsJoinDetails(groups: Groups) {

        mGroupsList = groups

        if (mGroupsName == groups.name && mGroupsPass == groups.password && mGroupsId == groups.groups_id) {
            joinGroups()

        } else {
            showErrorSnackBar("一致するグループがありません", true)
        }
    }

    private fun joinGroups() {

        val groupsHashMap = HashMap<String, Any>()

        groupsHashMap[Constants.BELONGING_GROUPS_ID] = mGroupsId

        FirestoreClass().JoinGroups(this@JoinGroupsActivity, groupsHashMap,mGroupsId)

    }

    fun successGroupsJoin() {
        hideProgressDialog()

        val intent = Intent(this@JoinGroupsActivity, AddGroupsUsersActivity::class.java)
        intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
        startActivity(intent)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btn_join_groups -> {
                    if (validateJoinGroups()) {
                        getJoinGroupsDetails()
                    }
                }
            }
        }
    }

    private fun validateJoinGroups(): Boolean {
        return when {

            TextUtils.isEmpty(et_join_groups_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_groups_name), true)
                false
            }

            TextUtils.isEmpty(et_join_groups_pass.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_groups_pass), true)
                false
            }

            TextUtils.isEmpty(et_join_groups_id.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_groups_id), true)
                false
            }
            else -> {
                true
            }
        }
    }
}