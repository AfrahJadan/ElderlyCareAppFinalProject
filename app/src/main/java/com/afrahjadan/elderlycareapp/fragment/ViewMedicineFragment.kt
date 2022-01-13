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
import com.afrahjadan.elderlycareapp.data.MedicineItem
import com.afrahjadan.elderlycareapp.databinding.FragmentViewAndAddMedicineBinding
import com.afrahjadan.elderlycareapp.medicineAdapter.MedAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class ViewMedicineFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MedAdapter.MedViewHolder>? = null
    val db = FirebaseFirestore.getInstance()
    private var medInfo = mutableListOf<MedicineItem?>()
    private lateinit var binding: FragmentViewAndAddMedicineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentViewAndAddMedicineBinding.inflate(inflater, container, false)
        binding.recycleMed.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.medAdd.setOnClickListener {
            val action =
                ViewMedicineFragmentDirections.actionViewMedicineFragmentToAddMedicineInfoFragment()
            findNavController().navigate(action)
        }
        eventChangeListener()
        return binding.root

    }
    private fun eventChangeListener() {
        db.collection("Medicines").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        medInfo.add(dc.document.toObject(MedicineItem::class.java))
                    }
                }
                val adapter =
                    MedAdapter(medInfo.filter { it?.userId == FirebaseAuth.getInstance().currentUser?.uid }
                        .toMutableList())
                binding.recycleMed.adapter = adapter
            }
        })
    }
}