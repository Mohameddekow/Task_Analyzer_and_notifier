package com.example.taskkeeperandanalyzer.auth.password

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val repository: PasswordRepository
): ViewModel() {

    fun resetPassword(
        email: String
    ): Task<Void> {
        return repository.resetPassword(email)
    }


    //re authenticate user b4 changing password
    fun reAuthenticateUser(password: String): Task<Void>{
        return repository.reAuthenticateUser(password)
    }


    //re authenticated user changing password
    fun changePassword(
        newPassword: String
    ): Task<Void>?{
        return  repository.changePassword(newPassword)
    }

}