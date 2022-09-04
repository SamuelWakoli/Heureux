package com.inystudio.heureux.ui.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.inystudio.heureux.R

class AboutUsFragment : Fragment() {
    private lateinit var mapLocation: TextView
    private lateinit var phoneNum: TextView
    private lateinit var emailAddress: TextView
    private lateinit var webAddress: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapLocation = view.findViewById(R.id.about_map_location)
        phoneNum = view.findViewById(R.id.about_phone)
        emailAddress = view.findViewById(R.id.about_email)
        webAddress = view.findViewById(R.id.about_web)

        mapLocation.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=Jetro Chambers Westlands")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        phoneNum.setOnClickListener {
            Snackbar.make(view,
                "Press back, click options menu to call agent", Snackbar.LENGTH_LONG)
                .setAnchorView(phoneNum)
                .show()
        }

        emailAddress.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    this.data = Uri.parse("mailto:info@heureuxproperties.co.ke")
                    this.`package` = "com.google.android.gm"
                }
                startActivity(intent)
            }catch (ex: ActivityNotFoundException){
                try {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:info@heureuxproperties.co.ke")
                    startActivity(intent)
                }catch (ex: ActivityNotFoundException) {
                    emailAddress.setText(R.string.no_gmail)
                }
            }
        }

        webAddress.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.heureuxproperties.co.ke")
            startActivity(intent)
        }
    }
}