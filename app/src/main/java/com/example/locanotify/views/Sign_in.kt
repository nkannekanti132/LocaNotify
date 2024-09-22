package com.example.locanotify.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.locanotify.utils.FirebaseUtils.firebaseAuth
import com.example.locanotify.R
import com.example.locanotify.utils.Extensions.toast
import com.google.firebase.auth.FirebaseUser


class sign_in : AppCompatActivity() {
    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputsArray: Array<EditText>
    lateinit var etSignInEmail:EditText
    lateinit var etSignInPassword:EditText
    lateinit var btnCreateAccount2:Button
    lateinit var btnSignIn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        etSignInEmail = findViewById(R.id.etSignInEmail)
        etSignInPassword = findViewById(R.id.etSignInPassword)
        btnCreateAccount2 = findViewById(R.id.btnCreateAccount2)
        btnSignIn = findViewById(R.id.btnSignIn)

        signInInputsArray = arrayOf(etSignInEmail, etSignInPassword)
        btnCreateAccount2.setOnClickListener {
            startActivity(Intent(this,activity_create_account::class.java))
        }

        btnSignIn.setOnClickListener {
            signInUser()
        }
    }
    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            startActivity(Intent(this, MainActivity::class.java))
            toast("welcome back")
        }
    }

    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()

    private fun signInUser() {
        signInEmail = etSignInEmail.text.toString().trim()
        signInPassword = etSignInPassword.text.toString().trim()

        if (notEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        toast("signed in successfully")
                        finish()
                    } else {
                        toast("sign in failed")
                    }
                }
        } else {
            signInInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }
    }
}