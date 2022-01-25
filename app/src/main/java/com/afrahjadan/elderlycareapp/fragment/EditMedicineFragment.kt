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
import com.afrahjadan.elderlycareapp.data.MedicineItem
import com.afrahjadan.elderlycareapp.databinding.FragmentEditMedicineBinding
import com.afrahjadan.elderlycareapp.viewmodel.ViewMedicineViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class EditMedicineFragment : Fragment() {
    private lateinit var binding: FragmentEditMedicineBinding
    private val navarg: EditMedicineFragmentArgs by navArgs()
    private val viewModel: ViewMedicineViewModel by activityViewModels()
//    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditMedicineBinding.inflate(layoutInflater)
        getMadData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMadData()

        binding.SaveToEditBtn.setOnClickListener {
            setMadData()
            val action =
                EditMedicineFragmentDirections.actionEditMedicineFragmentToViewMedicineFragment("")
            findNavController().navigate(action)
        }
        val action =
            EditMedicineFragmentDirections.actionEditMedicineFragmentToViewMedicineFragment("")
        viewModel.isSuccess.observe(viewLifecycleOwner,{
            if (it== true) {findNavController().navigate(action)
                viewModel.changeBoolean(false)}
            else return@observe
        })
    }

    private fun getMadData() {
        val id = navarg.id
        var medicineItem:MedicineItem
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                val list = viewModel.getTheMedicines()
                list.collect{ medicineList ->
                   val findMyMedicine = medicineList.find { id == it.userId }
                    medicineItem = MedicineItem(findMyMedicine!!.medType, findMyMedicine!!.dose,findMyMedicine!!.medTime,findMyMedicine!!.medDate)
                    binding.medTypeEdit.setText(medicineItem.medType)
                    binding.doseEdit.setText(medicineItem.dose)
                    binding.medTimeEdit.setText(medicineItem.medTime)
                    binding.medDateEdit.setText(medicineItem.medDate)
                }
            }
        }
//        db.collection("Medicines").document(id).get()
//            .addOnSuccessListener {
//                val getMad = it.toObject(MedicineItem::class.java)
//                binding.apply {
//                    doseEdit.setText(getMad?.dose.toString())
//                    medDateEdit.setText(getMad?.medDate.toString())
//                    medTimeEdit.setText(getMad?.medTime.toString())
//                    medTypeEdit.setText(getMad?.medType.toString())
//                }
//            }
    }

    private fun setMadData() {
        val id = navarg.id
        lifecycleScope.launch {
            viewModel.getTheMedicines().collect{
                val medFind =it.find {
                    it.userId.contains(id) }
                val medicineList = it.toMutableList()
                medicineList[medicineList.indexOf(medFind)] = MedicineItem(
                    binding.medTypeEdit.text.toString(),
                    binding.doseEdit.text.toString(),
                    binding.medTimeEdit.text.toString(),
                    binding.medDateEdit.text.toString(),
                    id)
                  viewModel.updateTheList(medicineList)

            }
        }
//        db.collection("Medicines").document(id)
//            .set(
//                MedicineItem(
//                    dose = binding.doseEdit.text.toString(),
//                    medDate = binding.medDateEdit.text.toString(),
//                    medTime = binding.medTimeEdit.text.toString(),
//                    medType = binding.medTypeEdit.text.toString(),
//                    id = id,
//                    userId = Firebase.auth.currentUser!!.uid
//                ), SetOptions.merge()
//            )
    }
}