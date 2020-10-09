package com.example.spacemss

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_welcome.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    //account is created inside the firebase, their own ID , get ID it is string type
    private var firebaseUserID:String=""


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {

            val intent = Intent(this@RegisterActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()
        register_btn.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val username: String = username_register.text.toString()
        val email: String = email_register.text.toString()
        val password: String = password_register.text.toString()

        if (username == "") {
            Toast.makeText(this, "please write username", Toast.LENGTH_SHORT).show()
        }

        else if (email=="")
    {
        Toast.makeText(this,"please write email",Toast.LENGTH_SHORT).show()
    }
        else if (password=="")
    {
        Toast.makeText(this,"please write password",Toast.LENGTH_SHORT).show()
    }
        else
        {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
                if(task.isSuccessful)
                {
                    firebaseUserID =mAuth.currentUser!!.uid
                    refUsers= FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                    val userHashMap=HashMap<String,Any>()
                    userHashMap["uid"]=firebaseUserID
                    userHashMap["username"]=username
                    userHashMap["profile"]="https://firebasestorage.googleapis.com/v0/b/spacemss-ef415.appspot.com/o/profile.png?alt=media&token=75bdbd24-f98d-4297-a51b-4e561dc178f4"
                    userHashMap["cover"]="https://firebasestorage.googleapis.com/v0/b/spacemss-ef415.appspot.com/o/cover_photo.jpg?alt=media&token=1badbd5a-9a71-47c0-8d8d-c6eda8e7d86a"
                    userHashMap["status"]="offline"
                    userHashMap["search"]=username.toLowerCase()
                    userHashMap["facebook"]="https://m.facebook.com"
                    userHashMap["instagram"]="https://m.instagram.com"
                    userHashMap["website"]="https://m.google.com"

                    refUsers.updateChildren(userHashMap)
                        .addOnCompleteListener { task  ->
                            if (task.isSuccessful)
                            {
                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                        }

                        }



                }else{
                    Toast.makeText(this,"Something wrong:"+ task.exception?.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
}
}
