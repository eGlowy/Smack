package com.example.edgarsc.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.edgarsc.R
import com.example.edgarsc.Services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginCreateUserBtnClicked(view: View){
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    fun loginLoginBtnClicked(view: View){
        val userEmail = loginEmailTxt.text.toString()
        val userPassword = loginPasswordTxt.text.toString()
        AuthService.loginUser(this, userEmail, userPassword) {loginSuccess ->
            if (loginSuccess){
                println(AuthService.authToken)
                println(AuthService.userEmail)
                AuthService.findUserByEmail(this) { findSuccess ->
                    if (findSuccess) {}
                    finish()
                }
            }
        }
    }
}
