package com.inystudio.heureux.ui.account.profile_settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.inystudio.heureux.R
import java.util.*

class ProfileSettingsFragment : Fragment() {

    private lateinit var selectImgBtn: Button
    private lateinit var profileImgHolder: ImageView
    private lateinit var imageUri: Uri
    private lateinit var enteredName: TextInputEditText
    private lateinit var enteredPhoneNum: TextInputEditText
    private lateinit var enteredNatIdNum: TextInputEditText
    private lateinit var enteredResidence: TextInputEditText
    private lateinit var saveProfileBtn: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage

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

        selectImgBtn.setOnClickListener {
            selectImage()
        }

        saveProfileBtn.setOnClickListener {
            if (enteredName.text?.isEmpty() == true) enteredName.error = "Enter your name"
            if (enteredName.text?.isEmpty() == false
                && enteredName.text.toString().length < 6) enteredName.error = "Enter your full name"
            val name = enteredName.text.toString()

            if (enteredPhoneNum.text?.isEmpty() == true) enteredPhoneNum.error = "Enter your phone number"
            if (enteredPhoneNum.text?.isEmpty() == false && enteredPhoneNum.text.toString().length !in 10..16) enteredPhoneNum.error = "Phone number not valid"
            val phoneNum = enteredPhoneNum.text.toString()

            if (enteredNatIdNum.text?.isEmpty() == true) enteredNatIdNum.error = "Enter your national ID number"
            if (enteredNatIdNum.text?.isEmpty() == false
                && enteredNatIdNum.text.toString().length !in 8..16) enteredNatIdNum.error = "Correct your national ID number"
            val natIdNum = enteredNatIdNum.text.toString()

            if (enteredResidence.text?.isEmpty() == true) enteredResidence.error = "Enter your place of residence"
            if (enteredResidence.text?.isEmpty() == false
                && enteredResidence.text.toString().length !in 5..16) enteredResidence.error = "Enter your place of residence"
            val userReside = enteredResidence.text.toString()


            if (name.isNotEmpty() && name.length >= 6  &&
                phoneNum.isNotEmpty() && (phoneNum.length in 10..16) &&
                natIdNum.isNotEmpty() && (natIdNum.length in 8..16) &&
                userReside.isNotEmpty() && (userReside.length in 5..16)){

                Snackbar.make(view,  "Saving Profile...", Snackbar.LENGTH_LONG).setAnchorView(R.id.user_phone_number)
                    .show()

                //upload this info to db
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
            uploadProfileImg()
        }
    }

    private fun uploadProfileImg() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        val storage = FirebaseStorage.getInstance()
        val time = Date().time
        val reference = storage.reference
            .child("User Profiles")
            .child(time.toString() + "")
        reference.putFile(imageUri).addOnCompleteListener { task ->
            if(task.isSuccessful){
                reference.downloadUrl.addOnCompleteListener { imageUri ->
                    val filePath = imageUri.toString()
                    val obj = HashMap<String, Any>()
                    obj["image"] = filePath
                    database.reference
                        .child("Profile Pictures")
                        .child(FirebaseAuth.getInstance().uid!!)
                        .updateChildren(obj).addOnSuccessListener {
                            Snackbar.make(requireView(),  "Profile image saved", Snackbar.LENGTH_SHORT).setAnchorView(R.id.user_phone_number)
                                .show()
                        }
                        .addOnFailureListener {

                            Snackbar.make(requireView(),  "Profile image not saved. Please try again", Snackbar.LENGTH_LONG).setAnchorView(R.id.user_phone_number)
                                .show()

                        }
                }
            }
        }


    }
}