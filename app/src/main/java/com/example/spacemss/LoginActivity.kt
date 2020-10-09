package com.example.spacemss

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import com.example.spacemss.R.id.toolbar_login
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_welcome.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_login)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()

        login_btn.setOnClickListener {
            loginUser()
        }

    }

    private fun loginUser() {
        val email: String = email_login.text.toString()
        val password: String = password_login.text.toString()

        if (email == "") {
            Toast.makeText(this, "please write email", Toast.LENGTH_SHORT).show()
        } else if (password == "") {
            Toast.makeText(this, "please write password", Toast.LENGTH_SHORT).show()
        } else {

            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this,"Something wrong:"+ task.exception?.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
