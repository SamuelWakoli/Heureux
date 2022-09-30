package com.inystudio.heureux.ui.account.profile_settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.inystudio.heureux.R
import com.inystudio.heureux.ui.model.User

class ProfileSettingsFragment : Fragment() {
    companion object {
        private const val USERS = "USERS"
        private val _userName = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        private val PROFILE_PICS = "USER PROFILE PICTURES/$_userName"
    }

    private lateinit var selectImgBtn: Button
    private lateinit var profileImgHolder: ImageView
    private lateinit var imageUri: Uri
    private lateinit var enteredName: TextInputEditText
    private lateinit var enteredPhoneNum: TextInputEditText
    private lateinit var enteredNatIdNum: TextInputEditText
    private lateinit var enteredResidence: TextInputEditText
    private lateinit var saveProfileBtn: Button
    private lateinit var isSavedMessage: TextView

    //text from screen
    private lateinit var name: String
    private lateinit var phoneNum: String
    private lateinit var natIdNum: String
    private lateinit var userReside: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectImgBtn = view.findViewById(R.id.select_image_btn)
        profileImgHolder = view.findViewById(R.id.selected_profile_img_view)
        enteredName = view.findViewById(R.id.user_name)
        enteredPhoneNum = view.findViewById(R.id.user_phone_number)
        enteredNatIdNum = view.findViewById(R.id.user_nat_id_number)
        enteredResidence = view.findViewById(R.id.user_residence)
        saveProfileBtn = view.findViewById(R.id.save_profile_btn)
        isSavedMessage = view.findViewById(R.id.saved_profile_message)

        selectImgBtn.setOnClickListener {
            selectImage()
        }

        saveProfileBtn.setOnClickListener {
            if (enteredName.text?.isEmpty() == true) enteredName.error = "Enter your name"
            if (enteredName.text?.isEmpty() == false
                && enteredName.text.toString().length < 6) enteredName.error = "Enter your full name"
            name = enteredName.text.toString()

            if (enteredPhoneNum.text?.isEmpty() == true) enteredPhoneNum.error = "Enter your phone number"
            if (enteredPhoneNum.text?.isEmpty() == false && enteredPhoneNum.text.toString().length !in 10..16) enteredPhoneNum.error = "Phone number not valid"
            phoneNum = enteredPhoneNum.text.toString()

            if (enteredNatIdNum.text?.isEmpty() == true) enteredNatIdNum.error = "Enter your national ID number"
            if (enteredNatIdNum.text?.isEmpty() == false
                && enteredNatIdNum.text.toString().length !in 8..16) enteredNatIdNum.error = "Correct your national ID number"
            natIdNum = enteredNatIdNum.text.toString()

            if (enteredResidence.text?.isEmpty() == true) enteredResidence.error = "Enter your place of residence"
            if (enteredResidence.text?.isEmpty() == false
                && enteredResidence.text.toString().length !in 5..16) enteredResidence.error = "Enter your place of residence"
            userReside = enteredResidence.text.toString()


            if (name.isNotEmpty() && name.length >= 6  &&
                phoneNum.isNotEmpty() && (phoneNum.length in 10..16) &&
                natIdNum.isNotEmpty() && (natIdNum.length in 8..16) &&
                userReside.isNotEmpty() && (userReside.length in 5..16)){

                Toast.makeText(requireContext(), "Saving...", Toast.LENGTH_LONG).show()
                //upload this info to db
                saveToDatabase(imageUri)
            }
        }
    }

    private fun selectImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 16)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 16 && data != null && data.data != null){
            imageUri = data.data!!
            profileImgHolder.setImageURI(imageUri)
        }
    }

    private fun saveToDatabase(uri: Uri){
        // First, upload the image
        val storageReference = FirebaseStorage.getInstance().getReference(PROFILE_PICS)
        storageReference.putFile(uri)
            .addOnSuccessListener(requireActivity())
            { taskSnapshot -> //2nd, get the downloadUrl for the image
            //then add it to the user account
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        val user =
                            User(uri.toString(), name, phoneNum, natIdNum, userReside, null)
                        val database = FirebaseDatabase.getInstance().getReference(USERS)
                        val userNodeInDB = FirebaseAuth.getInstance().currentUser?.displayName.toString()
                        database.child(userNodeInDB).setValue(user).addOnSuccessListener {

                            saveProfileBtn.isVisible = false
                            isSavedMessage.isVisible = true
                            selectImgBtn.isClickable = false
                            enteredName.isClickable = false
                            enteredPhoneNum.isClickable = false
                            enteredNatIdNum.isClickable = false
                            enteredResidence.isClickable = false

                            view?.let { it1 ->
                                Snackbar.make(it1, "Profile saved successfully.", Snackbar.LENGTH_LONG)
                                    .setAnchorView(enteredResidence).show() }
                            }
                            .addOnFailureListener {
                                view?.let { it1 ->
                                    Snackbar.make(it1, "Profile not saved. Please try again later.", Snackbar.LENGTH_LONG)
                                        .setAnchorView(enteredResidence).show() }
                            }

                    }
            }
            .addOnFailureListener {  }
    }
}