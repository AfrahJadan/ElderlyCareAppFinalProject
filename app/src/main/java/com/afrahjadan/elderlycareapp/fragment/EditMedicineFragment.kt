package com.afrahjadan.elderlycareapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afrahjadan.elderlycareapp.data.MedicineItem
import com.afrahjadan.elderlycareapp.databinding.FragmentEditMedicineBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase


class EditMedicineFragment : Fragment() {
    private lateinit var binding: FragmentEditMedicineBinding
    private val navarg: EditMedicineFragmentArgs by navArgs()
    val db = FirebaseFirestore.getInstance()
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


        binding.SaveToEditBtn.setOnClickListener {
            setMadData()
            val action =
                EditMedicineFragmentDirections.actionEditMedicineFragmentToViewMedicineFragment()
            findNavController().navigate(action)
        }
    }

    private fun getMadData() {
        val id = navarg.id
        db.collection("Medicines").document(id).get()
            .addOnSuccessListener {
                val getMad = it.toObject(MedicineItem::class.java)
                binding.apply {
                    doseEdit.setText(getMad?.dose.toString())
                    medDateEdit.setText(getMad?.medDate.toString())
                    medTimeEdit.setText(getMad?.medTime.toString())
                    medTypeEdit.setText(getMad?.medType.toString())
                }
            }
    }

    private fun setMadData() {
        val id = navarg.id
        db.collection("Medicines").document(id)
            .set(
                MedicineItem(
                    dose = binding.doseEdit.text.toString().toInt(),
                    medDate = binding.medDateEdit.text.toString(),
                    medTime = binding.medTimeEdit.text.toString(),
                    medType = binding.medTypeEdit.text.toString(),
                    id = id,
                    userId = Firebase.auth.currentUser!!.uid
                ), SetOptions.merge()
            )
    }
}