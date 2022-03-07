package com.patrolnavi.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val USERS: String = "users"
    const val CUSTOMER: String = "customer"
    const val GROUPS: String = "groups"
    const val GROUPS_USERS: String = "groups_users"
    const val BELONGING_GROUPS: String = "belonging_groups"

    const val EXTRA_GROUPS_ID: String = "extra_groups_id"
    const val EXTRA_GROUPS_NAME: String = "extra_groups_name"
    const val EXTRA_GROUPS_PASS: String = "extra_groups_pass"
    const val EXTRA_GROUPS_LAT: String = "extra_groups_lat"
    const val EXTRA_GROUPS_LNG: String = "extra_groups_lng"
    const val PREFERENCES: String = "preferences"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val BELONGING_GROUPS_ID: String = "belongingGroupsId"

    const val EXTRA_GROUPS_USER_NAME: String = "extra_groups_user_name"
    const val EXTRA_GROUPS_USER_ID: String = "extra_groups_user_id"

    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val EXTRA_CUSTOMER_DETAILS: String = "extra_customer_details"
    const val EXTRA_CUSTOMER_ID: String = "extra_customer_id"
    const val EXTRA_DATE_SELECT: String = "extra_date_select"
    const val EXTRA_COURSE_SELECT: String = "extra_course_select"
    const val EXTRA_NO_SELECT: String = "extra_no_select"
    const val EXTRA_FIRST_NAME: String = "extra_fist_name"
    const val EXTRA_LAST_NAME: String = "extra_last_name"

    const val EXTRA_LAT_SELECT: String = "extra_lat_select"
    const val EXTRA_LNG_SELECT: String = "extra_lng_select"

    const val PICK_IMAGE_REQUEST_CODE = 2

    const val DATE: String = "date"
    const val COURSE: String = "course"
    const val NO: String = "no"
    const val FIRST_NAME: String = "firstName"
    const val LAST_NAME: String = "lastName"
    const val LATLNG: String = "latlng"
    const val MEMO: String = "memo"


    const val MOBILE: String = "mobile"

//    fun showImageChooser(activity: Activity) {
//        val galleryIntent = Intent(
//            Intent.ACTION_PICK,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        )
//        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
//    }
//
//    fun getFileExtension(activity: Activity, uri: Uri?): String? {
//        return MimeTypeMap.getSingleton()
//            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
//    }
}