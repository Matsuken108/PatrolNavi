package com.patrolnavi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivitySettingCourseBinding
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.activity_setting_course.*

class SettingCourseActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingCourseBinding

    private var mDateSelect: String = ""
    private var mCourseSelect: String = ""

    private var mGroupsId: String = ""
    private var mGroupsName: String = ""
    private var mGroupsLat: String = ""
    private var mGroupsLng: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_GROUPS_ID)) {
            mGroupsId = intent.getStringExtra(Constants.EXTRA_GROUPS_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_NAME)) {
            mGroupsName = intent.getStringExtra(Constants.EXTRA_GROUPS_NAME)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_LAT)) {
            mGroupsLat = intent.getStringExtra(Constants.EXTRA_GROUPS_LAT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_GROUPS_LNG)) {
            mGroupsLng = intent.getStringExtra(Constants.EXTRA_GROUPS_LNG)!!
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        binding.tvNameGroups.setText(mGroupsName)

        if (binding.tvNameGroups.length() == 0) {
            binding.tvNameGroupsTitle.visibility = View.GONE
            binding.tvNameGroups.visibility = View.GONE
            binding.tvNameGroupsFound.visibility = View.VISIBLE
        } else {
            binding.tvNameGroupsTitle.visibility = View.VISIBLE
            binding.tvNameGroups.visibility = View.VISIBLE
            binding.tvNameGroupsFound.visibility = View.GONE
        }

        binding.btnSettingCourse.setOnClickListener(this)
        binding.btnIntentEdit.setOnClickListener(this)
        binding.btnIntentSolicitation.setOnClickListener(this)
        binding.btnIntentGroupsSetting.setOnClickListener(this)
        binding.btnIntentMyPage.setOnClickListener(this)

    }

    fun setCourseSelect() {
        mDateSelect = binding.etDateSet.text.toString().trim { it <= ' ' }
        mCourseSelect = binding.etCourseSet.text.toString().trim { it <= ' ' }
    }


    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.btn_intent_groups_setting -> {
                    val intent =
                        Intent(this@SettingCourseActivity, SettingGroupsActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_setting_course -> {
                    if (validateDateCourseSelect()) {
                        setCourseSelect()
                        val intent =
                            Intent(this@SettingCourseActivity, DashboardActivity::class.java)

                        val bundle = Bundle()

                        bundle.putString("dateSelect", mDateSelect)
                        bundle.putString("courseSelect", mCourseSelect)
                        bundle.putString("groupsId", mGroupsId)
                        bundle.putString("groupsLat",mGroupsLat)
                        bundle.putString("groupsLng",mGroupsLng)
                        intent.putExtras(bundle)

                        startActivity(intent)
                    }
                }

                R.id.btn_intent_edit -> {
                    if (validateDateCourseSelect()) {
                        setCourseSelect()
                        val intent =
                            Intent(this@SettingCourseActivity, EditCourseActivity::class.java)
                        intent.putExtra(Constants.EXTRA_DATE_SELECT, mDateSelect)
                        intent.putExtra(Constants.EXTRA_COURSE_SELECT, mCourseSelect)
                        intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
                        intent.putExtra(Constants.EXTRA_GROUPS_LAT,mGroupsLat)
                        intent.putExtra(Constants.EXTRA_GROUPS_LNG,mGroupsLng)

                        startActivity(intent)
                    }
                }
                R.id.btn_intent_solicitation -> {
                    val intent =
                        Intent(this@SettingCourseActivity, SolicitationMapsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_GROUPS_ID, mGroupsId)
                    intent.putExtra(Constants.EXTRA_GROUPS_LAT,mGroupsLat)
                    intent.putExtra(Constants.EXTRA_GROUPS_LNG,mGroupsLng)
                    startActivity(intent)
                }
                R.id.btn_intent_my_page -> {
                    val intent = Intent(this@SettingCourseActivity, DetailsUserProfileActivity::class.java)
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

            binding.tvNameGroups.length() == 0 -> {
                showErrorSnackBar("グループ設定を行ってください", true)
                false
            }

            TextUtils.isEmpty(binding.etDateSet.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_date), true)
                false
            }

            TextUtils.isEmpty(binding.etCourseSet.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_customer_course), true)
                false
            }

            else -> {
                true
            }
        }
    }

}