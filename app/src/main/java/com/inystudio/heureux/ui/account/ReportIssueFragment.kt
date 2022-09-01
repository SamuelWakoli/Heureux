package com.inystudio.heureux.ui.account

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.inystudio.heureux.R

class ReportIssueFragment : Fragment() {

    private lateinit var descriptionText: TextView
    private lateinit var reportBtn: Button
    private lateinit var errorText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_issue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        descriptionText = view.findViewById(R.id.desciption_text)
        val problemText = descriptionText.text.toString()

        errorText = view.findViewById(R.id.error_text)

        reportBtn = view.findViewById(R.id.report_btn)
        reportBtn.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    this.data = Uri.parse("mailto:swwakoli@gmail.com")
                    this.putExtra(Intent.EXTRA_SUBJECT, "Problem Report for Heureux")
                    this.putExtra(Intent.EXTRA_TEXT, problemText)
                    this.`package` = "com.google.android.gm"
                }
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                errorText.isVisible = true
                errorText.text = getString(R.string.no_gmail)
            }
        }
    }
 }

