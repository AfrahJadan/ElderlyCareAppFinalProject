package com.afrahjadan.elderlycareapp.data


data class MedicineItem(
    var medType: String = "",
    var dose: String = " ", //was Int
    var medTime: String = "",
    var medDate: String = "",
    var userId: String = ""
//    val id: String = ""
)
//add
data class MedicineList(
    val medicineItem: List<MedicineItem> = listOf()
)