package com.afrahjadan.elderlycareapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afrahjadan.elderlycareapp.appoitmentAdapter.AppAdapter
import com.afrahjadan.elderlycareapp.data.AppointmentItem
import com.afrahjadan.elderlycareapp.databinding.FragmentViewAndAddAppointmentBinding
import com.afrahjadan.elderlycareapp.viewmodel.AppointmentInfoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.launch


class ViewAppointmentFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private val viewModel: AppointmentInfoViewModel by activityViewModels()
    private var appInfo = mutableListOf<AppointmentItem>()

    private lateinit var binding: FragmentViewAndAddAppointmentBinding

    val auth = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewAndAddAppointmentBinding.inflate(inflater, container, false)
                binding.recycleApp.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.appAdd.setOnClickListener {
            val action = ViewAppointmentFragmentDirections.actionViewAppointmentFragmentToAddAppointmentInfoFragment()
            findNavController().navigate(action)
        }
//        getAppData()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.getTheapointments()
                getAppData()
            }
        }
    }

    private fun getAppData() {
        val adapter = AppAdapter()
        binding.recycleApp.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                val list = viewModel.getTheapointments()
                list.collect { apointList ->
                    apointList.let {
                        adapter.submitList(it)
                    }

                }

            }
        }
    }


}







