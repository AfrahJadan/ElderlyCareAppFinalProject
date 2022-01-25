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
        viewModel.isSuccess.observe(viewLifecycleOwner, {
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
                val  list =    viewModel.getTheAppointments()
                list.collect { appointmentList ->
                val  findMyaAppointment = appointmentList.find { id == it.appUserId }
                  appointmentItem = AppointmentItem(findMyaAppointment!!.appointmentDate,findMyaAppointment!!.appointmentTime,findMyaAppointment!!.appointmentReason,findMyaAppointment!!.hospitalName)

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
            viewModel.getTheAppointments().collect{

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


