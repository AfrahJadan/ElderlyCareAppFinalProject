package com.afrahjadan.elderlycareapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afrahjadan.elderlycareapp.R
import com.afrahjadan.elderlycareapp.data.AppointmentItem
import com.afrahjadan.elderlycareapp.databinding.FragmentEditAppointmentBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase


class EditAppointmentFragment : Fragment() {
    private lateinit var binding: FragmentEditAppointmentBinding
    private val navArg: EditAppointmentFragmentArgs by navArgs()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditAppointmentBinding.inflate(layoutInflater)
        getAppData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.SaveEditBtnApp.setOnClickListener {
            setAppData()
            val action =
                EditAppointmentFragmentDirections.actionEditAppointmentFragmentToViewAppointmentFragment()
            findNavController().navigate(action)
        }
    }

    private fun getAppData() {
        val id = navArg.id
        db.collection("Appointment").document(id).get()
            .addOnSuccessListener {
                val getApp = it.toObject(AppointmentItem::class.java)
                binding.apply {
                    editAppDate.setText(getApp?.appointmentDate.toString())
                    appTimeEdit.setText(getApp?.appointmentTime.toString())
                    appResEdit.setText(getApp?.appointmentReason.toString())
                    hospitalNameEdit.setText(getApp?.hospitalName.toString())
                }
            }

    }

    private fun setAppData() {
        val id = navArg.id
        db.collection("Appointment").document(id)
            .set(
                AppointmentItem(
                    appointmentDate = binding.editAppDate.text.toString(),
                    appointmentTime = binding.appTimeEdit.text.toString(),
                    appointmentReason = binding.appResEdit.text.toString(),
                    hospitalName = binding.hospitalNameEdit.text.toString(),
                    appUserId = Firebase.auth.currentUser!!.uid,
                    id = id
                ), SetOptions.merge()
            )
    }
}

