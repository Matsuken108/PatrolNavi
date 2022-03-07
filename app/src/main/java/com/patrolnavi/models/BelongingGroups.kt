package com.patrolnavi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BelongingGroups(
    val groups_name: String = "",
    var groups_id: String = ""
) : Parcelable