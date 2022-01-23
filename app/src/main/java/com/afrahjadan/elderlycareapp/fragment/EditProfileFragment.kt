package com.afrahjadan.elderlycareapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.afrahjadan.elderlycareapp.R
import com.afrahjadan.elderlycareapp.data.MedicineItem
import com.afrahjadan.elderlycareapp.data.User
import com.afrahjadan.elderlycareapp.databinding.FragmentEditProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val profileDataBase = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
binding = FragmentEditProfileBinding.inflate(layoutInflater)
        getProfileImage()
        binding.SaveChangEdit.setOnClickListener {
            val action = EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment()
            findNavController().navigate(action)

            val add = profileDataBase.collection("User").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            val profileAdd = User(
                binding.healthCareEdit.text.toString(), binding.NoteEdit.text.toString(),
                FirebaseAuth.getInstance().currentUser?.uid.toString(), add.id
            )
            add.set(profileAdd)
                .addOnCompleteListener {
                    Toast.makeText(
                        context,
                        "Successfully Added ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error:" + e.toString(), Toast.LENGTH_SHORT).show()
                }

        }
        return binding.root

    }

   private fun getProfileImage(){
        val image = FirebaseAuth.getInstance().currentUser?.photoUrl
        Glide.with(this)
            .load(image)
            .fitCenter()
            .placeholder(R.drawable.settings_icon)
            .into(binding.profileImageEdit)
    }

}