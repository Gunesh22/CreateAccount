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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class verifyOtpFragment : Fragment() {

    private lateinit var inputCode1: EditText
    private lateinit var inputCode2: EditText
    private lateinit var inputCode3: EditText
    private lateinit var inputCode4: EditText
    private lateinit var inputCode5: EditText
    private lateinit var inputCode6: EditText
    private lateinit var buttonVerifyOTP: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verify_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputCode1 = view.findViewById(R.id.inputCode1)
        inputCode2 = view.findViewById(R.id.inputCode2)
        inputCode3 = view.findViewById(R.id.inputCode3)
        inputCode4 = view.findViewById(R.id.inputCode4)
        inputCode5 = view.findViewById(R.id.inputCode5)
        inputCode6 = view.findViewById(R.id.inputCode6)
        buttonVerifyOTP = view.findViewById(R.id.buttonVerify)
        progressBar = view.findViewById(R.id.progressBar)

        // Retrieve the mobile number and verification ID from the arguments
        val mobile = arguments?.getString("mobile")
        val verificationId = arguments?.getString("verificationId")

        buttonVerifyOTP.setOnClickListener {
            // Concatenate all the input codes to form the full OTP
            val code = inputCode1.text.toString() + inputCode2.text.toString() +
                    inputCode3.text.toString() + inputCode4.text.toString() +
                    inputCode5.text.toString() + inputCode6.text.toString()

            if (code.isEmpty()) {
                Toast.makeText(activity, "Enter valid code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            buttonVerifyOTP.visibility = View.INVISIBLE

            if (verificationId != null) {
                // Create a credential with the verification ID and the entered code
                val credential = PhoneAuthProvider.getCredential(verificationId, code)
                signInWithPhoneAuthCredential(credential)
            } else {
                progressBar.visibility = View.GONE
                buttonVerifyOTP.visibility = View.VISIBLE
                Toast.makeText(activity, "Verification ID not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to sign in with the PhoneAuthCredential and handle the success/failure
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // OTP verification was successful, navigate to the next screen
                    progressBar.visibility = View.GONE
                    buttonVerifyOTP.visibility = View.VISIBLE

                    // Navigation to the next fragment
                    findNavController().navigate(R.id.action_verifyOtpFragment_to_verificationFragment)
                } else {
                    // OTP verification failed, show an error message
                    progressBar.visibility = View.GONE
                    buttonVerifyOTP.visibility = View.VISIBLE
                    Toast.makeText(activity, "Verification failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
