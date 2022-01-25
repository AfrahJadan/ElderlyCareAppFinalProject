package com.afrahjadan.elderlycareapp.data

data class AppointmentItem(
    var appointmentDate: String = "",
    var appointmentTime: String = "",
    var appointmentReason: String = "",
    var hospitalName: String = "",
    var appUserId: String = "",
)

data class UserModel(
    val medicineItem: List<MedicineItem> = listOf(),
    val appointment: List<AppointmentItem> = listOf()

)


//val timestamp: Long=0L
//{
//    fun isUpcoming(): Boolean =
//        System.currentTimeMillis() <= timestamp
//}

