package com.patrolnavi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val mobile: Long = 0,
    val groupsCount: Int = 0,
) : Parcelable

