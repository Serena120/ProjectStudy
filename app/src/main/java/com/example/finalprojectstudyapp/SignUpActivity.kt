package com.example.finalprojectstudyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalprojectstudyapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val confirmpass = binding.confirmPass.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmpass.isNotEmpty()) {
                if (pass == confirmpass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this, "Signup Unsuccessful",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    this, "Please Fill in the details",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        if (firebaseAuth.currentUser != null)
        {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}