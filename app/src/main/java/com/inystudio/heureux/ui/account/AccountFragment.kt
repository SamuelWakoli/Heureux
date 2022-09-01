package com.inystudio.heureux.ui.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.inystudio.heureux.R


class AccountFragment : Fragment() {
    private lateinit var setProfileBtn: Button
    private lateinit var purchasesBtn: Button
    private lateinit var termsBtn: Button
    private lateinit var reportBtn: Button
    private lateinit var settingsBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfileBtn = view.findViewById(R.id.set_profile_btn)
        setProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_profileSettingsFragment)
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