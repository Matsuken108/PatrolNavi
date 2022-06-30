package com.patrolnavi.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.patrolnavi.R
import com.patrolnavi.databinding.ActivityForgotPasswordBinding
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivForgotPasswordHome.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarForgotPasswordActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_white)
        }
        binding.toolbarForgotPasswordActivity.setNavigationOnClickListener { onBackPressed() }
    }


    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.iv_forgot_password_home -> {
                    val intent =
                        Intent(this@ForgotPasswordActivity, SettingCourseActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_submit -> {
                    val email: String = binding.etForgotEmail.text.toString().trim { it <= ' ' }

                    if (email.isEmpty()) {
                        showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                    } else {

                        showProgressDialog(resources.getString(R.string.please_wait))

                        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->

                                hideProgressDialog()

                                if (task.isSuccessful) {

                                    Toast.makeText(
                                        this@ForgotPasswordActivity,
                                        resources.getString(R.string.email_sent_success),
                                        Toast.LENGTH_LONG
                                    ).show()

                                    finish()
                                } else {
                                    showErrorSnackBar(task.exception!!.message.toString(), true)
                                }
                            }
                    }
                }
            }
        }
    }
}