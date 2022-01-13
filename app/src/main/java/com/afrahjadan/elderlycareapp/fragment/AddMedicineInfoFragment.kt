package com.afrahjadan.elderlycareapp.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.afrahjadan.elderlycareapp.databinding.FragmentAddMedicineInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddMedicineInfoFragment : Fragment() {

    private lateinit var binding: FragmentAddMedicineInfoBinding
    private val medDataBase = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_add_medicine_info, container, false)
        binding = FragmentAddMedicineInfoBinding.inflate(layoutInflater)
        binding.SaveToAddBtn.setOnClickListener {
            val action =
                AddMedicineInfoFragmentDirections.actionAddMedicineInfoFragmentToViewMedicineFragment()
            findNavController().navigate(action)

            if (binding.medTypeEt.text!!.isNotEmpty() && binding.medTime.text!!.isNotEmpty() && binding.doseEt.text!!.isNotEmpty()) {
                val medAdd = hashMapOf(
                    "medType" to binding.medTypeEt.text.toString(),
                    "dose" to binding.doseEt.text.toString().toInt(),
                    "medTime" to binding.medTime.text.toString(),
                    "userId" to FirebaseAuth.getInstance().currentUser?.uid.toString()
                )
                medDataBase.collection("Medicines")
                    .add(medAdd)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(
                            context,
                            "DocumentSnapshot added with ID: ${documentReference.id}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error:" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Please Enter Medicine First", Toast.LENGTH_SHORT).show()
            }

        }
        return binding.root
    }
}

