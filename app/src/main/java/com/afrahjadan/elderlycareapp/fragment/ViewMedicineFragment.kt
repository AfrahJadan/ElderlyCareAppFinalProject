package com.afrahjadan.elderlycareapp.fragment

import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afrahjadan.elderlycareapp.data.MedicineItem
import com.afrahjadan.elderlycareapp.databinding.FragmentViewAndAddMedicineBinding
import com.afrahjadan.elderlycareapp.medicineAdapter.MedAdapter
import com.afrahjadan.elderlycareapp.viewmodel.ViewMedicineViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewMedicineFragment : Fragment() {

//    private var layoutManager: RecyclerView.LayoutManager? = null
//    private var adapter: RecyclerView.Adapter<MedAdapter.MedViewHolder>? = null
    val db = FirebaseFirestore.getInstance()
    private val navarg: ViewMedicineFragmentArgs by navArgs()
    private val viewModel: ViewMedicineViewModel by activityViewModels()
//    private var medInfo = mutableListOf<MedicineItem?>()
    private lateinit var binding: FragmentViewAndAddMedicineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        deleteItem()

        binding = FragmentViewAndAddMedicineBinding.inflate(inflater, container, false)
        binding.recycleMed .apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.medAdd.setOnClickListener {
            val action =
                ViewMedicineFragmentDirections.actionViewMedicineFragmentToAddMedicineInfoFragment()
            findNavController().navigate(action)
        }

//        eventChangeListener()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.getTheMedicines()
                getMedData()
            }
        }
    }

    private fun getMedData() {
        val adapter = MedAdapter()
        binding.recycleMed.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                val list = viewModel.getTheMedicines()
                list.collect{medList ->
                    medList.let {
                        adapter.submitList(it)
                    }
                }
            }
        }
    }

    fun deleteItem(){
        val dose = navarg.dose
        if (dose.isNotBlank()) viewModel.prepareTheMedicineDataToDelete(dose)

    }

}

