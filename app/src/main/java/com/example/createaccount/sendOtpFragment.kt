package tech.demoproject.android_otp_verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.createaccount.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class sendOtpFragment : Fragment()  {

    private lateinit var inputMobile: EditText
    private lateinit var buttonGetOTP: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_send_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputMobile = view.findViewById(R.id.inputMobile)
        buttonGetOTP = view.findViewById(R.id.buttonGetOTP)
        progressBar = view.findViewById(R.id.progressBar)

        buttonGetOTP.setOnClickListener {
            if (inputMobile.text.toString().isEmpty()) {
                Toast.makeText(activity, "Enter mobile", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            buttonGetOTP.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE

            val options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber("+91${inputMobile.text.toString()}") // Change to your country code
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                        progressBar.visibility = View.GONE
                        buttonGetOTP.visibility = View.VISIBLE
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        progressBar.visibility = View.GONE
                        buttonGetOTP.visibility = View.VISIBLE
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onCodeSent(verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                        progressBar.visibility = View.GONE
                        buttonGetOTP.visibility = View.VISIBLE

                        val bundle = Bundle().apply {
                            putString("mobile", inputMobile.text.toString())
                            putString("verificationId", verificationId)
                        }

                        findNavController().navigate(R.id.action_sendOtpFragment_to_verifyOtpFragment, bundle)
                    }
                })
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }
}
