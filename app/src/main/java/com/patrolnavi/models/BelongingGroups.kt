package com.patrolnavi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BelongingGroups(
    var groups_id: String = "",
    val groups_name: String = "",
    var groups_user_id: String = "",
    var belonging_groups_id: String = "",
    val groups_user_name: String = "",
    var user_id: String = "",
    val createdAt: String = "",
) : Parcelable