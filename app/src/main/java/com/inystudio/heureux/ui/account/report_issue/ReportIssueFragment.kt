package com.inystudio.heureux.ui.account.report_issue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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
        reportBtn = view.findViewById(R.id.send_report_btn)
        reportBtn.setOnClickListener {

            if (errorText.text.toString().length < 16) errorText.error = "Please tell us more information"
            val enteredErrorText = errorText.text.toString()
            if (enteredErrorText.isNotEmpty() && enteredErrorText.length >= 16) {
                // when the report has been successfully updated to the data base, {errorText} to show that report
                //has been submited. This is after database success
            }
        }
    }
 }

