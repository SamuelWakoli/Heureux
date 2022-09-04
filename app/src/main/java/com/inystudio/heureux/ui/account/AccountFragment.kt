package com.inystudio.heureux.ui.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.inystudio.heureux.R


class AccountFragment : Fragment() {
    private lateinit var makePaymentBtn: Button
    private lateinit var setProfileBtn: Button
    private lateinit var purchasesBtn: Button
    private lateinit var termsBtn: Button
    private lateinit var reportBtn: Button
    private lateinit var settingsBtn: Button

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

        userNameDisplay = view.findViewById(R.id.user_name_display)
        userEmailDisplay = view.findViewById(R.id.user_email_display)
        userPhoneDisplay = view.findViewById(R.id.user_phone_display)


        setProfileBtn = view.findViewById(R.id.set_profile_btn)
        setProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_profileSettingsFragment)
        }
        if (userNameDisplay.text != getString(R.string.user_name)) {
            setProfileBtn.visibility = View.GONE
        }

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
}