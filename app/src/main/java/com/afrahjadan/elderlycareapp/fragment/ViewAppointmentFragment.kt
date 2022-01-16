package com.afrahjadan.elderlycareapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afrahjadan.elderlycareapp.appoitmentAdapter.AppAdapter
import com.afrahjadan.elderlycareapp.data.AppointmentItem
import com.afrahjadan.elderlycareapp.data.MedicineItem
import com.afrahjadan.elderlycareapp.databinding.FragmentViewAndAddAppointmentBinding
import com.afrahjadan.elderlycareapp.medicineAdapter.MedAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*


class ViewAppointmentFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<AppAdapter.AppViewHolder>? = null
    val db = FirebaseFirestore.getInstance()
    private var appInfo = mutableListOf<AppointmentItem?>()
    private lateinit var binding: FragmentViewAndAddAppointmentBinding


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
        eventChangeListener()
        return binding.root
    }

    private fun eventChangeListener() {
        db.collection("Appointment").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        appInfo.add(dc.document.toObject(AppointmentItem::class.java))
                    }
                }
                val adapter =
                    AppAdapter(appInfo.filter { it?.appUserId == FirebaseAuth.getInstance().currentUser?.uid }
                        .toMutableList())
                binding.recycleApp.adapter =adapter
            }
            })
        }
    }







