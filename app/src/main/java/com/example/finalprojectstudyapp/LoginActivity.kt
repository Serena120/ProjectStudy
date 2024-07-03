package com.example.finalprojectstudyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalprojectstudyapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginText.setOnClickListener {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginButton.setOnClickListener{
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java )
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        /*if (firebaseAuth.currentUser != null)
        {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }*/
    }
}