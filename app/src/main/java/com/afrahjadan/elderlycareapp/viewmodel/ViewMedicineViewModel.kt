package com.afrahjadan.elderlycareapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.afrahjadan.elderlycareapp.data.UserModel
import com.afrahjadan.elderlycareapp.data.MedicineItem
import com.afrahjadan.elderlycareapp.util.MEDICINEITEM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewMedicineViewModel: ViewModel() {

    private val medicinesDataBase = Firebase.firestore

    val auth = FirebaseAuth.getInstance().currentUser

    private var _isSuccess = MutableLiveData<Boolean>(false)
    val isSuccess = _isSuccess

    private val _medicines = MutableStateFlow(mutableListOf<MedicineItem>())
    val medicines = _medicines.asLiveData()

    init {
        getMedicine()
    }

    fun prepareTheMedicineData(medTypeEt: String, doseEt: String,
                               medDatePickBtn: String,medTimePickBtn:String, uiID:String ) {
        val medicineItem =
            MedicineItem(medTypeEt,doseEt
                ,medTimePickBtn,medDatePickBtn,uiID)

        setAndGetMedicines(medicineItem)

    }
    fun prepareTheMedicineDataToDelete( doseEt: String) {
        val medicineItem =
            MedicineItem(doseEt)

        setAndDeleteMedicines(medicineItem)

    }



    private fun setAndGetMedicines(medicineItem: MedicineItem){
        viewModelScope.launch {
            getMedicine().collect {
                val medicineList = it.toMutableList()
                medicineList.add(medicineItem)

                setTheMedicines(medicineList)
            }
        }
    }
    private fun setAndDeleteMedicines(medicineItem: MedicineItem){
        viewModelScope.launch {
            getMedicine().collect {
                val medicineList = it.find {
                    it.dose.contains(medicineItem.dose)}

                val newList = it.toMutableList()
                newList.remove(medicineItem)

                setTheMedicines(newList)
            }
        }
    }

    private fun setTheMedicines(medicinesItemList: List<MedicineItem>) {

        val add = medicinesDataBase.collection("User").document("${auth?.uid}")
        add.set(
            mapOf(MEDICINEITEM to medicinesItemList.toSet().toList() ), SetOptions.merge()
        ) .addOnCompleteListener {
            if (it.isSuccessful) _isSuccess.value = true
        }
    }
    fun updateTheList(medicinesItemList: List<MedicineItem>) {
        val update = medicinesDataBase.collection("User").document("${auth?.uid}")
        update.update(
            mapOf(MEDICINEITEM to medicinesItemList)
        ).addOnCompleteListener {
            if (it.isSuccessful) _isSuccess.value = true
        }

    }

    fun changeBoolean(boolean: Boolean){
        _isSuccess.value = boolean
    }

    fun getTheMedicines() = getMedicine()

    private fun getMedicine(): Flow<List<MedicineItem>> = callbackFlow {
        val getMedicine = medicinesDataBase.collection("User").document("${auth?.uid}")

        getMedicine.get().addOnCompleteListener { task ->
            val medicines = task.result.toObject(UserModel::class.java)
            _medicines.update {
                medicines?.medicineItem?.let {
                    it.toMutableList()
                }!!
            }
            trySend(medicines?.medicineItem ?: emptyList<MedicineItem>())
        }

        awaitClose {  }
    }

}