package com.afrahjadan.elderlycareapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.afrahjadan.elderlycareapp.data.AppointmentItem
import com.afrahjadan.elderlycareapp.data.UserModel
import com.afrahjadan.elderlycareapp.util.APPOINTMENT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppointmentInfoViewModel: ViewModel() {

    private val appDataBase = Firebase.firestore

    val auth = FirebaseAuth.getInstance().currentUser

    private var _isSuccess = MutableLiveData<Boolean>(false)
  val isSuccess = _isSuccess

    private val _appointments = MutableStateFlow(mutableListOf<AppointmentItem>())
    val appointments = _appointments.asLiveData()
init {
    getAppointment()
}

    fun prepareTheAppointmentData(appDatePickBtn: String, appTimePickBtn: String,
                              appResEt: String,hospitalName:String, uiID:String ) {
        val appointmentItem =
            AppointmentItem(appDatePickBtn,appTimePickBtn
             ,appResEt,hospitalName,uiID)

        setAndGetAppointments(appointmentItem)

    }



    private fun setAndGetAppointments(appointmentItem: AppointmentItem){
        viewModelScope.launch {
            getAppointment().collect {
                val appointmentList = it.toMutableList()
                appointmentList.add(appointmentItem)

                setTheAppointment(appointmentList)
            }
        }
    }

     private fun setTheAppointment(appointmentItemList: List<AppointmentItem>) {

        val add = appDataBase.collection("User").document("${auth?.uid}")

        add.set(
            mapOf(APPOINTMENT to appointmentItemList.toSet().toList() ), SetOptions.merge()
        ) .addOnCompleteListener {
            if (it.isSuccessful) _isSuccess.value = true
        }

    }

    fun updateTheList(appointmentItemList: List<AppointmentItem>) {
        val update = appDataBase.collection("User").document("${auth?.uid}")
        update.update(
            mapOf(APPOINTMENT to appointmentItemList)
        ).addOnCompleteListener {
            if (it.isSuccessful) _isSuccess.value = true
        }

    }

    fun changeBoolean(boolean: Boolean){
        _isSuccess.value = boolean
    }

    fun getTheAppointments() = getAppointment()

   private fun getAppointment(): Flow<List<AppointmentItem>> = callbackFlow {
        val getAppointment = appDataBase.collection("User").document("${auth?.uid}")

    getAppointment.get().addOnCompleteListener { task ->
        val appointments = task.result.toObject(UserModel::class.java)
        _appointments.update {
            appointments?.appointment?.let {
                it.toMutableList()
            }!!
        }
        trySend(appointments?.appointment ?: emptyList<AppointmentItem>())
    }

        awaitClose {  }
    }
}