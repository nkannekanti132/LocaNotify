package com.example.locanotify.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.locanotify.R
import com.example.locanotify.utils.Extensions.toast
import com.example.locanotify.utils.FirebaseUtils.firebaseAuth
import com.example.locanotify.utils.FirebaseUtils.firebaseUser


class activity_create_account : AppCompatActivity() {
    lateinit var userEmail: String
    lateinit var userPassword: String
    lateinit var createAccountInputsArray: Array<EditText>

    lateinit var email:EditText
    lateinit var password:EditText
    lateinit var confirmPassword:EditText
    lateinit var btncreate:Button
    lateinit var btnSignIn2:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        confirmPassword = findViewById(R.id.etConfirmPassword)
        btncreate = findViewById(R.id.btnCreateAccount)
        btnSignIn2 = findViewById(R.id.btnSignIn2)
        createAccountInputsArray = arrayOf(email, password, confirmPassword)
        btncreate.setOnClickListener {
            signIn()
        }

        btnSignIn2.setOnClickListener {
            startActivity(Intent(this, sign_in::class.java))
            toast("please sign into your account")
            finish()
        }
    }

    /* check if there's a signed-in user*/


    private fun notEmpty(): Boolean = email.text.toString().trim().isNotEmpty() &&
            password.text.toString().trim().isNotEmpty() &&
            confirmPassword.text.toString().trim().isNotEmpty()

    private fun identicalPassword(): Boolean {
        var identical = false
        if (notEmpty() &&
            password.text.toString().trim() == confirmPassword.text.toString().trim()
        ) {
            identical = true
        } else if (!notEmpty()) {
            createAccountInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        } else {
            toast("passwords are not matching !")
        }
        return identical
    }

    private fun signIn() {
        if (identicalPassword()) {
            // identicalPassword() returns true only  when inputs are not empty and passwords are identical
            userEmail = email.text.toString().trim()
            userPassword = password.text.toString().trim()

            /*create a user*/
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        toast("created account successfully !")
                        sendEmailVerification()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        toast("failed to Authenticate !")
                    }
                }
        }
    }

    /* send verification email to the new user. This will only
    *  work if the firebase user is not null.
    */

    private fun sendEmailVerification() {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("email sent to $userEmail")
                }
            }
        }
    }
}