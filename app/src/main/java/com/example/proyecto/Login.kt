package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.regex.Matcher
import java.util.regex.Pattern


class Login : AppCompatActivity() {

    private val TAG: String = LoginActivity::class.java.getName()
    private val VALID_EMAIL_ADDRESS_REGEX: Pattern =
        Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    var emailEdit: EditText? = null, var passEdit:EditText? = null
    private var mAuth: FirebaseAuth? = null

    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailEdit = findViewById(R.id.editTextUsername)
        passEdit = findViewById(R.id.editTextPassword)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
    }


    @Override
    protected fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }


    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(getBaseContext(), HomeActivity::class.java)
            intent.putExtra("user", currentUser.email)
            startActivity(intent)
        } else {
            emailEdit.setText("")
            passEdit.setText("")
        }
    }


    private fun validateForm(): Boolean {
        var valid = true
        val email: String = emailEdit.getText().toString()
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Required")
            valid = false
        } else {
            emailEdit.setError(null)
        }
        val password: String = passEdit.getText().toString()
        if (TextUtils.isEmpty(password)) {
            passEdit.setError("Required")
            valid = false
        } else {
            passEdit.setError(null)
        }
        return valid
    }


    private fun signInUser(email: String, password: String) {
        if (validateForm()) {
            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?>() {
                    @Override
                    fun onComplete(@NonNull task: Task<AuthResult?>) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI
                            Log.d(TAG, "signInWithEmail: Success")
                            val user = mAuth!!.currentUser
                            updateUI(user)
                        } else {
                            // If Sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail: Failure", task.getException())
                            Toast.makeText(
                                this@Login,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(null)
                        }
                    }
                })
        }
    }


    fun isEmailValid(emailStr: String?): Boolean {
        val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr)
        return matcher.find()
    }


    fun login(view: View?) {
        val email: String = emailEdit.getText().toString()
        val pass: String = passEdit.getText().toString()
        if (!isEmailValid(email)) {
            Toast.makeText(this@Login, "Email is not a valid format", Toast.LENGTH_SHORT)
                .show()
            return
        }
        signInUser(email, pass)
    }


    fun signUp(view: View?) {
        val email: String = emailEdit.getText().toString()
        val pass: String = passEdit.getText().toString()
        if (!isEmailValid(email)) {
            Toast.makeText(this@Login, "Email is not a valid format", Toast.LENGTH_SHORT)
                .show()
            return
        }
        mAuth!!.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?>() {
                @Override
                fun onComplete(@NonNull task: Task<AuthResult?>) {
                    if (task.isSuccessful()) {
                        val user = mAuth!!.currentUser
                        Toast.makeText(
                            this@Login,
                            String.format("The user %s is successfully registered", user!!.email),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }).addOnFailureListener(this, object : OnFailureListener() {
                @Override
                fun onFailure(@NonNull e: Exception) {
                    Toast.makeText(this@Login, e.getMessage(), Toast.LENGTH_LONG).show()
                }
            })
    }


    fun forgotPassword(view: View?) {
        val email: String = emailEdit.getText().toString()
        if (!isEmailValid(email)) {
            Toast.makeText(this@Login, "Email is not a valid format", Toast.LENGTH_SHORT)
                .show()
            return
        }
        mAuth!!.sendPasswordResetEmail(email)
            .addOnCompleteListener(this, object : OnCompleteListener<Void?>() {
                @Override
                fun onComplete(@NonNull task: Task<Void?>?) {
                    Toast.makeText(
                        this@Login,
                        "Email instructions hace been sent, please check your email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}