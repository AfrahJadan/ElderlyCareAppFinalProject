package com.afrahjadan.elderlycareapp.fragment


import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.afrahjadan.elderlycareapp.databinding.FragmentAddMedicineInfoBinding
import com.afrahjadan.elderlycareapp.viewmodel.ViewMedicineViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class AddMedicineInfoFragment : Fragment() {

    private val viewModel: ViewMedicineViewModel by activityViewModels()

    private lateinit var binding: FragmentAddMedicineInfoBinding
//    private val medDataBase = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    val auth = FirebaseAuth.getInstance().currentUser?.uid
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddMedicineInfoBinding.inflate(layoutInflater)

        binding.medDatePickBtn.setOnClickListener { dateDialog() }

        binding.medTimePickBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val min = cal.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    binding.medTimePickBtn.setText("$hourOfDay" + ":" + "$minute")
                },
                hour,
                min,
                true
            )
            timePickerDialog.show()

        }
        binding.SaveToAddBtn.setOnClickListener {


            if (binding.medTypeEt.text!!.isNotEmpty() && binding.medTimePickBtn.text!!.isNotEmpty()
                && binding.doseEt.text!!.isNotEmpty() && binding.medDatePickBtn.text!!.isNotEmpty()
            ) {
//                val add = medDataBase.collection("Medicines").document()
//                val medAdd = MedicineItem
                lifecycleScope.launch {
                    viewModel.prepareTheMedicineData(
                        binding.medTypeEt.text.toString(),
                        binding.doseEt.text.toString(),
                        binding.medTimePickBtn.text.toString(),
                        binding.medDatePickBtn.text.toString(),
                        FirebaseAuth.getInstance().currentUser?.uid.toString()
                    )
                }
//                    add.id
//                )
//                add.set(medAdd)
//                    //change from onSuccesses to onComplete
//                    .addOnCompleteListener {
//                        Toast.makeText(
//                            context,
//                            "Successfully Added ",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    .addOnFailureListener { e ->
//                        Toast.makeText(context, "Error:" + e.toString(), Toast.LENGTH_SHORT).show()
//                    }
//            } else {
//                Toast.makeText(context, "Please Enter Medicine First", Toast.LENGTH_SHORT).show()
//            }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val action =
            AddMedicineInfoFragmentDirections.actionAddMedicineInfoFragmentToViewMedicineFragment("")
        viewModel.isSuccess.observe(viewLifecycleOwner, {
            if (it == true) {
                findNavController().navigate(action)
                viewModel.changeBoolean(false)
            } else return@observe

        })

    }


    private fun medFormatDate(medDate: Long) {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val selectDate = formatter.format(medDate).toString()
        binding.medDatePickBtn.setText(selectDate)
    }

    private fun dateDialog() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        picker.show(requireFragmentManager(), picker.toString())

        picker.addOnNegativeButtonClickListener {
        }
        picker.addOnPositiveButtonClickListener {
            medFormatDate(it)
        }
    }
}