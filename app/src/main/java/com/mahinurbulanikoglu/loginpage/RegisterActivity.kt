package com.mahinurbulanikoglu.loginpage

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mahinurbulanikoglu.loginpage.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {
    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth // FirebaseAuth nesnesini tanımla


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        // Firebase Auth başlat
        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("tr")

        mBinding.fullNameEt.onFocusChangeListener = this
        mBinding.emailEt.onFocusChangeListener = this
        mBinding.passwordEt.onFocusChangeListener = this
        mBinding.cpasswordEt.onFocusChangeListener = this
        mBinding.registerBtn.setOnClickListener(this) // Kayıt butonuna tıklama işlemi

    }

    private fun registerUser() {
        val email = mBinding.emailEt.text.toString()
        val pass = mBinding.passwordEt.text.toString()



        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "İşlem başarılı", Toast.LENGTH_SHORT).show()
                   // val main = Intent(this@RegisterActivity, RegisterActivity::class.java)
                    //startActivity(main)
                    finish()
                } else {
                    Log.e("RegisterError", "Kayıt sırasında bir hata oluştu: ${task.exception?.message}")
                    Toast.makeText(this@RegisterActivity, "İşlem Sırasında Bir Hata Oluştu", Toast.LENGTH_SHORT).show()
                }
            }

        }



    private fun validateFullName(): Boolean {
         var errorMessage: String? = null
         val value: String = mBinding.fullNameEt.text.toString()
         if (value.isEmpty()) {
             errorMessage = "Full name is required"
         }

         if (errorMessage != null) {
             mBinding.fullNameTil.apply {

                 isErrorEnabled = true
                 error = errorMessage

             }
         }
         return errorMessage == null
     }


       private fun validateEmail(): Boolean {
           var errorMessage: String? = null
           val value = mBinding.emailEt.text.toString()
           if (value.isEmpty()) {
               errorMessage = "Email is required"
           } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
               errorMessage = "Email address is invalid"
           }
           if (errorMessage != null) {
               mBinding.emailTil.apply {

                   isErrorEnabled = true
                   error = errorMessage

               }
           }
           return errorMessage == null
       }

    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val value = mBinding.passwordEt.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Password is required"
        } else if (value.length < 6) {
            errorMessage = "Password must be 6 characters long"
        }
        if (errorMessage != null) {
            mBinding.passwordTil.apply {

                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }


    private fun validateConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val value = mBinding.cpasswordEt.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Confirm password is required"
        } else if (value.length < 6) {
            errorMessage = "Confirm password must be 6 characters long"
        }
        if (errorMessage != null) {
            mBinding.cpasswordTil.apply {

                isErrorEnabled = true
                error = errorMessage

            }
        }
        return errorMessage == null
    }

        private fun validatePasswordAndConfirmPassword(): Boolean {
            var errorMessage: String? = null
            val password = mBinding.passwordEt.text.toString()
            val confirmPassword = mBinding.cpasswordEt.text.toString()
            if (password != confirmPassword) {
                errorMessage = "Confirm password doesn't match with password"
            }
            if (errorMessage != null) {
                mBinding.cpasswordTil.apply {

                    isErrorEnabled = true
                    error = errorMessage

                }
            }
            return errorMessage == null
        }



            override fun onClick(view: View?) {
                when (view?.id) {
                    R.id.registerBtn -> registerUser() // Kayıt işlemini başlat
                }
            }

            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                    if (view != null) {
                        when (view.id) {
                            R.id.fullNameEt -> {
                                if (hasFocus) {
                                    if(mBinding.fullNameTil.isErrorEnabled){
                                        mBinding.fullNameTil.isErrorEnabled = false
                                    }

                                } else {
                                    validateFullName()
                                }
                            }
                            R.id.emailEt -> {
                                if (hasFocus) {
                                    if(mBinding.emailTil.isErrorEnabled){
                                        mBinding.emailTil.isErrorEnabled = false
                                    }

                                } else {
                                    if(validateEmail()){
                                        // do validation for its uniqueness
                                    }
                                }
                            }
                            R.id.passwordEt -> {
                                if (hasFocus) {
                                    if(mBinding.passwordTil.isErrorEnabled){
                                        mBinding.passwordTil.isErrorEnabled = false
                                    }

                                } else {
                                    if (validatePassword() && mBinding.cpasswordEt.text!!.isNotEmpty() && validateConfirmPassword() &&
                                        validatePasswordAndConfirmPassword()) {
                                        if(mBinding.cpasswordTil.isErrorEnabled){
                                            mBinding.cpasswordTil.isErrorEnabled = false
                                        }
                                        mBinding.cpasswordTil.apply {
                                            setStartIconDrawable(R.drawable.check_circle_24)
                                            setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                                        }
                                    }
                                }
                            }
                            R.id.cpasswordEt -> {
                                if (hasFocus) {
                                    if(mBinding.cpasswordTil.isErrorEnabled){
                                        mBinding.cpasswordTil.isErrorEnabled = false
                                    }

                                } else {
                                    if(validateConfirmPassword() && validatePassword() && validatePasswordAndConfirmPassword()){
                                        if(mBinding.passwordTil.isErrorEnabled){
                                            mBinding.passwordTil.isErrorEnabled = false
                                        }
                                        mBinding.cpasswordTil.apply {
                                            setStartIconDrawable(R.drawable.check_circle_24)
                                            setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


            override fun onKey(view: View?, p1: Int, p2: KeyEvent?): Boolean {
                return false
            }
        }
