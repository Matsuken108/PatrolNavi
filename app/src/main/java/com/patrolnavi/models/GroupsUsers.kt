package com.patrolnavi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GroupsUsers(
    val user_name: String = "",
    var user_id: String = "",
    val createdAt: String = "",
) : Parcelable