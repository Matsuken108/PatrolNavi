package com.patrolnavi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import com.patrolnavi.R
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_edit_customer.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_setting_course.*

class SettingCourseActivity : BaseActivity(), View.OnClickListener {

    private var mDateSelect: String = ""
    private var mCourseSelect: String = ""

    private var mGroupsId: String = ""
    private var mGroupsName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_course)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_NAME)) {
            mGroupsName = intent.getStringExtra(Constants.EXTRA_GROUPS_NAME)!!
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        tv_name_groups.setText(mGroupsName)

        if (tv_name_groups.length() == 0) {
            tv_name_groups_title.visibility = View.GONE
            tv_name_groups.visibility = View.GONE
            tv_name_groups_found.visibility = View.VISIBLE
        } else {
            tv_name_groups_title.visibility = View.VISIBLE
            tv_name_groups.visibility = View.VISIBLE
            tv_name_groups_found.visibility = View.GONE
        }

        btn_setting_course.setOnClickListener(this)
        btn_intent_edit.setOnClickListener(this)
        btn_intent_solicitation.setOnClickListener(this)
        btn_intent_groups_setting.setOnClickListener(this)
        btn_intent_my_page.setOnClickListener(this)

    }

    fun setCourseSelect() {
        mDateSelect = et_date_set.text.toString().trim { it <= ' ' }
        mCourseSelect = et_course_set.text.toString().trim { it <= ' ' }
    }


    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.btn_setting_course -> {
                    if (validateDateCourseSelect()) {
                        setCourseSelect()
                        val intent =
                            Intent(this@SettingCourseActivity, DashboardActivity::class.java)

                        val bundle = Bundle()

                        bundle.putString("dateSelect", mDateSelect)
                        bundle.putString("courseSelect", mCourseSelect)
                        bundle.putString("groupsId", mGroupsId)
                        intent.putExtras(bundle)

                        startActivity(intent)
                    }
                }

                R.id.btn_intent_groups_setting -> {
                    val intent =
                        Intent(this@SettingCourseActivity, SettingGroupsActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_intent_edit -> {
                    if (validateDateCourseSelect()) {
                        setCourseSelect()
                        val intent =
                            Intent(this@SettingCourseActivity, EditCourseActivity::class.java)
                        intent.putExtra(Constants.EXTRA_DATE_SELECT, mDateSelect)
                        intent.putExtra(Constants.EXTRA_COURSE_SELECT, mCourseSelect)
                        intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
                        startActivity(intent)
                    }
                }
                R.id.btn_intent_solicitation -> {
                    val intent =
                        Intent(this@SettingCourseActivity, SolicitationMapsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
                    startActivity(intent)
                }
                R.id.btn_intent_my_page -> {
                    val intent = Intent(this@SettingCourseActivity,UserProfileActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_course_setting_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }

        toolbar_course_setting_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateDateCourseSelect(): Boolean {
        return when {

            tv_name_groups.length() == 0 -> {
                showErrorSnackBar("グループ設定を行ってください", true)
                false
            }

            TextUtils.isEmpty(et_date_set.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_date), true)
                false
            }

            TextUtils.isEmpty(et_course_set.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_course), true)
                false
            }

            else -> {
                true
            }
        }
    }

}