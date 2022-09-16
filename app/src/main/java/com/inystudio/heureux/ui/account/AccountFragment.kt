package com.inystudio.heureux.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHost
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.inystudio.heureux.R
import com.inystudio.heureux.ui.main.MainActivity

class AccountFragment : Fragment() {
    companion object {
        private const val USERS = "USERS"
    }

    private lateinit var profileAcc: Button
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

        accComment = view.findViewById(R.id.acc_comment)

        //loadUserData()
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
            val sharedPrefe =
                requireActivity().applicationContext.getSharedPreferences("local", Context.MODE_PRIVATE)
            val editor = sharedPrefe.edit()
            editor?.apply{
                putBoolean("isProfileCreated", true)
                apply()
            }
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

//    private fun loadUserData() {
//        val userNodeInDB = FirebaseAuth.getInstance().currentUser?.displayName.toString()
//        val database = FirebaseDatabase.getInstance().getReference(USERS)
//        database.child(userNodeInDB).get().addOnSuccessListener {
//            if (it.exists()) {
//                val userName = it.child("name").value
//
//                if (userName != null) {
//                    accComment.isVisible = false
//                }
//            } else {
//                view?.let { it1 ->
//                Snackbar.make(it1, "Please setup your profile.", Snackbar.LENGTH_LONG)
//                    .setAnchorView(profileAcc).show() }
//            }
//        }
//    }

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