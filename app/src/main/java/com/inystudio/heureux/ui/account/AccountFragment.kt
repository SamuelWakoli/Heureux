package com.inystudio.heureux.ui.account

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
//import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.inystudio.heureux.R
import com.squareup.picasso.Picasso


class AccountFragment : Fragment() {
    companion object {
        private const val USERS = "USERS"
    }

    private lateinit var makePaymentBtn: Button
    private lateinit var setProfileBtn: Button
    private lateinit var purchasesBtn: Button
    private lateinit var termsBtn: Button
    private lateinit var reportBtn: Button
    private lateinit var settingsBtn: Button

    private lateinit var userPhotoDisplay: ImageView
    private lateinit var userNameDisplay: TextView
    private lateinit var userEmailDisplay: TextView
    private lateinit var userPhoneDisplay: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPhotoDisplay = view.findViewById(R.id.user_photo_display_acc)
        userNameDisplay = view.findViewById(R.id.user_name_display)
        userEmailDisplay = view.findViewById(R.id.user_email_display)
        userPhoneDisplay = view.findViewById(R.id.user_phone_display)

        userPhotoDisplay.isVisible = false
        userNameDisplay.isVisible = false
        userEmailDisplay.isVisible = false
        userPhoneDisplay.isVisible = false

        setProfileBtn = view.findViewById(R.id.set_profile_btn)
        setProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_profileSettingsFragment)
        }
        if (userNameDisplay.text != getString(R.string.user_name)) {
            setProfileBtn.visibility = View.GONE
        }

        loadLiveData()

        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        userEmailDisplay.text = userEmail.toString()

        makePaymentBtn = view.findViewById(R.id.make_pay_btn)
        makePaymentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_makePaymentFragment)
        }

        purchasesBtn = view.findViewById(R.id.purchases_btn)
        purchasesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_viewMyPurchasesFragment)
        }

        termsBtn = view.findViewById(R.id.ts_and_cs_btn)
        termsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_termsFragment)
        }

        reportBtn = view.findViewById(R.id.report_btn)
        reportBtn.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_reportIssueFragment)
        }

        settingsBtn = view.findViewById(R.id.settings_btn)
        settingsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_settingsFragment)
        }
    }

    private fun loadUserData() {
        val userNodeInDB = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        val database = FirebaseDatabase.getInstance().getReference(USERS)
        database.child(userNodeInDB).get().addOnSuccessListener {
            if (it.exists()) {
                val userPicUrl = it.child("profileUrl").value
                val userPhone = it.child("phoneNumber").value
                val userName = it.child("name").value

                //userPhotoDisplay.load(userPicUrl)
                //Picasso.get().load(userPicUrl).into(userPhotoDisplay)
                //Glide.with(this).load(userPicUrl).circleCrop().into(userPhotoDisplay)
                userNameDisplay.text = userName.toString()
                userPhoneDisplay.text = userPhone.toString()

                userPhotoDisplay.isVisible = true
                userNameDisplay.isVisible = true
                userEmailDisplay.isVisible = true
                userPhoneDisplay.isVisible = true

                if (userName != null) {
                    setProfileBtn.visibility = View.GONE
                }
            } else {
                view?.let { it1 ->
                Snackbar.make(it1, "Please setup your profile picture.", Snackbar.LENGTH_LONG)
                    .setAnchorView(userPhotoDisplay).show() }
            }
        }


    }

    private fun loadLiveData() {
        val userNodeInDB = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        val database = FirebaseDatabase.getInstance().getReference(USERS)
        database.child(userNodeInDB).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userPicUrl = snapshot.child("profileUrl").value.toString()
                    val userPhone = snapshot.child("phoneNumber").value
                    val userName = snapshot.child("name").value

                    //userPhotoDisplay.load(userPicUrl)
                    //Picasso.get().load(userPicUrl).into(userPhotoDisplay)
                    Glide.with(requireContext()).load(userPicUrl).circleCrop().into(userPhotoDisplay)
                    userNameDisplay.text = userName.toString()
                    userPhoneDisplay.text = userPhone.toString()

                    userPhotoDisplay.isVisible = true
                    userNameDisplay.isVisible = true
                    userEmailDisplay.isVisible = true
                    userPhoneDisplay.isVisible = true

                    if (userName != null) {
                        setProfileBtn.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}