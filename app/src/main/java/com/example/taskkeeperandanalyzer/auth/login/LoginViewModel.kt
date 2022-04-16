package com.example.taskkeeperandanalyzer.auth.login


import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
): ViewModel() {

    fun authenticateUser(
        email: String,
        password: String
    ): Task<AuthResult> {
        return repository.authenticateUser(email, password)
    }
}