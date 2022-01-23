package com.afrahjadan.elderlycareapp.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.afrahjadan.elderlycareapp.databinding.FragmentAddAppointmentInfoBinding
import com.afrahjadan.elderlycareapp.viewmodel.AppointmentInfoViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class AddAppointmentInfoFragment : Fragment() {

    private val viewModel: AppointmentInfoViewModel by activityViewModels()

    private lateinit var binding: FragmentAddAppointmentInfoBinding
    private val appDataBase = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    val auth = FirebaseAuth.getInstance().currentUser?.uid
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddAppointmentInfoBinding.inflate(inflater, container, false)

binding.appDatePickBtn.setOnClickListener {
    dateDialog()
}

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


            if (binding.appDatePickBtn.text!!.isNotEmpty() && binding.appTimePickBtn.text!!.isNotEmpty()
                && binding.appResEt.text!!.isNotEmpty() && binding.hospitalName.text!!.isNotEmpty()
            ) {
                lifecycleScope.launch {
                viewModel.prepareTheAppointmentData(
                    binding.appDatePickBtn.text.toString(),
                    binding.appTimePickBtn.text.toString(),
                    binding.appResEt.text.toString(),
                    binding.hospitalName.text.toString(),
                    FirebaseAuth.getInstance().currentUser?.uid.toString())


                }

            }

        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        lifecycleScope.launch {
            val action =
                AddAppointmentInfoFragmentDirections.actionAddAppointmentInfoFragmentToViewAppointmentFragment()
//            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.isSucces.observe(viewLifecycleOwner, {
                    if (it== true) {findNavController().navigate(action)
                    viewModel.changeBoolean(false)}
                    else return@observe
                })

        }


    private fun appFormatDate(appDate: Long) {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val selectDate = formatter.format(appDate).toString()
        binding.appDatePickBtn.setText(selectDate)
    }

    private fun dateDialog() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        picker.show(requireFragmentManager(), picker.toString())

        picker.addOnNegativeButtonClickListener {
        }
        picker.addOnPositiveButtonClickListener {
            appFormatDate(it)
        }
    }
}









