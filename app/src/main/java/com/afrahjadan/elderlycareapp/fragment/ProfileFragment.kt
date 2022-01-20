package com.afrahjadan.elderlycareapp.fragment

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.afrahjadan.elderlycareapp.MainActivity
import com.afrahjadan.elderlycareapp.NotificationsActivity
import com.afrahjadan.elderlycareapp.R
import com.afrahjadan.elderlycareapp.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.notifications.setOnClickListener {
            val intent =
                Intent(this@ProfileFragment.requireContext(), NotificationsActivity::class.java)
            startActivity(intent)
        }

        SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        binding.editProfile.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
            findNavController().navigate(action)
        }
        getProfileImage()
        return binding.root
    }

    private fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            activity?.let {
                val intent = Intent(it, MainActivity::class.java)
                it.startActivity(intent)
            }

        } else {
            val email = firebaseUser.email
            binding.emailTv.text = email
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    fun getProfileImage(){
        val image = FirebaseAuth.getInstance().currentUser?.photoUrl
        Glide.with(this)
            .load(image)
            .fitCenter()
            .placeholder(R.drawable.settings_icon)
            .into(binding.profileImage)
    }

}