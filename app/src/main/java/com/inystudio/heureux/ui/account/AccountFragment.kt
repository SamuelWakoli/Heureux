package com.inystudio.heureux.ui.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.inystudio.heureux.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AccountFragment : Fragment() {
    companion object {
        private const val USERS = "USERS"
    }

    private lateinit var userPhotoDisplay: ImageView
    private lateinit var userNameDisplay: TextView
    private lateinit var userEmailDisplay: TextView
    private lateinit var userPhoneDisplay: TextView

    private lateinit var makePaymentBtn: Button
    private lateinit var purchasesBtn: Button
    private lateinit var reportBtn: Button
    private lateinit var settingsBtn: Button
    private lateinit var accComment: TextView

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
        accComment = view.findViewById(R.id.acc_comment)

        loadUserData()
        ifProfileTrue(accComment)

        makePaymentBtn = view.findViewById(R.id.make_pay_btn)
        makePaymentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_makePaymentFragment)
        }

        purchasesBtn = view.findViewById(R.id.purchases_btn)
        purchasesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_viewMyPurchasesFragment)
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

    private fun ifProfileTrue(viewToGo: TextView) {

        val sharedPreferences =
            requireActivity().applicationContext.getSharedPreferences("local", Context.MODE_PRIVATE)
        val isProfileCreated = sharedPreferences.getBoolean("isProfileCreated", false)
        if (isProfileCreated) {
            viewToGo.isVisible = false
        }
    }

    private fun loadUserData() {
        val userNodeInDB = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        val database = FirebaseDatabase.getInstance().getReference(USERS)
        database.child(userNodeInDB).get().addOnSuccessListener {
            if (it.exists()) {
                val userName = it.child("name").value
                val userPicUrl = it.child("profileUrl").value.toString()
                val userPhone = it.child("phoneNumber").value

                val mReference = FirebaseStorage.getInstance().getReferenceFromUrl(userPicUrl)
                lifecycleScope.launch(Dispatchers.Main) {
                    val data = mReference.getBytes(1024*1024*10).await()
                    Log.d("data", "$data")
                    Glide.with(this@AccountFragment).load(data).circleCrop().into(userPhotoDisplay)
                }

                userNameDisplay.text = userName.toString()
                userPhoneDisplay.text = userPhone.toString()
                userEmailDisplay.text = FirebaseAuth.getInstance().currentUser?.email.toString()

                userPhotoDisplay.isVisible = true
                userNameDisplay.isVisible = true
                userEmailDisplay.isVisible = true
                userPhoneDisplay.isVisible = true

                if (userName != null) {
                    accComment.isVisible = false
                }
            } else {
                view?.let { it1 ->
                Snackbar.make(it1, "Please setup your profile.", Snackbar.LENGTH_LONG)
                    .setAnchorView(accComment).show() }
            }
        }
    }

//    private fun loadLiveData() {
//        val userNodeInDB = FirebaseAuth.getInstance().currentUser?.displayName.toString()
//        val database = FirebaseDatabase.getInstance().getReference(USERS)
//        database.child(userNodeInDB).addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    val userPicUrl = snapshot.child("profileUrl").value.toString()
//                    val userPhone = snapshot.child("phoneNumber").value
//                    val userName = snapshot.child("name").value
//
//                    //userPhotoDisplay.load(userPicUrl)
//                    //Picasso.get().load(userPicUrl).into(userPhotoDisplay)
//                    Glide.with(requireContext()).load(userPicUrl).circleCrop().into(userPhotoDisplay)
//                    userNameDisplay.text = userName.toString()
//                    userPhoneDisplay.text = userPhone.toString()
//
//                    userPhotoDisplay.isVisible = true
//                    userNameDisplay.isVisible = true
//                    userEmailDisplay.isVisible = true
//                    userPhoneDisplay.isVisible = true
//
//                    if (userName != null) {
//                        setProfileBtn.visibility = View.GONE
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//    }
}