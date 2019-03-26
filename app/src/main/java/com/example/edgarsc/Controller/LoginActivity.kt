package com.example.edgarsc.Controller

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.edgarsc.R
import com.example.edgarsc.Services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.edgarsc.R.layout.activity_login)
        loginSpinner.visibility = View.INVISIBLE
    }

    fun loginCreateUserBtnClicked(view: View){
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    fun loginLoginBtnClicked(view: View){
        enableSpinner(true)
        val userEmail = loginEmailTxt.text.toString()
        val userPassword = loginPasswordTxt.text.toString()
        hideKeyboard()
        if (userEmail.isNotEmpty() && userPassword.isNotEmpty())
        {
            AuthService.loginUser( userEmail, userPassword) {loginSuccess ->
                if (loginSuccess){
                    println(App.prefs.authToken)
                    println(App.prefs.userEmail)
                    AuthService.findUserByEmail(this) { findSuccess ->
                        if (findSuccess) {
                            enableSpinner(false)
                            finish()
                        } else {errorToast()}
                    }
                }
                else {errorToast()}
            }
        } else {
            Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }

    }

    fun errorToast(){
        Toast.makeText(this,"Something went wrong, please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean){
        if (enable){
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }
        loginLoginBtn.isEnabled = !enable
        loginCreateUserBtn.isEnabled = !enable
    }

    fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}
