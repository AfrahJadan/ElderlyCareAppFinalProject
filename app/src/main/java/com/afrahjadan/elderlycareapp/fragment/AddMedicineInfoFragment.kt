package com.afrahjadan.elderlycareapp.fragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.afrahjadan.elderlycareapp.data.MedicineItem
import com.afrahjadan.elderlycareapp.databinding.FragmentAddMedicineInfoBinding
import com.afrahjadan.elderlycareapp.util.*
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

            if (binding.medTypeEt.text!!.isNotEmpty() && binding.medTime.text!!.isNotEmpty() && binding.doseEt.text!!.isNotEmpty() && binding.medDateAdd.text!!.isNotEmpty()) {
//                val medAdd = hashMapOf(
//                    MEDTYPE to binding.medTypeEt.text.toString(),
//                    DOSE to binding.doseEt.text.toString().toInt(),
//                    MEDTIME to binding.medTime.text.toString(),
//                    MEDDATE to binding.medDateAdd.text.toString(),
//                    USERID to FirebaseAuth.getInstance().currentUser?.uid.toString()
//
//                )
    //    Log.d("TAG", "AddMedicindFragment onCreateView: ${medAdd["medDate"]}")
//            val adduserMad = medDataBase.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
//                .collection("Medicines").document()
//
//                val medAdd = hashMapOf(
//                    MEDTYPE to binding.medTypeEt.text.toString(),
//                    DOSE to binding.doseEt.text.toString(),
//                    MEDTIME to binding.medTime.text.toString(),
//                    MEDDATE to binding.medDateAdd.text.toString(),
//                    USERID to FirebaseAuth.getInstance().currentUser?.uid.toString(),
//                    id to adduserMad.id
//                )
//
//                adduserMad.set(medAdd) .addOnSuccessListener { documentReference ->
//                        Toast.makeText(
//                            context,
//                            "DocumentSnapshot added with ID: success",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    .addOnFailureListener { e ->
//                        Toast.makeText(context, "Error:" + e.toString(), Toast.LENGTH_SHORT).show()
//                    }

                       val add = medDataBase.collection("Medicines").document()

                val medAdd =MedicineItem(
                    binding.medTypeEt.text.toString(), binding.doseEt.text.toString().toInt(),
                      binding.medTime.text.toString(),
                      binding.medDateAdd.text.toString(),
                    FirebaseAuth.getInstance().currentUser?.uid.toString(),
                    add.id
                )
                    add.set(medAdd)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(
                            context,
                            "DocumentSnapshot added with ID: ${add.id}",
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

