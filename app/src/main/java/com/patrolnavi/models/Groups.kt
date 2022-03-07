package com.patrolnavi.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Groups(
    val name: String = "",
    val password: String = "",
    val groups_lat: String = "",
    val groups_lng: String = "",
    val owner: String = "",
    var groups_id: String = "",
    val userCount: String = "",
    val createdAt: String = ""
) : Parcelable