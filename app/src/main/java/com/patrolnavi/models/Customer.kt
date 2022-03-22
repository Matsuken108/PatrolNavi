package com.patrolnavi.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(
    val date: String = "",
    val course: String = "",
    val no: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val customer_lat: String = "",
    val customer_lng: String = "",
//    val installationImage1:String = "",
//    val installationImage2:String = "",
    val memo: String = "",
    var customer_id: String = "",
    var groups_id: String = ""
) : Parcelable