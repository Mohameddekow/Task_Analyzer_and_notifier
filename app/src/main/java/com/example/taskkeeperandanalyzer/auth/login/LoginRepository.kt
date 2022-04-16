package com.example.taskkeeperandanalyzer.auth.login


import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val auth: FirebaseAuth

) {


    fun authenticateUser(
        email: String,
        password: String
    ): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

}