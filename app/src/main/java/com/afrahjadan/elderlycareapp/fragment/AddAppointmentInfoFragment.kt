package com.afrahjadan.elderlycareapp.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.afrahjadan.elderlycareapp.data.AppointmentItem
import com.afrahjadan.elderlycareapp.databinding.FragmentAddAppointmentInfoBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class AddAppointmentInfoFragment : Fragment() {
lateinit var dateMedForm:String
    private lateinit var binding: FragmentAddAppointmentInfoBinding
    private val appDataBase = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddAppointmentInfoBinding.inflate(inflater, container, false)

binding.appDatePickBtn.setOnClickListener {
    dateDialog()
}
//        binding.appDatePickBtn.setOnClickListener {
//            val c = Calendar.getInstance()
//            val year = c.get(Calendar.YEAR)
//            val month = c.get(Calendar.MONTH)
//            val day = c.get(Calendar.DAY_OF_MONTH)
//
//            val datePicker = DatePickerDialog(
//                requireContext(),
//                DatePickerDialog.OnDateSetListener {
//
//                        view, year, monthofyear, dayOfMonth ->
//                    val months = monthofyear + 1
//
//                    binding.AddAppDate.setText("$dayOfMonth/$months/$year")
//                }
//                ,
//                year,
//                month,
//                day
//            )
//            datePicker.datePicker.maxDate = c.timeInMillis
//            datePicker.show()
//
//        }


        binding.appTimePickBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val min = cal.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    binding.appTimePickBtn.setText("$hourOfDay" + ":" + "$minute")
                },
                hour,
                min,
                true
            )
            timePickerDialog.show()

        }
        binding.SaveToAddBtnApp.setOnClickListener {

            val action =
                AddAppointmentInfoFragmentDirections.actionAddAppointmentInfoFragmentToViewAppointmentFragment()
            findNavController().navigate(action)
            if (binding.appDatePickBtn.text!!.isNotEmpty() && binding.appTimePickBtn.text!!.isNotEmpty()
                && binding.appResEt.text!!.isNotEmpty() && binding.hospitalName.text!!.isNotEmpty())
                {
                val add = appDataBase.collection("Appointment").document()
                val appAdd = AppointmentItem(
                    binding.appDatePickBtn.text.toString(),
                    binding.appTimePickBtn.text.toString(),
                    binding.appResEt.text.toString(),
                    binding.hospitalName.text.toString(),
                    FirebaseAuth.getInstance().currentUser?.uid.toString(),
                    add.id
                )
                add.set(appAdd)
                    //change from onSuccesses to onComplete
                    .addOnCompleteListener {
                        Toast.makeText(
                            context,
                            "Successfully Added",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error:" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(
                    context, "Please Enter Appointment First",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root

    }
    fun medFormatDate(medDate:Long){
        val formatter =SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val selectDate = formatter.format(medDate).toString()
        binding.appDatePickBtn.setText(selectDate)
    }
    fun dateDialog() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        picker.show(requireFragmentManager(), picker.toString())

        picker.addOnNegativeButtonClickListener {
        }
        picker.addOnPositiveButtonClickListener {
            medFormatDate(it)
        }
    }
    fun medicineIsPassed(){
    }
}









