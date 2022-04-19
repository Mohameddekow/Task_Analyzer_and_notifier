package com.example.taskkeeperandanalyzer.auth.password


import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject



class PasswordRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    fun resetPassword(
        email: String
    ): Task<Void> {
        return auth.sendPasswordResetEmail(email)
    }


    //re authenticate user b4 changing password
    fun reAuthenticateUser(
        password: String
    ): Task<Void> {

        val currentUser = auth.currentUser

        val credentials = EmailAuthProvider.getCredential(currentUser?.email!!, password)

        return currentUser.reauthenticate(credentials)

    }

    //re authenticated user changing password
    fun changePassword(
        newPassword: String
    ): Task<Void>?{

        val currentUser = auth.currentUser

        return currentUser?.updatePassword(newPassword)
    }
}