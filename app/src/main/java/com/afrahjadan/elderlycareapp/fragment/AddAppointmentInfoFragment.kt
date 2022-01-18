package com.afrahjadan.elderlycareapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.afrahjadan.elderlycareapp.data.AppointmentItem
import com.afrahjadan.elderlycareapp.databinding.FragmentAddAppointmentInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddAppointmentInfoFragment : Fragment() {

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
        binding.SaveToAddBtnApp.setOnClickListener {

            val action =
                AddAppointmentInfoFragmentDirections.actionAddAppointmentInfoFragmentToViewAppointmentFragment()
            findNavController().navigate(action)
            if (binding.AddAppDate.text!!.isNotEmpty() && binding.appTimeEt.text!!.isNotEmpty()
                && binding.appResEt.text!!.isNotEmpty() && binding.hospitalName.text!!.isNotEmpty()
            ) {
                val add = appDataBase.collection("Appointment").document()
                val appAdd = AppointmentItem(
                    binding.AddAppDate.text.toString(),
                    binding.appTimeEt.text.toString(),
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
                        ).show()
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
}


//                val appAdd = hashMapOf(
//                    "appointmentDate" to binding.AddAppDate.text.toString(),
//                    "appointmentTime" to binding.appTimeEt.text.toString(),
//                    "appointmentReason" to binding.appResEt.text.toString(),
//                    "hospitalName" to binding.hospitalName.text.toString(),
//                    "appUserId" to FirebaseAuth.getInstance().currentUser?.uid.toString()
//                )

//                appDataBase.collection("Appointment")
//                    .add(appAdd)







