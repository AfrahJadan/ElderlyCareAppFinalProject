package com.afrahjadan.elderlycareapp.fragment


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.afrahjadan.elderlycareapp.data.MedicineItem
import com.afrahjadan.elderlycareapp.databinding.FragmentAddMedicineInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddMedicineInfoFragment : Fragment() {

    private lateinit var binding: FragmentAddMedicineInfoBinding
    private val medDataBase = Firebase.firestore

    private val medTypeLiveData = MutableLiveData<String>()
    private val medDoseLiveData = MutableLiveData<String>()
    private val isValidLiveData = MediatorLiveData<Boolean>().apply {
        addSource(medTypeLiveData) { medtype ->
            val medicineLD = medDoseLiveData.value
            this.value = medValid(medicineLD!!, medtype)

        }
        addSource(medDoseLiveData) { medicineLD ->
            val medtype = medTypeLiveData.value
            this.value = medValid(medicineLD, medtype!!)

        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddMedicineInfoBinding.inflate(layoutInflater)
        binding.medDatePickBtn.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthofyear, dayOfMonth ->
                    val months = monthofyear + 1
                    binding.medDateAdd.setText("$dayOfMonth/$months/$year")
                },
                year,
                month,
                day
            )
            datePicker.datePicker.maxDate = c.timeInMillis
            datePicker.show()
        }
        binding.medTimePickBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val min = cal.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    binding.medTimePick.setText("$hourOfDay" + ":" + "$minute")
                },
                hour,
                min,
                true
            )
            timePickerDialog.show()

        }
        binding.layout2.editText?.doOnTextChanged { text, _, _, _ ->
            medTypeLiveData.value = text?.toString()
        }
        binding.tlayot.editText?.doOnTextChanged { text, _, _, _ ->
            medTypeLiveData.value = text?.toString()
        }
        isValidLiveData.observe(viewLifecycleOwner) { isvalid ->
            binding.SaveToAddBtn.isEnabled = isvalid
        }
        binding.SaveToAddBtn.setOnClickListener {
            val action =
                AddMedicineInfoFragmentDirections.actionAddMedicineInfoFragmentToViewMedicineFragment()
            findNavController().navigate(action)

            if (binding.medTypeEt.text!!.isNotEmpty() && binding.medTimePick.text!!.isNotEmpty() && binding.doseEt.text!!.isNotEmpty() && binding.medDateAdd.text!!.isNotEmpty()) {


                val add = medDataBase.collection("Medicines").document()

                val medAdd = MedicineItem(
                    binding.medTypeEt.text.toString(), binding.doseEt.text.toString().toInt(),
                    binding.medTimePick.text.toString(),
                    binding.medDateAdd.text.toString(),
                    FirebaseAuth.getInstance().currentUser?.uid.toString(),
                    add.id
                )
                add.set(medAdd)
                    //change from onSuccesses to onComplete
                    .addOnCompleteListener {
                        Toast.makeText(
                            context,
                            "Successfully Added ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error:" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Please Enter Medicine First", Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }

    private fun medValid(medtype: String, medicineLD: String): Boolean {
        val isValidType = medtype != null && medtype.isNotBlank() && medtype.isNotEmpty()
        val isValidDose =
            medicineLD != null && medicineLD.isNotBlank() && medicineLD.isNotEmpty()
        return isValidDose && isValidType
    }
}