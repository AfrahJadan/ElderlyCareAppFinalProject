package com.afrahjadan.elderlycareapp.data

data class AppointmentItem(
    var appointmentDate: String = "",
    var appointmentTime: String = "",
    var appointmentReason: String = "",
    var hospitalName: String = "",
    var appUserId: String = "",
    val id: String = ""

)

//data class AppointmentList(
//    val Appointments: MutableList<AppointmentItem?> = mutableListOf()
//)

//val timestamp: Long=0L
//{
//    fun isUpcoming(): Boolean =
//        System.currentTimeMillis() <= timestamp
//}

