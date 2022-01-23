package com.afrahjadan.elderlycareapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afrahjadan.elderlycareapp.data.AppointmentItem
import com.afrahjadan.elderlycareapp.databinding.FragmentEditAppointmentBinding
import com.afrahjadan.elderlycareapp.viewmodel.AppointmentInfoViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class EditAppointmentFragment : Fragment() {
    private lateinit var binding: FragmentEditAppointmentBinding
    private val navArg: EditAppointmentFragmentArgs by navArgs()
    private val viewModel: AppointmentInfoViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditAppointmentBinding.inflate(layoutInflater)
        getAppData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAppData()

        binding.SaveEditBtnApp.setOnClickListener {
            setAppData()
            val action =
                EditAppointmentFragmentDirections.actionEditAppointmentFragmentToViewAppointmentFragment()
            findNavController().navigate(action)
        }

        val action =
            EditAppointmentFragmentDirections.actionEditAppointmentFragmentToViewAppointmentFragment()
//            repeatOnLifecycle(Lifecycle.State.RESUMED) {
        viewModel.isSucces.observe(viewLifecycleOwner, {
            if (it== true) {findNavController().navigate(action)
                viewModel.changeBoolean(false)}
            else return@observe
        })
    }

    private fun getAppData() {
        val id = navArg.id
        var appointmentItem: AppointmentItem
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                val  list =    viewModel.getTheapointments()
                list.collect { apointList ->
                val  findMyappoitment = apointList.find { id == it.appUserId }
                  appointmentItem = AppointmentItem(findMyappoitment!!.appointmentDate,findMyappoitment!!.appointmentTime,findMyappoitment!!.appointmentReason,findMyappoitment!!.hospitalName)

                    binding.hospitalNameEdit.setText(appointmentItem.hospitalName)
                    binding.appResEdit.setText(appointmentItem.appointmentReason)
                    binding.appTimeEdit.setText(appointmentItem.appointmentTime)
                    binding.editAppDate.setText(appointmentItem.appointmentDate)
                }
        }

            }

    }

    private fun setAppData() {
        val id = navArg.id
        lifecycleScope.launch {
            viewModel.getTheapointments().collect{

              val x =  it.find {
                  it.appUserId.contains(id)}
                val appoitnmetList = it.toMutableList()
                appoitnmetList[appoitnmetList.indexOf(x)] = AppointmentItem(
                    binding.editAppDate.text.toString(),
                    binding.appTimeEdit.text.toString(),
                    binding.appResEdit.text.toString(),
                    binding.hospitalNameEdit.text.toString(),
                    id)

                viewModel.updateTheList(appoitnmetList)


              }
        }

        }
    }


