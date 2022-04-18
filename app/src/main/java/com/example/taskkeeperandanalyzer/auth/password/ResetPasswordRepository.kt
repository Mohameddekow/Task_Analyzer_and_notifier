package com.example.taskkeeperandanalyzer.auth.password


import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject



class ResetPasswordRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    fun resetPassword(
        email: String
    ): Task<Void> {
        return auth.sendPasswordResetEmail(email)
    }


}